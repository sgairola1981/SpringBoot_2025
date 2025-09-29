const express = require("express");
const bodyParser = require("body-parser");
const mysql = require("mysql2");
const session = require("express-session");
const bcrypt = require("bcrypt");

const app = express();

// Middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.set("view engine", "ejs");

app.use(session({
  secret: "secret123",
  resave: false,
  saveUninitialized: true
}));

// MySQL connection
const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "gairola",
  database: "sa",
  port: 3306
});

db.connect(err => {
  if (err) throw err;
  console.log("✅ Connected to MySQL");
});

// Middleware to protect routes
function isLoggedIn(req, res, next) {
  if (req.session.userId) return next();
  res.redirect("/login");
}

// ================= Registration =================
app.get("/register", (req, res) => res.render("register", { error: null,user: {} }));
app.post("/register", async (req, res) => {
  const { id, name, email, password } = req.body;

  try {
    if (id) {
      // Update existing user
      await db.execute(
        "UPDATE t_users SET name = ?, email = ?, password = ? WHERE id = ?",
        [name, email, password, id]
      );
    } else {
      // Add new user
      await db.execute(
        "INSERT INTO t_users (name, email, password) VALUES (?, ?, ?)",
        [name, email, password]
      );
    }
    res.redirect("/users");
  } catch (err) {
    console.error(err);
    res.render("register", { error: "Something went wrong!", user: req.body });
  }
});

// ================= Login =================
app.get("/login", (req, res) => res.render("login", { error: null }));

app.post("/login", (req, res) => {
  const { email, password } = req.body;
  db.query("SELECT * FROM t_users WHERE email = ?", [email], async (err, results) => {
    if (err) throw err;
    if (results.length === 0) return res.render("login", { error: "User not found" });

    const user = results[0];
    const match = await bcrypt.compare(password, user.password);

    if (match) {
      req.session.userId = user.id;
      req.session.userName = user.name;
      res.redirect("/dashboard");
    } else {
      res.render("login", { error: "Incorrect password" });
    }
  });
});

// 👉 Default route (Homepage → Login page)
app.get("/", (req, res) => {
  res.render("login", { error: null });
});
// ================= Logout =================
app.get("/logout", (req, res) => {
  req.session.destroy();
  res.redirect("/login");
});
app.get("/home", (req, res) => {
  res.render("home", { name: req.session.userName }); // pass username or other data
});

// ================= Dashboard Layout =================
app.get("/dashboard", isLoggedIn, (req, res) => {
  res.render("layout", { name: req.session.userName });
});

// ================= Users Partial =================
// Users List with Pagination, Search, Sorting
app.get("/users", (req, res) => {
  const page = parseInt(req.query.page) || 1;
  const limit = 5;
  const offset = (page - 1) * limit;
  const search = req.query.search || "";
  const sort = req.query.sort || "id";          
  const order = req.query.order === "desc" ? "DESC" : "ASC";

  const countQuery = "SELECT COUNT(*) AS count FROM t_users WHERE name LIKE ? OR email LIKE ?";
  const dataQuery = `SELECT * FROM t_users 
                     WHERE name LIKE ? OR email LIKE ? 
                     ORDER BY ${sort} ${order} 
                     LIMIT ? OFFSET ?`;

  db.query(countQuery, [`%${search}%`, `%${search}%`], (err, countResult) => {
    if (err) throw err;

    const totalUsers = countResult[0].count;
    const totalPages = Math.ceil(totalUsers / limit);

    db.query(dataQuery, [`%${search}%`, `%${search}%`, limit, offset], (err, results) => {
      if (err) throw err;

      res.render("users", {
        users: results,
        currentPage: page,
        totalPages,
        search,
        sort,
        order
      });
    });
  });
});

// ================= Add User Partial =================
app.get("/add-user", isLoggedIn, (req, res) => {
  res.render("add-user", { user: null });
});

app.post("/add-user", isLoggedIn, async (req, res) => {
  const { name, email, password } = req.body;
  const hashedPassword = await bcrypt.hash(password, 10);
  db.query(
    "INSERT INTO t_users (name, email, password) VALUES (?, ?, ?)",
    [name, email, hashedPassword],
    err => {
      if (err) throw err;
      res.redirect("/users");
    }
  );
});

// ================= Edit/Delete =================
app.get("/edit/:id", isLoggedIn, (req, res) => {
  db.query("SELECT * FROM t_users WHERE id = ?", [req.params.id], (err, results) => {
    if (err) throw err;
    res.render("add-user", { user: results[0] });
  });
});

app.post("/edit/:id", isLoggedIn, (req, res) => {
  const { name, email } = req.body;
  db.query(
    "UPDATE t_users SET name = ?, email = ? WHERE id = ?",
    [name, email, req.params.id],
    err => {
      if (err) throw err;
      res.redirect("/users");
    }
  );
});

app.get("/delete/:id", isLoggedIn, (req, res) => {
  db.query("DELETE FROM t_users WHERE id = ?", [req.params.id], err => {
    if (err) throw err;
    res.redirect("/users");
  });
});

// ================= Start Server =================
app.listen(3000, () => console.log("🚀 Server running on http://localhost:3000"));

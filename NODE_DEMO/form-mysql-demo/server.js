const express = require("express");
const bodyParser = require("body-parser");
const mysql = require("mysql2");

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.set("view engine", "ejs");

// MySQL connection
const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "gairola",
  database: "sa",
  port: 3306
});

db.connect((err) => {
  if (err) throw err;
  console.log("✅ Connected to MySQL");
});

// Show form
app.get("/", (req, res) => {
  res.render("index");
});

// Add new user
app.post("/submit", (req, res) => {
  const { name, email } = req.body;
  db.query("INSERT INTO users (name, email) VALUES (?, ?)", [name, email], (err) => {
    if (err) throw err;
    res.redirect("/users");
  });
});

// List users with paging & sorting
app.get("/users", (req, res) => {
  let page = parseInt(req.query.page) || 1;
  let limit = parseInt(req.query.limit) || 5;
  let sort = req.query.sort || "id";
  let order = req.query.order || "ASC";

  let offset = (page - 1) * limit;

  db.query("SELECT COUNT(*) AS count FROM users", (err, countResult) => {
    if (err) throw err;

    const totalUsers = countResult[0].count;
    const totalPages = Math.ceil(totalUsers / limit);

    const sql = `SELECT * FROM users ORDER BY ${sort} ${order} LIMIT ? OFFSET ?`;
    db.query(sql, [limit, offset], (err, results) => {
      if (err) throw err;

      res.render("users", {
        users: results,
        page,
        totalPages,
        limit,
        sort,
        order
      });
    });
  });
});

// Edit user form
app.get("/edit/:id", (req, res) => {
  const userId = req.params.id;
  db.query("SELECT * FROM users WHERE id = ?", [userId], (err, results) => {
    if (err) throw err;
    res.render("edit", { user: results[0] });
  });
});

// Update user
app.post("/edit/:id", (req, res) => {
  const userId = req.params.id;
  const { name, email } = req.body;
  db.query(
    "UPDATE users SET name = ?, email = ? WHERE id = ?",
    [name, email, userId],
    (err) => {
      if (err) throw err;
      res.redirect("/users");
    }
  );
});

// Delete user
app.get("/delete/:id", (req, res) => {
  const userId = req.params.id;
  db.query("DELETE FROM users WHERE id = ?", [userId], (err) => {
    if (err) throw err;
    res.redirect("/users");
  });
});

// Start server
app.listen(3000, () => console.log("🚀 Server running on http://localhost:3000"));

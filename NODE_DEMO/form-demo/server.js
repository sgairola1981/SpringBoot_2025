const express = require("express");
const bodyParser = require("body-parser");
const app = express();

// Middleware to parse form data
app.use(bodyParser.urlencoded({ extended: true }));

// Serve HTML form
app.get("/", (req, res) => {
  res.send(`
    <h2>User Form</h2>
    <form action="/submit" method="post">
      <label>Name:</label>
      <input type="text" name="name" required />
      <br><br>
      <label>Email:</label>
      <input type="email" name="email" required />
      <br><br>
      <button type="submit">Submit</button>
    </form>
  `);
});

// Handle form submission
app.post("/submit", (req, res) => {
  const { name, email } = req.body;
  res.send(`<h3>Form Submitted</h3><p>Name: ${name}</p><p>Email: ${email}</p>`);
});

// Start server
app.listen(3000, () => {
  console.log("Server running on http://localhost:3000");
});

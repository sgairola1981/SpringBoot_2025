import axios from "axios";
import { useState } from "react";

export default function Login({ setToken, goToRegister }) {

  const [username, setU] = useState("");
  const [password, setP] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
const BASE_URL = `http://${window.location.hostname}:8888`;
  const login = async () => {

    if (!username || !password) {
      setError("Please enter username and password");
      return;
    }

    try {
      setLoading(true);
      setError("");

        const res = await axios.post(`${BASE_URL}/auth/login`, {

        username,
        password
      });

      console.log("LOGIN RESPONSE:", res.data); // 🔍 debug

      // ✅ handle both response types
      const token = res.data.token || res.data;

      // 🔥 STORE DATA (VERY IMPORTANT)
      localStorage.setItem("token", token);
      localStorage.setItem("username", username);

      // ✅ update app state
      setToken(token);

    } catch (err) {
      console.error(err);

      if (err.response?.status === 401) {
        setError("Invalid username or password");
      } else {
        setError("Server error. Try again.");
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>

        <h2 style={styles.title}>Welcome Back 👋</h2>

        {error && <p style={styles.error}>{error}</p>}

        <input
          style={styles.input}
          placeholder="Username"
          value={username}
          onChange={e => setU(e.target.value)}
        />

        <input
          style={styles.input}
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setP(e.target.value)}
          onKeyDown={e => e.key === "Enter" && login()} // ✅ enter login
        />

        <button
          style={styles.button}
          onClick={login}
          disabled={loading}
        >
          {loading ? "Logging in..." : "Login"}
        </button>

        <p style={{ marginTop: "15px" }}>
          Don't have an account?{" "}
          <span style={styles.link} onClick={goToRegister}>
            Register
          </span>
        </p>

      </div>
    </div>
  );
}

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(to right, #667eea, #764ba2)"
  },

  card: {
    background: "#fff",
    padding: "30px",
    borderRadius: "10px",
    width: "300px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.2)",
    textAlign: "center"
  },

  title: {
    marginBottom: "20px"
  },

  input: {
    width: "100%",
    padding: "10px",
    margin: "10px 0",
    borderRadius: "5px",
    border: "1px solid #ccc",
    outline: "none"
  },

  button: {
    width: "100%",
    padding: "10px",
    background: "#667eea",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    fontWeight: "bold"
  },

  error: {
    color: "red",
    fontSize: "14px"
  },

  link: {
    color: "#667eea",
    cursor: "pointer",
    fontWeight: "bold"
  }
};
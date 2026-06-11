import axios from "axios";
import { useState } from "react";

// ✅ Works for localhost + mobile
//const BASE_URL = `http://${window.location.hostname}:8888`;
const BASE_URL = `https://${window.location.hostname}:8443`;

export default function Register({ goToLogin }) {

  const [username, setU] = useState("");
  const [password, setP] = useState("");
  const [msg, setMsg] = useState("");

  const register = async () => {

    if (!username || !password) {
      setMsg("❌ Please enter username and password");
      return;
    }

    try {
      await axios.post(`${BASE_URL}/auth/register`, {
        username,
        password
      });

      setMsg("✅ Registration successful! Please login.");

      setTimeout(() => {
        goToLogin();
      }, 1500);

    } catch (e) {
      setMsg("❌ User already exists or server error");
    }
  };

  return (
    <div style={styles.container}>

      <div style={styles.card}>
        <h2 style={styles.title}>📝 Register</h2>

        {msg && <p>{msg}</p>}

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
          onKeyDown={e => e.key === "Enter" && register()}
        />

        <button style={styles.button} onClick={register}>
          Register
        </button>

        <p style={{ marginTop: 10 }}>
          Already have account?{" "}
          <span style={styles.link} onClick={goToLogin}>
            Login
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
    background: "linear-gradient(to right, #43cea2, #185a9d)",
  },
  card: {
    background: "#fff",
    padding: "30px",
    borderRadius: "12px",
    width: "300px",
    textAlign: "center",
    boxShadow: "0 8px 20px rgba(0,0,0,0.2)"
  },
  title: {
    marginBottom: "20px"
  },
  input: {
    width: "100%",
    padding: "10px",
    margin: "10px 0",
    borderRadius: "6px",
    border: "1px solid #ccc"
  },
  button: {
    width: "100%",
    padding: "10px",
    background: "#43cea2",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "bold"
  },
  link: {
    color: "#185a9d",
    cursor: "pointer",
    fontWeight: "bold"
  }
};
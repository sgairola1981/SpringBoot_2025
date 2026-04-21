import { useState } from "react";
import Login from "./Login";
import Register from "./Register";
import Chat from "./Chat";
import VideoCall from "./VideoCall";

function App() {

  const [token, setToken] = useState(localStorage.getItem("token"));
  const [page, setPage] = useState("login");

  // ✅ If logged in → Chat
  if (token) {
    return <Chat setToken={setToken} />;
  }

  // 🔹 Register page
  if (page === "register") {
    return <Register goToLogin={() => setPage("login")} />;
  }

  // 🔹 Login page
  return (
    <Login
      setToken={setToken}
      goToRegister={() => setPage("register")}
    />
  );
}

export default App;
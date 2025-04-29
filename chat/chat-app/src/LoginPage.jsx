import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import './LoginPage.css';

const LoginPage = () => {
  const [username, setUsername] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    if (username.trim()) {
      localStorage.setItem("username", username);
      navigate("/chat");
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-gray-100">
      <form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-md w-full max-w-sm">
        <h2 className="text-2xl mb-4 font-semibold text-center">Login</h2>
        <input
          type="text"
          placeholder="Enter username"
          className="border w-full px-3 py-2 mb-4 rounded"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Enter Chat
        </button>
      </form>
    </div>
  );
};

export default LoginPage;

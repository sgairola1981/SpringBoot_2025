import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useEffect, useState, useRef } from "react";
import axios from "axios";
import VideoCall from "./VideoCall";

// ✅ GLOBAL BASE URL
const BASE_URL = `http://${window.location.hostname}:8888`;

export default function Chat({ setToken }) {

  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  const [users, setUsers] = useState([]);
  const [msgs, setMsgs] = useState([]);
  const [input, setInput] = useState("");
  const [selectedUser, setSelectedUser] = useState("");

  // 🔥 NEW
  const [inCall, setInCall] = useState(false);

  const clientRef = useRef(null);

  useEffect(() => {
    if (!token) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(`${BASE_URL}/chat`),

      connectHeaders: {
        Authorization: "Bearer " + token
      },

      onConnect: () => {
        console.log("✅ Connected as:", username);

        // 💬 CHAT
        client.subscribe("/user/queue/messages", (msg) => {
          const body = JSON.parse(msg.body);
          setMsgs(prev => [
            ...prev,
            { sender: body.sender, content: body.content }
          ]);
        });

        // 📞 CALL SIGNAL
        client.subscribe("/user/queue/call", (msg) => {
          const data = JSON.parse(msg.body);

          console.log("📞 Incoming:", data);

          if (data.type === "offer") {
            setSelectedUser(data.sender);
            setInCall(true); // 🔥 open call UI
          }
        });

        loadUsers();
      }
    });

    client.activate();
    clientRef.current = client;

    return () => client.deactivate();

  }, [token]);

  // 🔹 Load users
  const loadUsers = async () => {
    try {
      const res = await axios.get(`${BASE_URL}/users/online`, {
        headers: { Authorization: "Bearer " + token }
      });

      setUsers(res.data.filter(u => u !== username));

    } catch (e) {
      console.error("User load failed", e);
    }
  };

  // 🔹 Send message
  const send = () => {

    if (!selectedUser) {
      alert("Select user first");
      return;
    }

    if (!input.trim()) return;

    clientRef.current.publish({
      destination: "/app/private-message",
      body: JSON.stringify({
        receiver: selectedUser,
        content: input
      })
    });

    setMsgs(prev => [
      ...prev,
      { sender: "Me", content: input }
    ]);

    setInput("");
  };

  // 🔹 Logout
  const logout = () => {
    localStorage.clear();
    setToken(null);
  };

  return (
    <div style={styles.container}>

      {/* SIDEBAR */}
      <div style={styles.sidebar}>

        <div style={styles.profile}>
          <div style={styles.avatar}>{username?.charAt(0).toUpperCase()}</div>
          <span>{username}</span>
          <button onClick={logout} style={styles.logout}>Logout</button>
        </div>

        <div style={styles.userList}>
          {users.map(u => (
            <div
              key={u}
              onClick={() => setSelectedUser(u)}
              style={{
                ...styles.userItem,
                background: selectedUser === u ? "#2a3942" : "transparent"
              }}
            >
              <div style={styles.avatar}>
                {u.charAt(0).toUpperCase()}
              </div>

              <div>
                <div>{u}</div>
                <div style={styles.online}>online</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* CHAT AREA */}
      <div style={styles.chatArea}>

        <div style={styles.header}>
          💬 {selectedUser || "Select a user"}

          {/* 📞 CALL BUTTON */}
          {selectedUser && (
            <button
              style={{ marginLeft: "20px", cursor: "pointer" }}
              onClick={() => setInCall(true)}
            >
              📞 Call
            </button>
          )}
        </div>

        <div style={styles.messages}>
          {msgs.map((m, i) => {
            const isMe = m.sender === "Me";

            return (
              <div
                key={i}
                style={{
                  ...styles.msg,
                  alignSelf: isMe ? "flex-end" : "flex-start",
                  background: isMe ? "#d9fdd3" : "#fff"
                }}
              >
                {!isMe && (
                  <div style={styles.sender}>{m.sender}</div>
                )}
                {m.content}
              </div>
            );
          })}
        </div>

        <div style={styles.inputArea}>
          <input
            style={styles.input}
            value={input}
            onChange={e => setInput(e.target.value)}
            placeholder="Type message..."
            onKeyDown={e => e.key === "Enter" && send()}
          />

          <button style={styles.sendBtn} onClick={send}>
            ➤
          </button>
        </div>

      </div>

      {/* 📞 VIDEO CALL POPUP */}
      {inCall && (
        <VideoCall
          client={clientRef.current}
          selectedUser={selectedUser}
          username={username}
          onClose={() => setInCall(false)}
        />
      )}
    </div>
  );
}
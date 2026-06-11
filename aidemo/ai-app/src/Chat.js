import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useEffect, useState, useRef } from "react";
import axios from "axios";
import VideoCall from "./VideoCall";

const BASE_URL = `https://${window.location.hostname}:8443`;

export default function Chat({ setToken }) {

  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  const [users, setUsers] = useState([]);
  const [msgs, setMsgs] = useState([]);
  const [input, setInput] = useState("");
  const [selectedUser, setSelectedUser] = useState("");
  const [inCall, setInCall] = useState(false);

  const clientRef = useRef(null);

  useEffect(() => {

    if (!token) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(`${BASE_URL}/chat`),

      connectHeaders: {
        Authorization: "Bearer " + token
      },

      debug: () => {},

      reconnectDelay: 5000,

      onConnect: () => {
        console.log("✅ Connected:", username);

        // ✅ RECEIVE MESSAGES
        client.subscribe("/user/queue/messages", (msg) => {
          const body = JSON.parse(msg.body);

          setMsgs(prev => [
            ...prev,
            {
              sender: body.sender,
              content: body.content,
              time: new Date().toLocaleTimeString()
            }
          ]);
        });

        // ✅ CALL SIGNAL
        client.subscribe("/user/queue/call", (msg) => {
          const data = JSON.parse(msg.body);

          if (data.type === "offer") {
            setSelectedUser(data.sender);
            setInCall(true);
          }
        });

        loadUsers();
      },

      onStompError: (frame) => {
        console.error("❌ STOMP ERROR:", frame);
      }
    });

    client.activate();
    clientRef.current = client;

    return () => {
      if (client) client.deactivate();
    };

  }, [token]);

  // ✅ LOAD USERS
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

  // ✅ SEND MESSAGE (FIXED)
  const send = () => {

    if (!selectedUser) return alert("Select user first");
    if (!input.trim()) return;

    clientRef.current.publish({
      destination: "/app/private-message",
      body: JSON.stringify({
        sender: username,              // ✅ FIXED
        receiver: selectedUser,
        content: input
      })
    });

    // ✅ Add own message instantly
    setMsgs(prev => [
      ...prev,
      {
        sender: "Me",
        content: input,
        time: new Date().toLocaleTimeString()
      }
    ]);

    setInput("");
  };

  // ✅ LOGOUT
  const logout = () => {
    localStorage.clear();
    setToken(null);
  };

  return (
    <div style={styles.container}>

      {/* SIDEBAR */}
      <div style={styles.sidebar}>

        <div style={styles.profile}>
          <div style={styles.avatar}>
            {username?.charAt(0).toUpperCase()}
          </div>

          <span>{username}</span>

          <button onClick={logout} style={styles.logout}>
            Logout
          </button>
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

        {/* HEADER */}
        <div style={styles.header}>
          <span>💬 {selectedUser || "Select user"}</span>

          {selectedUser && (
            <button
              style={styles.callBtn}
              onClick={() => setInCall(true)}
            >
              📞
            </button>
          )}
        </div>

        {/* MESSAGES */}
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

                <div style={styles.time}>{m.time}</div>
              </div>
            );
          })}
        </div>

        {/* INPUT */}
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

      {/* VIDEO CALL */}
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

const styles = {

  container: {
    display: "flex",
    height: "100vh",
    fontFamily: "Segoe UI",
    background: "#d1d7db"
  },

  sidebar: {
    width: "280px",
    background: "#111b21",
    color: "#fff",
    display: "flex",
    flexDirection: "column"
  },

  profile: {
    padding: "15px",
    display: "flex",
    alignItems: "center",
    gap: "10px",
    borderBottom: "1px solid #222"
  },

  avatar: {
    width: "35px",
    height: "35px",
    borderRadius: "50%",
    background: "#4caf50",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: "bold"
  },

  logout: {
    marginLeft: "auto",
    background: "#ff4d4f",
    border: "none",
    color: "#fff",
    padding: "5px 10px",
    borderRadius: "5px",
    cursor: "pointer"
  },

  userList: {
    flex: 1,
    overflowY: "auto"
  },

  userItem: {
    padding: "12px",
    display: "flex",
    gap: "10px",
    cursor: "pointer",
    borderBottom: "1px solid #222"
  },

  online: {
    fontSize: "11px",
    color: "#4caf50"
  },

  chatArea: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    background: "#efeae2"
  },

  header: {
    padding: "15px",
    background: "#202c33",
    color: "#fff",
    display: "flex",
    justifyContent: "space-between"
  },

  callBtn: {
    background: "#00a884",
    border: "none",
    color: "#fff",
    padding: "6px 12px",
    borderRadius: "5px",
    cursor: "pointer"
  },

  messages: {
    flex: 1,
    padding: "20px",
    display: "flex",
    flexDirection: "column",
    gap: "10px",
    overflowY: "auto"
  },

  msg: {
    padding: "10px 14px",
    borderRadius: "10px",
    maxWidth: "60%",
    boxShadow: "0 1px 2px rgba(0,0,0,0.2)"
  },

  sender: {
    fontSize: "11px",
    fontWeight: "bold",
    marginBottom: "3px"
  },

  inputArea: {
    display: "flex",
    padding: "10px",
    background: "#f0f2f5"
  },

  input: {
    flex: 1,
    padding: "10px",
    borderRadius: "20px",
    border: "none",
    outline: "none"
  },

  sendBtn: {
    marginLeft: "10px",
    padding: "10px 15px",
    border: "none",
    background: "#00a884",
    color: "#fff",
    borderRadius: "50%",
    cursor: "pointer"
  }
};
import React, { useEffect, useState } from "react";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './ChatPage.css';

let stompClient = null;

const ChatPage = () => {
  const [message, setMessage] = useState("");
  const [chatMessages, setChatMessages] = useState([]);
  const [users, setUsers] = useState([]);
  const [toUser, setToUser] = useState("");
  const username = localStorage.getItem("username");

  useEffect(() => {
    const socket = new SockJS("http://localhost:9999/ws"); // adjust port if needed
    stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("Connected to WebSocket");

        stompClient.subscribe("/user/queue/messages", (msg) => {
          const newMsg = JSON.parse(msg.body);
          setChatMessages(prev => [...prev, newMsg]);
        });

        stompClient.subscribe("/topic/online", (msg) => {
          const onlineUsers = JSON.parse(msg.body);
          setUsers(onlineUsers.filter(user => user !== username));
        });

        stompClient.publish({
          destination: "/app/online",
          body: username,
        });
      },
      onStompError: (frame) => {
        console.error("STOMP Error:", frame);
      }
    });

    stompClient.activate();
  }, [username]);

  const sendMessage = () => {
    if (!toUser) {
      alert("Please select a user.");
      return;
    }
    if (!message.trim()) {
      alert("Cannot send empty message.");
      return;
    }

    const msgObj = {
      from: username,
      to: toUser,
      content: message
    };
    stompClient.publish({
      destination: "/app/chat",
      body: JSON.stringify(msgObj),
    });
    setChatMessages(prev => [...prev, msgObj]);
    setMessage("");
  };

  return (
    <div className="p-6 max-w-2xl mx-auto">
      <h2 className="text-lg font-semibold mb-4">Welcome: {username}</h2>

      <select
        value={toUser}
        onChange={(e) => setToUser(e.target.value)}
        className="w-full p-2 mb-4 border rounded"
      >
        <option value="">-- Select User --</option>
        {users.map((user, idx) => (
          <option key={idx} value={user}>{user}</option>
        ))}
      </select>

      <div className="h-60 overflow-y-auto border p-4 mb-4 rounded bg-gray-50">
        {chatMessages.map((msg, idx) => (
          <div key={idx} className={`mb-2 p-2 rounded ${msg.from === username ? 'bg-green-200 text-right' : 'bg-blue-200'}`}>
            <b>{msg.from === username ? "You" : msg.from}</b>: {msg.content}
          </div>
        ))}
      </div>

      <div className="flex gap-2">
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type your message..."
          className="flex-1 border rounded p-2"
        />
        <button
          onClick={sendMessage}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Send
        </button>
      </div>
    </div>
  );
};

export default ChatPage;

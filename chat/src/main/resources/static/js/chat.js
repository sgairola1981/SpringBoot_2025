let stompClient = null;
let username = null;
let selectedUser = null;

function connect() {
  username = document.getElementById("usernameInput").value;
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function () {
    document.getElementById("loginScreen").style.display = "none";
    document.getElementById("chatScreen").style.display = "block";
    document.getElementById("userLabel").innerText = username;

    // Subscribe to personal queue
    stompClient.subscribe('/user/queue/messages', function (message) {
      const msg = JSON.parse(message.body);
      document.getElementById("chatBox").innerHTML += `<li><b>${msg.sender}</b>: ${msg.content}</li>`;
    });

    // Fetch online users
    fetchOnlineUsers();
  });
}

function fetchOnlineUsers() {
  fetch("/")
    .then(response => response.text())
    .then(html => {
      const users = [...document.querySelectorAll("#userList")];
      users.innerHTML = "";
      document.querySelectorAll("li.userItem").forEach(li => li.remove());

      const userList = document.getElementById("userList");
      const usersFromPage = [...new Set(html.match(/<li class="userItem">(.*?)<\/li>/g))];

      usersFromPage.forEach(item => {
        const li = document.createElement("li");
        const user = item.match(/>(.*?)</)[1];
        if (user !== username) {
          li.textContent = user;
          li.className = "userItem";
          li.onclick = () => selectedUser = user;
          userList.appendChild(li);
        }
      });
    });
}

function sendMessage() {
  const msg = document.getElementById("message").value;
  stompClient.send("/app/private-message", {}, JSON.stringify({
    sender: username,
    recipient: selectedUser,
    content: msg
  }));
}

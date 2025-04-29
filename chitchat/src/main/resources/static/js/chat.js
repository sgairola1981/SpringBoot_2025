let stompClient = null;
const username = /*[[${username}]]*/ '';  // Make sure this is correctly injected from Spring.

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        // Subscribe to incoming messages
        stompClient.subscribe('/user/queue/messages', (message) => {
            const msg = JSON.parse(message.body);
            showMessage(msg.from + ": " + msg.content);
        });

        // Subscribe to online users list
        stompClient.subscribe('/topic/online', (message) => {
            const users = JSON.parse(message.body);
            updateUserList(users);
        });

        // Send the username to the server
        stompClient.send("/app/online", {}, JSON.stringify({ username: username }));
    });
}

function sendMessage() {
    const content = document.getElementById('messageInput').value;
    const toUser = document.getElementById('toUser').value;

    if (content && toUser) {
        const chatMessage = {
            from: username,
            to: toUser,
            content: content
        };

        // Send the message to the server
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        showMessage("You: " + content);

        // Clear the input field
        document.getElementById('messageInput').value = '';
    } else {
        alert('Please enter a message and select a user.');
    }
}

function showMessage(message) {
    const chatArea = document.getElementById('chatArea');
    chatArea.innerHTML += `<div>${message}</div>`;
}

function updateUserList(users) {
    const toUser = document.getElementById('toUser');
    toUser.innerHTML = '<option value="">-- Select User --</option>';
    users.forEach(user => {
        if (user !== username) {
            const option = document.createElement('option');
            option.value = user;
            option.text = user;
            toUser.appendChild(option);
        }
    });
}

connect();

async function sendQuestion() {

    const txt = document.getElementById("question");
    const q = txt.value.trim();

    if (q === "") return;

    const btn = document.querySelector(".send-btn");
    const chat = document.getElementById("chatWindow");
    const typing = document.getElementById("typing");

    // Disable button
    btn.disabled = true;
    btn.innerHTML =
        '<span class="spinner-border spinner-border-sm"></span> Please wait';

    txt.disabled = true;

    // User Message
    chat.innerHTML += `
        <div class="message user">
            <div class="bubble">
                ${escapeHtml(q)}
            </div>
        </div>
    `;

    chat.scrollTop = chat.scrollHeight;

    typing.style.display = "block";

    try {

        const response = await fetch("/chat", {

            method: "POST",

            headers: {
                "Content-Type": "text/plain"
            },

            body: q

        });

        const answer = await response.text();

        typing.style.display = "none";

        chat.innerHTML += `
            <div class="message ai">
                <div class="bubble">
                    ${formatResponse(answer)}
                </div>
            </div>
        `;

    }
    catch(e){

        typing.style.display="none";

        chat.innerHTML+=`
            <div class="message ai">
                <div class="bubble text-danger">
                    Unable to connect to server.
                </div>
            </div>
        `;
    }
    finally{

        txt.value="";
        txt.disabled=false;

        btn.disabled=false;
        btn.innerHTML='<i class="fa-solid fa-paper-plane"></i> Ask AI';

        txt.focus();

        chat.scrollTop=chat.scrollHeight;

    }

}

function formatResponse(text){

    return escapeHtml(text).replace(/\n/g,"<br>");

}

function escapeHtml(text){

    const div=document.createElement("div");

    div.innerText=text;

    return div.innerHTML;

}
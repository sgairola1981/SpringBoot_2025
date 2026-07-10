(() => {
  const messagesEl = document.getElementById('messages');
  const input = document.getElementById('inputText');
  const btnAsk = document.getElementById('btn-ask');
  const btnNew = document.getElementById('btn-new');
  const btnClear = document.getElementById('btn-clear');
  const btnCopyLast = document.getElementById('btn-copy-last');
  const loadingIndicator = document.getElementById('loadingIndicator');
  const agentStatus = document.getElementById('agentStatus');

  let processing = false;
  const conversation = [];

  function formatElapsed(ms) {
    if (ms == null || isNaN(ms)) return '';
    if (ms < 1000) return `${Math.round(ms)} ms`;
    return `${(ms / 1000).toFixed(2)} s`;
  }

  function scrollToBottom() {
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function autoResize() {
    input.style.height = 'auto';
    input.style.height = Math.min(input.scrollHeight, 220) + 'px';
  }

  function setProcessing(val) {
    processing = val;
    btnAsk.disabled = val;
    input.disabled = val;
    loadingIndicator.style.display = val ? 'inline-block' : 'none';
    agentStatus.innerText = val ? 'Processing' : 'Ready';
  }

  function escapeHtml(unsafe) {
    return String(unsafe)
      .replaceAll('&', '&amp;')
      .replaceAll('<', '&lt;')
      .replaceAll('>', '&gt;');
  }

  function appendSystem(text) {
    const el = document.createElement('div');
    el.className = 'msg system';
    el.innerHTML = `<div class="muted">${escapeHtml(text)}</div>`;
    messagesEl.appendChild(el);
    scrollToBottom();
  }

  function appendUser(text) {
    conversation.push({ role: 'user', content: text });
    const wrap = document.createElement('div');
    wrap.className = 'msg out';
    wrap.innerHTML = `<div class="bubble">${escapeHtml(text)}</div><div class="avatar">You</div>`;
    messagesEl.appendChild(wrap);
    scrollToBottom();
  }

  function appendAssistantCard(htmlContent, meta) {
    conversation.push({ role: 'assistant', content: htmlContent });

    const wrap = document.createElement('div');
    wrap.className = 'msg in';

    const badgeClass = meta.status === 'ok' ? 'badge-success' : 'badge-warn';
    const exec = meta.execTime ? `<div class="time-meta">Execution: ${meta.execTime}</div>` : '';

    wrap.innerHTML = `
      <div class="avatar">AI</div>
      <div class="response-card">
        <div class="card-row">
          <div style="font-weight:600">Assistant</div>
          <div class="card-actions small">
            <div class="badge-status ${badgeClass}">${meta.statusText || 'Processed'}</div>
            <div class="time-meta">${meta.shortTime || ''}</div>
          </div>
        </div>
        <div class="small response-text">
          ${htmlContent}
        </div>
        <div class="card-row">
          <div class="muted small">${meta.model || ''}</div>
          <div class="small d-flex align-items-center gap-2">
            ${exec}
            <button class="btn btn-sm btn-outline-secondary btn-copy-response">Copy</button>
          </div>
        </div>
      </div>
    `;

    messagesEl.appendChild(wrap);

    const rawText = wrap.querySelector('.response-text').innerText;
    wrap.querySelector('.btn-copy-response').addEventListener('click', () => {
      navigator.clipboard.writeText(rawText).then(() => {
        appendSystem('Copied to clipboard');
        setTimeout(() => {
          const sys = messagesEl.querySelectorAll('.msg.system');
          if (sys.length) sys[sys.length - 1].remove();
        }, 1000);
      });
    });

    scrollToBottom();
  }

  async function sendMessage() {
    const text = input.value.trim();
    if (!text || processing) return;

    appendUser(text);
    input.value = '';
    autoResize();
    setProcessing(true);

    const start = performance.now();

    try {
      const historySnapshot = JSON.stringify(conversation.slice(-6));

      const payload = {
        query: text,
        sessionId: 'session-1',
        history: historySnapshot
      };

      const resp = await fetch('/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const elapsed = performance.now() - start;

      if (resp.ok) {
        const dto = await resp.json();

        let htmlContent;
        if (dto.voucherFound === false) {
          // Short view when voucher not available
          htmlContent = `
            <div class="small">
              <div style="font-weight:600;">Voucher</div>
              <div class="mt-1">
                Voucher No: <span style="font-weight:500;">${escapeHtml(dto.voucherNo || '')}</span><br/>
                Status: <span style="font-weight:500;">${escapeHtml(dto.voucherStatus || 'Voucher not available')}</span>
              </div>
            </div>
          `;
        } else {
          // Rich view when voucher exists
          htmlContent = `
            <div class="small">
              <div class="mb-2">
                <div style="font-weight:600;">Voucher</div>
                <div class="d-flex justify-content-between align-items-center small mt-1">
                  <div>Voucher No: <span style="font-weight:500;">${escapeHtml(dto.voucherNo || '')}</span></div>
                  <div>Status: <span style="font-weight:500;">${escapeHtml(dto.voucherStatus || '')}</span></div>
                </div>
              </div>

              <hr class="my-2" />

              <div class="row g-2 small">
                <div class="col-6">
                  <div style="font-weight:600;">Workflow</div>
                  <div class="badge badge-success" style="padding:.25rem .5rem;border-radius:999px;">
                    ${escapeHtml(dto.workflow || 'Data not available')}
                  </div>
                </div>
                <div class="col-6">
                  <div style="font-weight:600;">GST</div>
                  <div class="badge ${dto.gst && dto.gst.toLowerCase().includes('not') ? 'badge-warn' : 'badge-success'}"
                       style="padding:.25rem .5rem;border-radius:999px;">
                    ${escapeHtml(dto.gst || 'Data not available')}
                  </div>
                </div>
                <div class="col-6 mt-2">
                  <div style="font-weight:600;">Budget</div>
                  <div class="badge badge-success" style="padding:.25rem .5rem;border-radius:999px;">
                    ${escapeHtml(dto.budget || 'Data not available')}
                  </div>
                </div>
                <div class="col-6 mt-2">
                  <div style="font-weight:600;">Payment</div>
                  <div class="badge ${dto.payment === 'Success' || dto.payment === 'Paid' ? 'badge-success' : 'badge-warn'}"
                       style="padding:.25rem .5rem;border-radius:999px;">
                    ${escapeHtml(dto.payment || 'Data not available')}
                  </div>
                </div>
              </div>
            </div>
          `;
        }

        appendAssistantCard(htmlContent, {
          status: 'ok',
          statusText: 'Done',
          execTime: formatElapsed(dto.executionMs ?? elapsed),
          model: 'finance-v1',
          shortTime: new Date().toLocaleTimeString()
        });
      } else {
        appendAssistantCard(`<div class="small">Server error ${resp.status}</div>`, {
          status: 'err',
          statusText: 'Error',
          execTime: formatElapsed(elapsed),
          model: 'n/a',
          shortTime: new Date().toLocaleTimeString()
        });
      }
    } catch (err) {
      const elapsed = performance.now() - start;
      appendAssistantCard(`<div class="small">Network error: ${escapeHtml(err.message)}</div>`, {
        status: 'err',
        statusText: 'Network',
        execTime: formatElapsed(elapsed),
        model: 'n/a',
        shortTime: new Date().toLocaleTimeString()
      });
    } finally {
      setProcessing(false);
    }
  }

  input.addEventListener('input', autoResize);
  window.addEventListener('load', () => {
    autoResize();
    input.focus();
  });

  input.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  });

  btnAsk.addEventListener('click', sendMessage);

  btnNew.addEventListener('click', () => {
    conversation.length = 0;
    messagesEl.innerHTML = `<div class="msg system"><div class="muted">New investigation started.</div></div>`;
    input.value = '';
    autoResize();
    input.focus();
  });

  btnClear.addEventListener('click', () => {
    input.value = '';
    autoResize();
    input.focus();
  });

  btnCopyLast.addEventListener('click', () => {
    const lastResponse = messagesEl.querySelector('.response-text:last-of-type');
    if (!lastResponse) {
      appendSystem('No responses to copy');
      setTimeout(() => {
        const sys = messagesEl.querySelectorAll('.msg.system');
        if (sys.length) sys[sys.length - 1].remove();
      }, 1000);
      return;
    }
    navigator.clipboard.writeText(lastResponse.innerText).then(() => {
      appendSystem('Copied last response');
      setTimeout(() => {
        const sys = messagesEl.querySelectorAll('.msg.system');
        if (sys.length) sys[sys.length - 1].remove();
      }, 1000);
    });
  });

  setProcessing(false);
  scrollToBottom();
})();
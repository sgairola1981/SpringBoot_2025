import { useEffect, useRef, useState } from "react";

export default function VideoCall({ client, selectedUser, username, onClose }) {

  const localVideoRef = useRef();
  const remoteVideoRef = useRef();
  const pcRef = useRef(null);

  const [callStatus, setCallStatus] = useState("connecting"); // connecting | connected

  // ✅ CREATE PEER CONNECTION
  const createPeer = () => {

    const pc = new RTCPeerConnection({
      iceServers: [
        { urls: "stun:stun.l.google.com:19302" } // ✅ FREE STUN
      ]
    });

    // 🔁 ICE candidates
    pc.onicecandidate = (event) => {
      if (event.candidate) {
        client.publish({
          destination: "/app/call",
          body: JSON.stringify({
            type: "candidate",
            receiver: selectedUser,
            candidate: event.candidate
          })
        });
      }
    };

    // 📺 Remote stream
    pc.ontrack = (event) => {
      remoteVideoRef.current.srcObject = event.streams[0];
      setCallStatus("connected");
    };

    return pc;
  };

  // ✅ START MEDIA (FIXED FOR MOBILE)
  const startMedia = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: "user" }, // ✅ mobile fix
        audio: true
      });

      localVideoRef.current.srcObject = stream;

      return stream;

    } catch (err) {
      console.error("Camera error:", err);


      // 🔥 fallback → audio only
      try {
        const audioStream = await navigator.mediaDevices.getUserMedia({
          audio: true
        });

        localVideoRef.current.srcObject = audioStream;

        alert("📞 Camera not available, using audio only");

        return audioStream;

      } catch (e) {
        alert("❌ Camera & Mic blocked");
        onClose();
      }
    }
  };

  // ✅ INIT CALL
  useEffect(() => {

    const init = async () => {

      const stream = await startMedia();

      const pc = createPeer();
      pcRef.current = pc;

      // 🎤 Add tracks
      stream.getTracks().forEach(track => pc.addTrack(track, stream));

      // 📞 Create offer
      const offer = await pc.createOffer();
      await pc.setLocalDescription(offer);

      client.publish({
        destination: "/app/call",
        body: JSON.stringify({
          type: "offer",
          receiver: selectedUser,
          offer: offer
        })
      });
    };

    init();

    // 📩 SIGNALING
    client.subscribe("/user/queue/call", async (msg) => {
      const data = JSON.parse(msg.body);

      const pc = pcRef.current;

      // 🔹 OFFER
      if (data.type === "offer") {

        const stream = await startMedia();

        const newPc = createPeer();
        pcRef.current = newPc;

        stream.getTracks().forEach(track => newPc.addTrack(track, stream));

        await newPc.setRemoteDescription(data.offer);

        const answer = await newPc.createAnswer();
        await newPc.setLocalDescription(answer);

        client.publish({
          destination: "/app/call",
          body: JSON.stringify({
            type: "answer",
            receiver: data.sender,
            answer: answer
          })
        });
      }

      // 🔹 ANSWER
      if (data.type === "answer") {
        await pc.setRemoteDescription(data.answer);
      }

      // 🔹 ICE
      if (data.type === "candidate") {
        await pc.addIceCandidate(data.candidate);
      }
    });

  }, []);

  // ✅ END CALL
  const endCall = () => {

    pcRef.current?.close();

    // stop camera
    localVideoRef.current?.srcObject?.getTracks().forEach(t => t.stop());

    onClose();
  };

  return (
    <div style={styles.overlay}>

      <div style={styles.callBox}>

        <h3>📞 {selectedUser}</h3>

        <div style={styles.videoContainer}>

          <video
            ref={localVideoRef}
            autoPlay
            muted
            playsInline
            style={styles.localVideo}
          />

          <video
            ref={remoteVideoRef}
            autoPlay
            playsInline
            style={styles.remoteVideo}
          />

        </div>

        <p>{callStatus === "connecting" ? "Connecting..." : "Connected"}</p>

        <button style={styles.endBtn} onClick={endCall}>
          ❌ End Call
        </button>

      </div>
    </div>
  );
}
const styles = {
  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    background: "rgba(0,0,0,0.9)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 999
  },

  callBox: {
    textAlign: "center",
    color: "#fff"
  },

  videoContainer: {
    position: "relative"
  },

  remoteVideo: {
    width: "500px",
    height: "350px",
    background: "#000"
  },

  localVideo: {
    position: "absolute",
    width: "120px",
    height: "90px",
    bottom: "10px",
    right: "10px",
    border: "2px solid #fff"
  },

  endBtn: {
    marginTop: "15px",
    padding: "10px 20px",
    background: "red",
    border: "none",
    color: "#fff",
    borderRadius: "5px",
    cursor: "pointer"
  }
};
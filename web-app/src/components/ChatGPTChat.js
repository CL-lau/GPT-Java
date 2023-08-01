import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import "./ChatGPTChat.css";

const ChatGPTChat = () => {
    const [userInput, setUserInput] = useState("");
    const [chatMessages, setChatMessages] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const chatMessagesDiv = useRef(null);

    useEffect(() => {
        scrollToBottom();
    }, [chatMessages]);

    const handleInputChange = (event) => {
        setUserInput(event.target.value);
    };

    const handleSendMessage = async () => {
        const message = userInput.trim();
        if (message !== "") {
            const newChatMessage = {
                content: message,
                fromUser: true,
            };

            setUserInput("");
            setChatMessages((prevMessages) => [...prevMessages, newChatMessage]);

            try {
                setIsLoading(true);
                // Simulate delay to show loading state
                await new Promise((resolve) => setTimeout(resolve, 1000));

                // Replace 'YOUR_BOT_ENDPOINT' with the actual endpoint URL of your ChatGPT backend
                const response = await axios.post("YOUR_BOT_ENDPOINT", {
                    message,
                });

                const botReply = response.data.response;
                const newBotMessage = {
                    content: botReply,
                    fromUser: false,
                };
                setChatMessages((prevMessages) => [...prevMessages, newBotMessage]);
                setIsLoading(false);
            } catch (error) {
                console.error("Error sending message to bot:", error);
                setIsLoading(false);
            }
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === "Enter") {
            handleSendMessage();
        }
    };

    const scrollToBottom = () => {
        chatMessagesDiv.current.scrollTop = chatMessagesDiv.current.scrollHeight;
    };

    return (
        <div className="chat-container">
            <div className="chat-messages" ref={chatMessagesDiv}>
                {chatMessages.map((message, index) => (
                    <div
                        key={index}
                        className={message.fromUser ? "user-message" : "bot-message"}
                    >
                        {message.content}
                    </div>
                ))}
                {isLoading && <div className="bot-message loading-message">Loading...</div>}
            </div>
            <div className="input-container">
                <input
                    type="text"
                    className="input-text"
                    value={userInput}
                    onChange={handleInputChange}
                    onKeyPress={handleKeyPress}
                    placeholder="Type your message..."
                />
                <button className="input-button" onClick={handleSendMessage}>
                    Send
                </button>
            </div>
        </div>
    );
};

export default ChatGPTChat;

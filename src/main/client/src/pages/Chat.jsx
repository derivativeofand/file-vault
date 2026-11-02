import { useState } from 'react';
import NavBar from './NavBar';
import api from '../axios';
import { useParams } from 'react-router-dom';
import ChatMessage from './ChatMessage';

function Chat() {
    let { fileid } = useParams();
    return (
        <div className="min-h-screen bg-gray-100">
            <NavBar />
            <div className="px-4 py-10">
                <div className="mx-auto max-w-2xl bg-white rounded-lg shadow-md p-8 space-y-6">
                    <h1 className="text-2xl font-semibold text-gray-800">Chat about file {fileid}</h1>

                    <div className="space-y-3">
                        <ChatMessage message={{ role: 'user', text: 'What is this file about?' }} />
                        <ChatMessage message={{ role: 'bot', text: "It's a sales report." }} />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Chat;
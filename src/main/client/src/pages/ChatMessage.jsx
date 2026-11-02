
function ChatMessage({ message: {role, text}}) {

    const isUser = role === 'user';
    
    return (
        <div className={`flex ${isUser ? 'justify-end' : 'justify-start'}`}>
            <div className={`max-w-[80%] rounded-lg px-4 py-2 whitespace-pre-wrap break-words ${isUser ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-800'}`}>
                {text}
            </div>
        </div>
    )
}

export default ChatMessage;
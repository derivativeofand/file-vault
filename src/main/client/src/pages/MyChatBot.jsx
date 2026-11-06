import ChatBot from 'react-chatbotify';
import api from '../axios';

const MyChatBot = ({fileId}) => {
    
    const settings = {
        header: {
            title: "ChatBot",
            showAvatar: false,
            buttons: [],
        },

        general: {
            primaryColor: '#2563eb',
            secondaryColor: '#2563eb',
            fontFamily: 'inherit',
            embedded: true,
            showFooter: false
        },
        botBubble: {
            showAvatar: false
        },
        chatHistory: {
            disabled: true
        }
    };

    const styles = {
        chatWindowStyle: { width: '100%', height: '100%', borderRadius: 0, boxShadow: 'none' },
        headerStyle: { padding: '8px 16px', fontSize: '0.95rem', background: '#ffffff', color: '#1f2937', borderBottom: '1px solid #e5e7eb' },
        bodyStyle: { backgroundColor: '#f3f4f6' },
        userBubbleStyle: { fontSize: '0.9rem', padding: '8px 12px' },
        botBubbleStyle: { fontSize: '0.9rem', padding: '8px 12px', backgroundColor: '#ffffff', color: '#1f2937' },
        chatInputContainerStyle: { padding: '8px 16px', borderTop: '1px solid #e5e7eb' },
    };
    
    const flow = {
        start: {
            message: "Welcome to File Vault! Ask me anything about your documents",
            path: "loop"
        },
        loop: {
            message: async (params) => {
                try {
                    const response = await api.post(`/chat/${fileId}`, params.userInput, { headers: {  'Content-Type': 'text/plain', 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
                    return response.data ;
                } catch (error) {
                    return "Error connecting to backend";
                }
            },
            path: "loop" 
        }
    };
    return <ChatBot settings={settings} styles={styles} flow={flow} />;
}

export default MyChatBot;
import NavBar from './NavBar';
import { useParams } from 'react-router-dom';
import MyChatBot from './MyChatBot';

function Chat() {
    let { fileId } = useParams();
    return (
        <div className="flex h-dvh flex-col bg-gray-100">
            <NavBar />
            {/* the chatbot wraps its window in two divs; give both full height so it fills the space under the navbar */}
            <div className="min-h-0 flex-1 [&>div]:h-full [&>div>div]:h-full">
                <MyChatBot fileId={fileId}/>
            </div>
        </div>
    )
}

export default Chat;
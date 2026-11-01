import { useContext } from 'react'
import  { UserContext } from './UserContext'
import { useNavigate } from 'react-router-dom'

function NavBar() {
    const navigate = useNavigate();
    const { user, setUser } = useContext(UserContext);
    function handleLogout() {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            setUser('');
            navigate('/login');
    }
    return (
        <nav className="flex items-center justify-between gap-4 bg-white px-6 py-4 shadow-md">
            <span className="shrink-0 text-lg font-semibold text-gray-800">File Vault</span>

            <div className="flex min-w-0 items-center gap-4">
                <span className="truncate text-sm text-gray-700">Hello {user}</span>
                <button type="button" onClick={handleLogout} className="shrink-0 text-sm text-blue-600 hover:underline cursor-pointer">Logout</button>
            </div>
        </nav>
    )
}

export default NavBar
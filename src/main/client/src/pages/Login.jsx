import { useState,useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import  { UserContext } from './UserContext'
import api from '../axios'

function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { setUser } = useContext(UserContext);

    // On form submission, send a POST request to the backend with the username and password.
    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
        <form className="w-full max-w-sm bg-white rounded-lg shadow-md p-8 space-y-5" onSubmit={async (e) => {
            // Prevent the default form submission behavior.
            e.preventDefault();
            try {
                const response = await api.post('/auth/login', { username, password });
                if(response.status === 200) {
                    setUser(username);
                    const token = response.data;
                    localStorage.setItem('token', token);
                    localStorage.setItem('user', username);
                    // Login successful, redirect to dashboard
                    console.log('Login successful, redirecting to dashboard...');
                    navigate('/dashboard');
                }

            } catch (error) {
                console.error('Login failed:', error);
            }
        }}>
            <h1 className="text-2xl font-semibold text-gray-800 text-center">Log in</h1>

            <div className="space-y-1">
                <label htmlFor="username" className="block text-sm font-medium text-gray-700">Username</label>
                <input type="text" id="username" value={username} onChange={(e) => setUsername(e.target.value)}
                    className="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"/>
            </div>
            <div className="space-y-1">
                <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)}
                    className="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"/>
            </div>

            <button type="submit" className="w-full rounded-md bg-blue-600 py-2 font-medium text-white hover:bg-blue-700 cursor-pointer">Login</button>
            <button type="button" onClick={() => navigate('/register')} className="w-full text-sm text-blue-600 hover:underline cursor-pointer">Register</button>
        </form>
        </div>
)
}

export default Login
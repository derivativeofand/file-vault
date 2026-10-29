import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../axios'

function Register() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');   


    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
        <form className="w-full max-w-sm bg-white rounded-lg shadow-md p-8 space-y-5" onSubmit={async (e) => {
            e.preventDefault();
            try {
                const response = await api.post('/users/register', { email, username, password });
                console.log(response.status);
                if(response.status === 200) {
                    // Registration successful, redirect to login page
                    console.log('Registration successful, redirecting to login page...');
                    navigate('/login');
                }
            } catch (error) {
                console.error('Registration failed:', error);
            }
        }}>
            <h1 className="text-2xl font-semibold text-gray-800 text-center">Create account</h1>

            <div className="space-y-1">
                <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email</label>
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)}
                    className="w-full rounded-md border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"/>
            </div>
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

            <button type="submit" className="w-full rounded-md bg-blue-600 py-2 font-medium text-white hover:bg-blue-700 cursor-pointer">Register</button>
            <button type="button" onClick={() => navigate('/login')} className="w-full text-sm text-blue-600 hover:underline cursor-pointer">Back to Login</button>
        </form>
        </div>
    )
}

export default Register
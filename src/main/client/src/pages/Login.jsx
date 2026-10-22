import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import api from '../axios'

function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    // On form submission, send a POST request to the backend with the username and password.
    return (<form onSubmit={async (e) => {
            // Prevent the default form submission behavior.
            e.preventDefault();
            try {
                const response = await api.post('/auth/login', { username, password });
                if(response.status === 200) {
                    const token = response.data;
                    localStorage.setItem('token', token);
                    // Login successful, redirect to dashboard
                    console.log('Login successful, redirecting to dashboard...');
                    navigate('/dashboard');
                }

            } catch (error) {
                console.error('Login failed:', error);
            }
        }}>
            <label htmlFor="username">Username</label><br></br>
            <input type="text" id="username" value={username} onChange={(e) => setUsername(e.target.value)}/><br></br>
            <label htmlFor="password">Password</label><br></br>
            <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)}/><br></br>

            <button type="button" onClick={() => navigate('/register')}>Register</button> 
            <button type="submit">Login</button>
        </form>
)
}

export default Login
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import api from '../axios'

function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    console.log('Username:', username);
    console.log('Password:', password);
    // On form submission, send a POST request to the backend with the username and password.
    return (<form onSubmit={async (e) => {
            // Prevent the default form submission behavior.
            e.preventDefault();
            try {
                const response = await api.post('/auth/login', { username, password });
                console.log(response.data);
            } catch (error) {
                console.error('Login failed:', error);
            }
        }}>
            <label htmlFor="username">Username</label><br></br>
            <input type="text" id="username" value={username} onChange={(e) => setUsername(e.target.value)}/><br></br>
            <label htmlFor="password">Password</label><br></br>
            <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)}/><br></br>

            <button type="submit">Login</button>
            <button type="button" onClick={() => navigate('/register')}>Register</button> 
        </form>
)
}

export default Login
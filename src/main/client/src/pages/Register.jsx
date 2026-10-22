import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../axios'

function Register() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');   


    return (<form onSubmit={async (e) => {
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
            <label htmlFor="email">Email</label><br></br>
            <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)}/><br></br>
            <label htmlFor="username">Username</label><br></br>
            <input type="text" id="username" value={username} onChange={(e) => setUsername(e.target.value)}/><br></br>
            <label htmlFor="password">Password</label><br></br>
            <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)}/><br></br>

            <button type="button" onClick={() => navigate('/login')}>Back to Login</button>
            <button type="submit">Register</button>
        </form>
    )
}

export default Register
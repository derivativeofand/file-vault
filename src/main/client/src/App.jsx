import { useState } from 'react';
import { UserContext } from './pages/UserContext';
import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Chat from './pages/Chat'

function App() {
  const [user, setUser] = useState(localStorage.getItem('user'));
  return (
    <UserContext.Provider value={{user, setUser}}>
      <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/chat/:fileId" element = {<Chat />} />
          <Route path="/" element={<Login />} /> 
      </Routes>
    </UserContext.Provider>
  )
}

export default App

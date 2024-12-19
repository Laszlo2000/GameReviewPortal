import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login.tsx';
import Home from './components/Home.tsx';
import Register from './components/Register.tsx';

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                {/* Gyökérútvonal */}
                <Route path="/" element={<Home />} />

                {/* Bejelentkezési/Regisztrációs oldal */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* További funkciók */}
                <Route path="/home" element={<Home />} />
            </Routes>
        </Router>
    );
};

export default App;
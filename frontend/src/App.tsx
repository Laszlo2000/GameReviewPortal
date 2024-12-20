import React from 'react';
import {BrowserRouter as Router, Routes, Route, useLocation} from 'react-router-dom';
import Login from './components/Login.tsx';
import Home from './components/Home.tsx';
import Register from './components/Register.tsx';
import Navbar from "./components/Navbar.tsx";
import PrivateRoute from "./components/PrivateRoute";
import Admin from "./components/Admin.tsx";
import Users from "./components/Users.tsx";

const App: React.FC = () => {
    return (
        <Router>
            <NavBarWithConditionalRender/>
            <Routes>
                {/* Gyökérútvonal */}
                <Route path="/" element={<Home/>}/>

                {/* Bejelentkezési/Regisztrációs oldal */}
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>

                {/* Védett útvonalak */}
                <Route path="/home" element={
                    <PrivateRoute>
                        <Home/>
                    </PrivateRoute>
                }/>
                <Route
                    path="/admin"
                    element={
                        <PrivateRoute>
                            <Admin/>
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/admin/users"
                    element={
                        <PrivateRoute>
                            <Users/>
                        </PrivateRoute>
                    }
                />
            </Routes>
        </Router>
    );
};

const NavBarWithConditionalRender: React.FC = () => {
    const location = useLocation();
    // Feltétel: Ne jelenjen meg a Navbar a /login és /register útvonalakon
    if (location.pathname === "/login" || location.pathname === "/register") {
        return null;
    }
    return (
        <div className="p-10">
            <Navbar/>
        </div>
    );
};

export default App;
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const Navbar: React.FC = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const checkAuthStatus = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setIsLoggedIn(false);
                return;
            }

            setIsLoggedIn(true);

            try {
                const response = await fetch("http://localhost:8080/admin/check-role", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const role = await response.text();
                    if (role.trim() === "admin") {
                        setIsAdmin(true);
                    }
                }
            } catch (error) {
                console.error("Error checking admin status:", error);
            }
        };

        checkAuthStatus();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        setIsAdmin(false);
        navigate("/login");
    };

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const closeDropdown = () => {
        setIsDropdownOpen(false);
    };

    return (
        <nav className="fixed top-0 left-0 w-full text-white z-50 flex items-center justify-between px-12 py-6 bg-gray-900 shadow-[0px_0px_10px_5px_rgba(0,0,0,0.4)]">
            <div className="text-lg font-bold">GameReviewPortal</div>
            <div className="flex items-center space-x-6">
                <Link
                    to="/guest"
                    className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                >
                    Guest
                </Link>
                <Link
                    to={isLoggedIn ? "/home" : "/"}
                    className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                >
                    Home
                </Link>
                {isLoggedIn && isAdmin && (
                    <div className="relative">
                        <button
                            onClick={toggleDropdown}
                            className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                        >
                            Admin
                        </button>
                        {isDropdownOpen && (
                            <div className="absolute right-0 mt-2 w-48 bg-gray-800 rounded shadow-lg">
                                <Link
                                    to="/admin"
                                    onClick={closeDropdown}
                                    className="block px-4 py-2 text-white hover:bg-gray-700 transition duration-300"
                                >
                                    AdminPage
                                </Link>
                                <Link
                                    to="/users"
                                    onClick={closeDropdown}
                                    className="block px-4 py-2 text-white hover:bg-gray-700 transition duration-300"
                                >
                                    Users
                                </Link>
                                <Link
                                    to="/games"
                                    onClick={closeDropdown}
                                    className="block px-4 py-2 text-white hover:bg-gray-700 transition duration-300"
                                >
                                    Games
                                </Link>
                            </div>
                        )}
                    </div>
                )}
                {isLoggedIn ? (
                    <button
                        onClick={handleLogout}
                        className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                    >
                        Logout
                    </button>
                ) : (
                    <Link
                        to="/login"
                        className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                    >
                        Login
                    </Link>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
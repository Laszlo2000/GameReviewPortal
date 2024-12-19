import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Navbar: React.FC = () => {
    const [isAdmin, setIsAdmin] = useState(false);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const checkAdminStatus = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                console.error("No token found");
                setLoading(false);
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/admin/check-role", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const role = await response.text(); // "admin" vagy "user"
                    setIsAdmin(role.trim() === "admin");
                } else {
                    console.error("Failed to verify admin status:", response.status);
                }
            } catch (error) {
                console.error("Error checking admin status:", error);
            } finally {
                setLoading(false);
            }
        };

        checkAdminStatus();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    if (loading) {
        return null; // Várjuk, amíg a státuszt lekérjük
    }

    return (
        <nav className="fixed top-0 left-0 w-full text-white z-50 flex items-center justify-between px-12 py-6 bg-gray-900 shadow-[0px_0px_10px_5px_rgba(0,0,0,0.4)]">
            <div className="text-lg font-bold">GameReviewPortal</div>
            <div className="flex items-center space-x-6 ml-auto"> {/* `ml-auto` a jobb oldalra igazításhoz */}
                <Link
                    to="/home"
                    className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                >
                    Home
                </Link>
                {isAdmin && (
                    <Link
                        to="/admin"
                        className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                    >
                        Admin
                    </Link>
                )}
                <button
                    onClick={handleLogout}
                    className="hover:bg-gray-700 px-4 py-2 rounded transition duration-300 hover:text-white"
                >
                    Logout
                </button>
            </div>
        </nav>
    );

};

export default Navbar;

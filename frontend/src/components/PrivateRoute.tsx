import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import Admin from "./Admin.tsx";

const PrivateRoute: React.FC<{ children: JSX.Element }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setLoading(false);
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/admin/check-role", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`, // Bearer előtag biztosítása
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const role = await response.text(); // "admin" vagy "user"
                    setIsAuthenticated(true);
                    setIsAdmin(role.trim() === "admin");
                } else {
                    setIsAuthenticated(false);
                }
            } catch (error) {
                console.error("Error checking role:", error);
                setIsAuthenticated(false);
            } finally {
                setLoading(false);
            }
        };

        checkAuth();
    }, []);


    if (loading) {
        return <div className="text-center text-white">Loading...</div>;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" />;
    }

    if (children.type === Admin && !isAdmin) {
        return <Navigate to="/home" />;
    }

    return children;
};

export default PrivateRoute;

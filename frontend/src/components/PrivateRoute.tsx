import React, { useEffect, useState } from "react";
import { Navigate, useLocation } from "react-router-dom";

const PrivateRoute: React.FC<{ children: JSX.Element }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [loading, setLoading] = useState(true);
    const location = useLocation(); // Az aktuális útvonal lekérése

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
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const role = await response.text();
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

    // Loading állapotban ne rendereljen semmit, amíg az authentikáció zajlik
    if (loading) {
        return <div className="text-center text-white">Loading...</div>;
    }

    // Ha nincs bejelentkezve, irányítsa a /login oldalra
    if (!isAuthenticated) {
        return <Navigate to="/login" />;
    }

    // Ellenőrizzük, hogy admin útvonalon vagyunk-e, ha igen és a felhasználó nem admin, átirányítjuk a /home oldalra
    const isAdminRoute = location.pathname.startsWith("/admin");
    if (isAdminRoute && !isAdmin) {
        return <Navigate to="/home" />;
    }

    // Alapértelmezett: ha minden rendben, renderelje az adott oldalt
    return children;
};

export default PrivateRoute;

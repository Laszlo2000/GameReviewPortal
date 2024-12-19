import React, { useEffect, useState } from "react";

// JWT dekódoló függvény
const decodeToken = (token: string): { sub: string } | null => {
    try {
        const base64Payload = token.split(".")[1]; // JWT payload
        const payload = atob(base64Payload); // Base64 dekódolás
        return JSON.parse(payload);
    } catch (error) {
        console.error("Invalid token:", error);
        return null;
    }
};

const Admin: React.FC = () => {
    const [username, setUsername] = useState<string | null>(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            const decoded = decodeToken(token); // Token dekódolása
            if (decoded) {
                setUsername(decoded.sub); // A "sub" mező tartalmazza a felhasználónevet
            }
        }
    }, []);

    return (
        <div className="flex justify-center items-center h-screen bg-gray-700">
            <h1 className="text-4xl font-bold text-black">
                🎉 Congratulations {username || "Admin"} you are an Admin! 🎉
            </h1>
        </div>
    );
};

export default Admin;

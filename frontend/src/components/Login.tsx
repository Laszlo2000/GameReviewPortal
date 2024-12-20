import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Login: React.FC = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate(); // Navigációs hook

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            console.log("Response status:", response.status);

            if (response.ok) {
                const token = await response.text();
                console.log("Received token:", token);

                if (token) {
                    localStorage.setItem("token", token);
                    navigate("/home"); // Átirányítás a Home oldalra
                } else {
                    setError("No token received from the server.");
                }
            } else {
                console.error("Login failed with status:", response.status);
                setError("Login failed. Please check your credentials.");
            }
        } catch (err) {
            console.error("Error during login request:", err);
            setError("An error occurred. Please try again.");
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-[#f7EEC9]">
            <form
                onSubmit={handleLogin}
                className="bg-[#EE4E4E] p-6 rounded-lg shadow-md w-full max-w-sm"
            >
                <h2 className="text-2xl font-bold mb-4 text-center">Login</h2>
                {error && (
                    <div className="mb-4 text-[#EDD609] 0 font-bold text-sm text-center">{error}</div>
                )}
                <div className="mb-4">
                    <label htmlFor="username" className="block text-sm font-medium text-black">
                        Username:
                    </label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        className="mt-1 block w-full px-3 py-2 bg-[#f7EEC9] border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="password" className="block text-sm font-medium text-black">
                        Password:
                    </label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="mt-1 block w-full px-3 py-2 bg-[#f7EEC9] border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300"
                >
                    Login
                </button>
                <div className="mt-4 text-center">
                    <span className="text-sm text-black">
                        Don't have an account?{" "}
                        <Link to="/register" className="text-blue-600 hover:underline">
                            <br />
                            Register here
                        </Link>
                        <Link to="/" className="text-blue-600 hover:underline">
                            <br />
                            Home
                        </Link>
                    </span>
                </div>
            </form>
        </div>
    );
};

export default Login;

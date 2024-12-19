import React, { useState } from "react";
import {Link, useNavigate} from "react-router-dom";

const Register: React.FC = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState<string | null>(null);
    const navigate = useNavigate(); // Hook a navigációhoz

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();

        // Jelszó egyezésének ellenőrzése
        if (password !== confirmPassword) {
            setMessage("Passwords do not match!");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, email, password }),
            });

            if (response.ok) {
                // Sikeres regisztráció esetén átirányítás
                navigate("/login");
            } else {
                const errorData = await response.json();
                setMessage(errorData.message || "Registration failed.");
            }
        } catch (err) {
            console.error("Error during registration:", err);
            setMessage("An error occurred. Please try again.");
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-gray-700">
            <form
                onSubmit={handleRegister}
                className="bg-gray-500 0 p-6 rounded-lg shadow-md w-full max-w-sm"
            >
                <h2 className="text-2xl font-bold mb-4 text-center">Register</h2>
                {message && (
                    <div className="mb-4 font-bold text-[#EDD609] text-sm text-center">{message}</div>
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
                        className="mt-1 block w-full px-3 py-2 bg-gray-300 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="email" className="block text-sm font-medium text-black">
                        Email:
                    </label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="mt-1 block w-full px-3 py-2 bg-gray-300 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
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
                        className="mt-1 block w-full px-3 py-2 bg-gray-300 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="confirmPassword" className="block text-sm font-medium text-black">
                        Confirm Password:
                    </label>
                    <input
                        type="password"
                        id="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                        className="mt-1 block w-full px-3 py-2 bg-gray-300 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300"
                >
                    Register
                </button>
                <div className="mt-4 text-center">
                    <span className="text-sm text-black">
                        Do you have an account?{" "}
                        <Link to="/login" className="text-blue-600 hover:underline">
                            <br/>
                            Login here
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

export default Register;

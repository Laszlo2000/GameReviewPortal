import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Game {
    id: number;
    title: string;
    developerId: number;
    releaseDate: string;
    coverImageUrl: string;
    description: string;
    price: number;
    averageRating: number;
    createdAt: string;
    updatedAt: string;
}

const GameAdmin: React.FC = () => {
    const [games, setGames] = useState<Game[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    // Fetch games
    useEffect(() => {
        const fetchGames = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setError("Unauthorized: Please log in.");
                navigate("/login");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/games", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data: Game[] = await response.json();
                    setGames(data);
                } else if (response.status === 401) {
                    setError("Unauthorized: Access denied.");
                    navigate("/login");
                } else {
                    setError("Failed to fetch games. Please try again.");
                }
            } catch (err) {
                console.error("Error fetching games:", err);
                setError("An unexpected error occurred.");
            } finally {
                setLoading(false);
            }
        };

        fetchGames();
    }, [navigate]);

    if (error) {
        return <p className="text-center text-lg text-red-500">{error}</p>;
    }

    if (loading) {
        return <p className="text-center text-lg text-gray-600">Loading...</p>;
    }

    // TODO: Kesobb a fejleszto studiok neveit a backend adja vissza
    const developerNames: { [key: number]: string } = {
        1: "Mojang",
        2: "Rockstar Games",
        3: "Ubisoft",
        4: "CD Projekt Red",
        5: "Bethesda Game Studios",
        6: "Valve Corporation",
    };

    return (
        <div className="bg-[#F6EEC9] min-h-screen flex flex-col items-center p-6 text-white">
            <h1 className="text-2xl font-bold text-black mb-4">Admin: Games Management</h1>
            <div className="overflow-x-auto">
                <table className="table-auto w-full border-collapse border border-black bg-[#7caa56] text-black">
                    <thead>
                    <tr className="bg-[#EE4E4E] h-12">
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">#</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Cover Image</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Title</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Developer ID</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Release Date</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Description</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Price</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Average Rating</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Created At</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Updated At</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Edit Games</th>
                    </tr>
                    </thead>
                    <tbody>
                    {games.map((game, index) => (
                        <tr key={index} className="hover:bg-[#A1DD70] hover:text-black font-bold h-12">
                            <td className="border px-2 py-2 text-center w-16">{index + 1}</td>
                            <td className="border px-2 py-2 text-center w-28">
                                <img src={game.coverImageUrl} alt={game.title} className="w-36 h-auto mx-auto"/>
                            </td>
                            <td className="border px-2 py-2 w-52">{game.title}</td>
                            <td className="border px-2 py-2 text-center w-1">
                                {developerNames[game.developerId] || "Unknown Developer"}
                            </td>
                            <td className="border px-2 py-2 text-center w-32">{game.releaseDate}</td>
                            {/*<td className="border px-2 py-2 w-64 truncate">{game.description}</td>*/}
                            {/*<td className="border px-2 py-2">*/}
                            {/*    <div className="max-h-32 overflow-y-auto p-2"*/}
                            {/*         style={{scrollbarWidth: "thin", scrollbarColor: "#888 #ccc"}}>*/}
                            {/*        {game.description}*/}
                            {/*    </div>*/}
                            {/*</td>*/}
                            <td className="border px-2 py-2">
                                <div
                                    className="max-h-40 overflow-y-auto p-4"
                                    style={{
                                        scrollbarWidth: "thin",
                                        scrollbarColor: "#888 #ccc",
                                    }}
                                >
                                    {game.description}
                                </div>
                            </td>
                            <td className="border px-2 py-2 text-center w-24">${game.price.toFixed(2)}</td>
                            <td className="border px-2 py-2 text-center w-20">{game.averageRating.toFixed(2)}</td>
                            <td className="border px-2 py-2 text-center w-36">
                                {new Date(game.createdAt).toLocaleString()}
                            </td>
                            <td className="border px-2 py-2 text-center w-36">
                                {new Date(game.updatedAt).toLocaleString()}
                            </td>
                            <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                <div className="flex justify-center space-x-2">
                                    <button
                                        className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600 transition duration-300">
                                        Edit
                                    </button>
                                    <button
                                        className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition duration-300">
                                        Delete
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GameAdmin;

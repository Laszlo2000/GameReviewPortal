import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Game {
    developerName: string;
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
    const [newGameData, setNewGameData] = useState<Partial<Game>>({
        title: "",
        developerId: undefined,
        releaseDate: "",
        coverImageUrl: "",
        description: "",
        price: undefined,
    });
    const [editGameId, setEditGameId] = useState<number | null>(null);
    const [editGameData, setEditGameData] = useState<Partial<Game>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    // Fetch games
    useEffect(() => {
        fetchGames();
    }, [navigate]);

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

    const handleAddGame = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/games", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newGameData),
            });

            if (response.ok) {
                alert("Game added successfully!");
                fetchGames(); // Játékok újratöltése
                setNewGameData({
                    title: "",
                    developerId: undefined,
                    releaseDate: "",
                    coverImageUrl: "",
                    description: "",
                    price: undefined,
                });
            } else {
                alert("Failed to add game. Please add game details!");
            }
        } catch (err) {
            console.error("Error adding game:", err);
            alert("An unexpected error occurred.");
        }
    };

    const handleInputChange = (field: keyof Game, value: string | number) => {
        setEditGameData((prev) => ({ ...prev, [field]: value }));
    };

    const handleEdit = (game: Game) => {
        setEditGameId(game.id);
        setEditGameData({
            title: game.title,
            developerId: game.developerId,
            releaseDate: game.releaseDate,
            coverImageUrl: game.coverImageUrl,
            price: game.price,
        });
    };

    const handleSave = async () => {
        if (editGameId === null) return;
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/games/${editGameId}`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(editGameData),
            });

            if (response.ok) {
                alert("Game updated successfully!");
                // Újratöltjük a játékokat
                fetchGames();
                setEditGameId(null);
                setEditGameData({});
            } else {
                alert("Failed to update the game. Please try again.");
            }
        } catch (err) {
            console.error("Error updating game:", err);
            alert("An unexpected error occurred.");
        }
    };

    const handleCancel = () => {
        setEditGameId(null);
        setEditGameData({});
    };

    const handleDelete = async (gameId: number) => {
        const confirmed = window.confirm("Are you sure you want to delete this game?");
        if (!confirmed) return;

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/games/${gameId}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                alert("Game deleted successfully!");
                setGames((prevGames) => prevGames.filter((game) => game.id !== gameId));
            } else {
                alert("Failed to delete the game. Please try again.");
            }
        } catch (err) {
            console.error("Error deleting game:", err);
            alert("An unexpected error occurred.");
        }
    };

    const handleNewGameInputChange = (field: keyof Game, value: string | number | undefined) => {
        setNewGameData((prev) => ({ ...prev, [field]: value }));
    };

    if (error) {
        return <p className="text-center text-lg text-red-500">{error}</p>;
    }

    if (loading) {
        return <p className="text-center text-lg text-gray-600">Loading...</p>;
    }

    const developerNames: { [key: number]: string } = {
        1: "Mojang",
        2: "Rockstar Games",
        3: "Ubisoft",
        4: "CD Projekt Red",
        5: "Bethesda Game Studios",
        6: "Valve Corporation",
        7: "Electronic Arts",
    };

    return (
        <div className="bg-[#F6EEC9] min-h-screen flex flex-col items-center p-6 text-white">
            <h1 className="text-2xl font-bold text-black mb-4">Games Management</h1>

            {/* Új játék hozzáadása */}
            <div className="w-full max-w-4xl mb-6 p-4 bg-[#EE4E4E] text-black rounded shadow">
                <h2 className="text-xl font-bold mb-4 text-center">Add New Game</h2>
                <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                    <input
                        className="p-2 border rounded bg-[#F6EEC9]"
                        placeholder="Title"
                        value={newGameData.title || ""}
                        onChange={(e) => handleNewGameInputChange("title", e.target.value)}
                    />
                    <input
                        className="p-2 border rounded bg-[#F6EEC9]"
                        type="number"
                        placeholder="Developer ID"
                        value={newGameData.developerId || ""}
                        onChange={(e) =>
                            handleNewGameInputChange("developerId", Number(e.target.value))
                        }
                    />
                    <input
                        className="p-2 border rounded bg-[#F6EEC9]"
                        type="date"
                        placeholder="Release Date"
                        value={newGameData.releaseDate || ""}
                        onChange={(e) => handleNewGameInputChange("releaseDate", e.target.value)}
                    />
                    <input
                        className="p-2 border rounded bg-[#F6EEC9]"
                        placeholder="Enter image name (e.g., xyz.jpg)"
                        value={(newGameData.coverImageUrl || "").replace("/game_pictures/", "")}
                        onChange={(e) =>
                            handleNewGameInputChange("coverImageUrl", `/game_pictures/${e.target.value}`)
                        }
                    />
                </div>
                <div className="flex flex-col items-center mt-4 gap-4 w-full">
                    <input
                        className="p-2 border rounded bg-[#F6EEC9] w-1/2"
                        type="number"
                        placeholder="Price"
                        value={newGameData.price || ""}
                        onChange={(e) => handleNewGameInputChange("price", Number(e.target.value))}
                    />
                    {/* Description mező teljes szélességgel */}
                    <textarea
                        className="p-2 border rounded bg-[#F6EEC9] w-full max-w-4xl"
                        placeholder="Description"
                        value={newGameData.description || ""}
                        onChange={(e) => handleNewGameInputChange("description", e.target.value)}
                    />
                </div>
                <div className="flex justify-center mt-4">
                    <button
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                        onClick={handleAddGame}
                    >
                        Add Game
                    </button>
                </div>
            </div>

            <div className="overflow-x-auto">
                <table className="table-auto w-full border-collapse border border-black bg-[#7caa56] text-black">
                    <thead>
                    <tr className="bg-[#EE4E4E] h-12">
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">#</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Cover Image</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Cover Image URL
                        </th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Title</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Developer Studio</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Developer Studio ID</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Release Date</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Price</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Average Rating</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Created At</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Updated At</th>
                        <th className="border border-[#F6EEC9] px-4 py-2 text-black">Edit Games</th>
                    </tr>
                    </thead>
                    <tbody>
                    {games.map((game, index) => (
                        <tr key={game.id} className="hover:bg-[#A1DD70] hover:text-black font-bold">
                            <td className="border px-4 py-2 text-center">{index + 1}</td>
                            <td className="border px-4 py-2 text-center">
                                <img src={game.coverImageUrl} alt={game.title} className="w-28 h-auto mx-auto"/>
                            </td>
                            {editGameId === game.id ? (
                                <>
                                    <td className="border px-4 py-2">
                                        <input
                                            className="bg-[#F6EEC9] text-black px-2 py-1 rounded w-full"
                                            value={editGameData.coverImageUrl || ""}
                                            onChange={(e) => handleInputChange("coverImageUrl", e.target.value)}
                                        />
                                    </td>
                                    <td className="border px-4 py-2">
                                        <input
                                            className="bg-[#F6EEC9] text-black px-2 py-1 rounded w-full"
                                            value={editGameData.title || ""}
                                            onChange={(e) => handleInputChange("title", e.target.value)}
                                        />
                                    </td>
                                    <td className="border px-2 py-2 text-center">
                                        {game.developerName || "Unknown Developer"}
                                    </td>
                                    <td className="border px-4 py-2">
                                    <input
                                            type="number"
                                            className="bg-[#F6EEC9] text-black px-2 py-1 rounded w-full"
                                            value={editGameData.developerId || game.developerId}
                                            onChange={(e) =>
                                                handleInputChange("developerId", Number(e.target.value))
                                            }
                                        />
                                    </td>
                                    <td className="border px-4 py-2">
                                        <input
                                            type="date"
                                            className="bg-[#F6EEC9] text-black px-2 py-1 rounded w-full"
                                            value={editGameData.releaseDate || ""}
                                            onChange={(e) => handleInputChange("releaseDate", e.target.value)}
                                        />
                                    </td>
                                    <td className="border px-4 py-2">
                                        <input
                                            type="number"
                                            className="bg-[#F6EEC9] text-black px-2 py-1 rounded w-full"
                                            value={editGameData.price || game.price}
                                            onChange={(e) => handleInputChange("price", Number(e.target.value))}
                                        />
                                    </td>
                                    <td className="border px-4 py-2 text-center">
                                        {game.averageRating > 0 ? (
                                            <>
                                                {game.averageRating} ⭐
                                            </>
                                        ) : (
                                            ""
                                        )}
                                    </td>
                                    <td className="border px-4 py-2 text-center">{game.createdAt}</td>
                                    <td className="border px-4 py-2 text-center">{game.updatedAt}</td>
                                    <td className="border px-4 py-2 text-center">
                                        <div className="flex justify-center space-x-2">
                                            <button
                                                onClick={handleSave}
                                                className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                                            >
                                                Save
                                            </button>
                                            <button
                                                onClick={handleCancel}
                                                className="bg-gray-500 text-white px-2 py-1 rounded hover:bg-gray-600"
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </td>
                                </>
                            ) : (
                                <>
                                    <td className="border px-2 py-2 text-center w-auto">{game.coverImageUrl}</td>
                                    <td className="border px-2 py-2 w-52">{game.title}</td>
                                    <td className="border px-2 py-2 text-center">
                                        {developerNames[game.developerId] || "Unknown Developer"}
                                    </td>
                                    <td className="border px-2 py-2 text-center w-auto">{game.developerId}</td>
                                    <td className="border px-4 py-2 text-center">{game.releaseDate}</td>
                                    <td className="border px-4 py-2 text-center">${game.price.toFixed(2)}</td>
                                    <td className="border px-4 py-2 text-center">
                                        {game.averageRating > 0 ? (
                                            <>
                                                {game.averageRating} ⭐
                                            </>
                                        ) : (
                                            ""
                                        )}
                                    </td>
                                    <td className="border px-4 py-2 text-center">{game.createdAt}</td>
                                    <td className="border px-4 py-2 text-center">{game.updatedAt}</td>
                                    <td className="border px-4 py-2 text-center">
                                        <div className="flex justify-center space-x-2">
                                            <button
                                                onClick={() => handleEdit(game)}
                                                className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600"
                                            >
                                                Edit
                                            </button>
                                            <button
                                                onClick={() => handleDelete(game.id)}
                                                className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </td>
                                </>
                            )}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GameAdmin;

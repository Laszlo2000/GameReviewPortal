import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface Role {
    id: number;
    role: string;
    authority: string;
}

interface User {
    id: number;
    username: string;
    email: string;
    firstName: string | null;
    lastName: string | null;
    dateOfBirth: string | null;
    country: string | null;
    registrationDate: string;
    role: Role;
}

interface CurrentUser {
    userId: number;
    username: string;
    roleId: number;
}

const Users: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [editUserId, setEditUserId] = useState<number | null>(null);
    const [editUserData, setEditUserData] = useState<Partial<User>>({});
    const navigate = useNavigate();

    // Fetch current user
    useEffect(() => {
        const fetchCurrentUser = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setError("Unauthorized: Please log in.");
                navigate("/login");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/admin/current-user", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data: CurrentUser = await response.json();
                    setCurrentUser(data);
                } else {
                    setError("Failed to fetch current user.");
                }
            } catch (err) {
                console.error("Error fetching current user:", err);
                setError("An unexpected error occurred.");
            }
        };

        fetchCurrentUser();
    }, [navigate]);

    // Fetch users
    useEffect(() => {
        const fetchUsers = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setError("Unauthorized: Please log in.");
                navigate("/login");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/admin/users", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data: User[] = await response.json();
                    setUsers(data);
                } else {
                    setError("Failed to fetch users. Please try again.");
                }
            } catch (err) {
                console.error("Error fetching users:", err);
                setError("An unexpected error occurred.");
            }
        };

        fetchUsers();
    }, [navigate]);

    const handleEdit = (user: User) => {
        setEditUserId(user.id);
        setEditUserData({
            username: user.username || "",
            email: user.email || "",
            firstName: user.firstName || "",
            lastName: user.lastName || "",
            dateOfBirth: user.dateOfBirth || "",
            country: user.country || "",
            role: user.role,
        });
    };

    const handleInputChange = (field: keyof User, value: string) => {
        setEditUserData((prev) => ({ ...prev, [field]: value }));
    };

    const handleSave = async () => {
        if (editUserId === null) return;
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/admin/users/${editUserId}`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    ...editUserData,
                    role: editUserData.role || users.find((user) => user.id === editUserId)?.role,
                }),
            });

            if (response.ok) {
                alert("User updated successfully!");
                setUsers((prevUsers) =>
                    prevUsers.map((user) =>
                        user.id === editUserId ? { ...user, ...editUserData } : user
                    )
                );
                setEditUserId(null);
            } else {
                alert("Failed to update the user. Please try again.");
            }
        } catch (err) {
            console.error("Error updating user:", err);
            alert("An unexpected error occurred.");
        }
    };

    const handleCancel = () => {
        setEditUserId(null);
        setEditUserData({});
    };

    const handleDelete = async (userId: number) => {
        const confirmed = window.confirm("Are you sure you want to delete this user?");
        if (!confirmed) return;

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/admin/users/${userId}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                alert("User deleted successfully!");
                setUsers((prevUsers) => prevUsers.filter((user) => user.id !== userId));
            } else {
                alert("Failed to delete the user. Please try again.");
            }
        } catch (err) {
            console.error("Error deleting user:", err);
            alert("An unexpected error occurred.");
        }
    };

    const handleRoleChange = async (userId: number, newRoleId: number) => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized: Please log in.");
            return;
        }

        // Kérj megerősítést a rangváltás előtt
        const confirmed = window.confirm("Are you sure you want to change this user's role?");
        if (!confirmed) {
            return; // Megszakítjuk, ha a felhasználó nem erősítette meg
        }

        try {
            const response = await fetch(`http://localhost:8080/admin/${userId}/role`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ roleId: newRoleId }),
            });

            if (response.ok) {
                alert("Role updated successfully!");
                setUsers((prevUsers) =>
                    prevUsers.map((user) =>
                        user.id === userId ? { ...user, role: { ...user.role, id: newRoleId } } : user
                    )
                );
            } else {
                alert("Failed to update role. Please try again.");
            }
        } catch (err) {
            console.error("Error updating role:", err);
            alert("An unexpected error occurred.");
        }
    };


    return (
        <div className="bg-[#F6EEC9] min-h-screen flex flex-col items-center p-6 text-white">
            <h1 className="text-2xl font-bold text-black mb-4">User List</h1>
            <p className="text-red-500">{error}</p>
            <table className="table-auto w-full border-collapse border border-black bg-[#7caa56] text-black">
                <thead>
                <tr className="bg-[#EE4E4E] h-12">
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-12">#</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-32">Username</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-40">Email</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-32">First Name</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-32">Last Name</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-40">Date of Birth</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-32">Country</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-48">Registration Date</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-48">Edit User</th>
                    <th className="border border-[#F6EEC9] px-4 py-2 text-black w-32">Role</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <tr key={index} className="hover:bg-[#A1DD70] hover:text-black font-bold h-12">
                        <td className="border border-[#F6EEC9] px-4 py-2 text-center">{index + 1}</td>
                        {editUserId === user.id ? (
                            <>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.username || ""}
                                        onChange={(e) =>
                                            handleInputChange("username", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.email || ""}
                                        onChange={(e) =>
                                            handleInputChange("email", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.firstName || ""}
                                        onChange={(e) =>
                                            handleInputChange("firstName", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.lastName || ""}
                                        onChange={(e) =>
                                            handleInputChange("lastName", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.dateOfBirth || ""}
                                        onChange={(e) =>
                                            handleInputChange("dateOfBirth", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <input
                                        className="bg-[#F6EEC9] text-black w-full h-full px-2 py-1"
                                        value={editUserData.country || ""}
                                        onChange={(e) =>
                                            handleInputChange("country", e.target.value)
                                        }
                                    />
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    {user.registrationDate}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-0 text-center">
                                    <div className="flex justify-center space-x-2">
                                        <button
                                            onClick={handleSave}
                                            className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition duration-300"
                                        >
                                            Save
                                        </button>
                                        <button
                                            onClick={handleCancel}
                                            className="bg-gray-500 text-white px-2 py-1 rounded hover:bg-gray-600 transition duration-300"
                                        >
                                            Cancel
                                        </button>
                                    </div>
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {editUserId === user.id ? null : (
                                        <select
                                            className="bg-[#3388ff] text-black w-full h-full px-2 py-1 rounded"
                                            value={user.role.id}
                                            onChange={(e) => handleRoleChange(user.id, Number(e.target.value))}
                                        >
                                            <option value={1}>Admin</option>
                                            <option value={2}>User</option>
                                        </select>
                                    )}
                                </td>
                            </>
                        ) : (
                            <>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.username}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.email}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.firstName || ""}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.lastName || ""}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.dateOfBirth || ""}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.country || ""}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {user.registrationDate}
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    <div className="flex justify-center space-x-2">
                                        <button
                                            onClick={() => handleEdit(user)}
                                            className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600 transition duration-300"
                                        >
                                            Edit
                                        </button>
                                        <button
                                            onClick={() => handleDelete(user.id)}
                                            className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition duration-300"
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </td>
                                <td className="border border-[#F6EEC9] px-4 py-2 text-center">
                                    {currentUser && (currentUser.userId === user.id || user.role.id === 1) ? (
                                        <button
                                            className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition duration-300"
                                            onClick={() =>
                                                alert("You cannot change your own role or role of another admin!")
                                            }
                                        >
                                            Alert
                                        </button>
                                    ) : (
                                        <select
                                            className="bg-[#3388FF] text-black w-full h-full px-2 py-1 rounded"
                                            value={user.role.id}
                                            onChange={(e) => handleRoleChange(user.id, Number(e.target.value))}
                                        >
                                            <option className="font-bold" value={1}>Admin</option>
                                            <option className="font-bold" value={2}>User</option>
                                        </select>
                                    )}
                                </td>
                            </>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default Users;
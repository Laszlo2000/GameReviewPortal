import { useEffect, useState } from 'react';

const App = () => {
    const [data, setData] = useState<string | null>(null);

    useEffect(() => {
        fetch('http://localhost:8080/api/data')
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Válasz JSON-ként való feldolgozása
            })
            .then((data) => {
                setData(data.message); // A válaszból kinyerjük az üzenetet
            })
            .catch((error) => {
                console.error("Error fetching data from backend:", error);
            });
    }, []);

    return (
        <div className="bg-blue-400 flex items-center justify-center h-screen">
            {/*<h1 className="text-4xl font-bold text-black">{data ? data : 'Loading data from backend...'}</h1>*/}
            <h1 className="text-black text-center text-lg font-bold">{data}</h1>
        </div>
    );
};

export default App;

import { useEffect, useState } from 'react';
import './index.css';

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
        <div className="bg-amber-300">
            {/*<h1 className="text-4xl font-bold text-black">{data ? data : 'Loading data from backend...'}</h1>*/}
            <h1 className="text-blue-500">{data}</h1>
            <h1 className="text-red-800">Static Text</h1>
        </div>
    );
};

export default App;

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
        <div>
            <h1>{data ? data : 'Loading data from backend...'}</h1>
        </div>
    );
};

export default App;

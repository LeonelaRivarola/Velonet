import { useEffect, useState } from "react";
import { getCurvaPagos, getMetrics } from "../services/dashboardService";

export default function useDashboard() {

    const [metrics, setMetrics] = useState(null);
    const [curvaPagos, setCurvaPagos] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function cargar() {
            try {

                const data = await getMetrics();
                const curva =  await getCurvaPagos( "01-01-2026", "31-12-2026");

                setMetrics(data)
                setCurvaPagos(curva);
            } finally {
                setLoading(false)
            }
        }

        cargar();
    }, []);

    return {
        metrics, curvaPagos, loading
    };
}
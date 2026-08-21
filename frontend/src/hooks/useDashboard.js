import { useEffect, useState } from "react";
import { getCurvaPagos, getMetrics, getHistoricoDeudores } from "../services/dashboardService";

export default function useDashboard() {

    const [metrics, setMetrics] = useState(null);
    const [curvaPagos, setCurvaPagos] = useState([]);
    const [deudores, setDeudores] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function cargar() {
            try {

                const data = await getMetrics();
                const curva =  await getCurvaPagos(60);
                const historico = await getHistoricoDeudores();

                setMetrics(data);
                setDeudores(historico);
                setCurvaPagos(curva);
            } finally {
                setLoading(false)
            }
        }

        cargar();
    }, []);

    return {
        metrics, curvaPagos, loading, deudores
    };
}
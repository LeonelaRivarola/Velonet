import { useEffect, useState } from "react";
import { getCurvaPagos, getMetrics, getHistoricoDeudores, getHistorialCuentaCorriente } from "../services/dashboardService";

export default function useDashboard() {

    const [metrics, setMetrics] = useState(null);
    const [curvaPagos, setCurvaPagos] = useState([]);
    const [deudores, setDeudores] = useState([]);
    const [historialCuentaCorriente, setHistorialCuentaCorriente] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function cargar() {
            try {

                const data = await getMetrics();
                const curva =  await getCurvaPagos(60);
                const historico = await getHistoricoDeudores();
                const historial = await getHistorialCuentaCorriente();

                setMetrics(data);
                setDeudores(historico);
                setCurvaPagos(curva);
                setHistorialCuentaCorriente(historial.reverse()); //esto para mostrar la fecha mas antigua a la izquierda
            } catch (error) {
                console.error("Error al cargar datos del dashboard:", error);
            }
            finally {
                setLoading(false)
            }
        }

        cargar();
    }, []);

    return {
        metrics, curvaPagos, loading, deudores, historialCuentaCorriente
    };
}
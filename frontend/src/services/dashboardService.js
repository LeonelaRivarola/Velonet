import axios from "axios";

const API = "http://localhost:8081/dashboard";

export const getMetrics = async () => {
    const response = await axios.get(`${API}/metrics`);
    return response.data;
};

export const getCurvaPagos = async (periodo = 60) => {
    const response = await axios.get(`${API}/pagos`, {
        params: {
            periodo
        }
    });
    return response.data;
};

export async function getHistoricoDeudores() {
    const response = await axios.get(`${API}/deudores`);
    return response.data;
}
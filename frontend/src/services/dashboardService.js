import axios from "axios";

const API = "http://localhost:8081/dashboard";

export const getMetrics = async () => {
    const response = await axios.get(`${API}/metrics`);
    return response.data;
};

export const getCurvaPagos = async (fechaDesde, fechaHasta) => {
    const response = await axios.get(`${API}/pagos`, {
        params: {
            fechaDesde,
            fechaHasta
        }
    });
    return response.data;
}
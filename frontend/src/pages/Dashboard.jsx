import React, { useState, useMemo } from 'react';
import { Box, Grid2 as Grid, Typography, Paper, Button, Divider, Card, CardContent } from '@mui/material';
import { ResponsiveContainer, ComposedChart, Line, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import FilterBar from '../components/dashboard/FilterBar';
import CardMetric from '../components/dashboard/MetricCard';

import { FiUsers, FiFileText, FiTrendingDown, FiActivity, FiDollarSign, FiAlertTriangle, FiCreditCard } from 'react-icons/fi';
import useDashboard from '../hooks/useDashboard';

// DATOS FICTICIOS COMPLETOS BASADOS EN EL AUDIO:
const mockDatosControl = [
  {
    periodo: 'Abril',
    recaudadoAlDia15: 1450000,
    deudaFinMes: 420000,
    cortadosAlDia23: 12,
    bajasVoluntarias: 5,
    bajasPorFaltaPago: 8
  },
  {
    periodo: 'Mayo',
    recaudadoAlDia15: 1620000,
    deudaFinMes: 380000,
    cortadosAlDia23: 8,
    bajasVoluntarias: 4,
    bajasPorFaltaPago: 6
  },
  {
    periodo: 'Junio',
    recaudadoAlDia15: 1890000,
    deudaFinMes: 590000,
    cortadosAlDia23: 19,
    bajasVoluntarias: 9,
    bajasPorFaltaPago: 14
  },
];

export default function Dashboard() {
  const [cobroTerceros, setCobroTerceros] = useState(null);
  const { metrics, curvaPagos, loading, deudores, historialCuentaCorriente } = useDashboard();

  const handleSearch = (filters) => {
    console.log("Filtros aplicados (Contrato Modelo / Fechas):", filters);
  };

  const calcularCobroTerceros = () => {
    const baseCalculo = 1845000;
    setCobroTerceros(baseCalculo.toLocaleString('es-AR', { style: 'currency', currency: 'ARS' }));
  };

  const datosCurva = curvaPagos.map(item => ({
    periodo: item.mesAnio,
    recaudadoAlDia15: item.totalCobrado
  }));


  const normalizarPeriodo = (fechaOMes) => {
  if (fechaOMes.includes('-') && fechaOMes.split('-')[0].length === 4) {
    const [anio, mes] = fechaOMes.split('-');
    return `${mes}-${anio}`;
  }
  return fechaOMes;
};

  //unificacion de deuda en $ y cantidad de deudores
  const datosDeudaYDeudores = useMemo(() => {
    const mapaDatos = {};

    historialCuentaCorriente.forEach(item => {
      const periodo = normalizarPeriodo(item.fecha);
      mapaDatos[periodo] = { ...mapaDatos[periodo], periodo, deudaTotal: item.monto };
    });

    deudores.forEach(item => {
      const periodo = normalizarPeriodo(item.mes);
      mapaDatos[periodo] = { ...mapaDatos[periodo], periodo, deudoresCant: item.cantidad };
    });

    return Object.values(mapaDatos).sort((a, b) => {
      const [mesA, anioA] = a.periodo.split('-');
      const [mesB, anioB] = b.periodo.split('-');
      return new Date(`${anioA}-${mesA}-01`) - new Date(`${anioB}-${mesB}-01`);
    });
  }, [historialCuentaCorriente, deudores]);

  const ultimoMontoCC = historialCuentaCorriente.length > 0 ? historialCuentaCorriente[historialCuentaCorriente.length - 1].monto : 0;

  if (loading) {
    return <div>Cargando...</div>;
  }

  return (
    <Box sx={{ width: '100%', backgroundColor: '#f8fafc', minHeight: '100%' }}>

      {/* Banner Superior */}
      <Paper
        elevation={0}
        sx={{
          p: 3,
          mb: 4,
          backgroundColor: '#ffffff',
          borderLeft: '6px solid #0d47a1',
          borderRadius: '8px',
          boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)'
        }}
      >
        <Typography variant="h5" fontWeight="800" sx={{ color: '#0d47a1', mb: 0.5 }}>
          CONCILIACIÓN Y CONTROL DE CLIENTES
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Panel ejecutivo automatizado. Comparativas mensuales fijadas por puntos de control operativos.
        </Typography>
      </Paper>

      {/* Filtros */}
      <FilterBar onSearch={handleSearch} />

      {/* Sección KPIs principales*/}
      <Typography variant="subtitle2" sx={{ mb: 2, fontWeight: 'bold', color: '#0d47a1', letterSpacing: '0.5px' }}>
        KPI // CONTROL DE CONTRATOS Y CLIENTES
      </Typography>

      <Grid container spacing={2} sx={{ mb: 4 }}>
        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Clientes Activos"
            value={metrics?.totalClientesActivos ?? 0}
            icon={FiUsers}
            color="primary"
            percentage="+2.1%"
            isPositive={true}
          />
        </Grid>

        {/*Tarjeta kpi 3: deuda cuenta corriente */}
        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Deuda en Cuenta Corriente"
            value={`$${(ultimoMontoCC / 1000000).toFixed(2)}M`}
            icon={FiCreditCard}
            color="error"
            percentage="Snapshot 3AM"
            isPositive={false}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Contratos Activos"
            value={metrics?.totalContratoActivos ?? 0}
            icon={FiFileText}
            color="success"
            percentage="+1.8%"
            isPositive={true}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Cortados (Día 23)"
            value="19"
            icon={FiActivity}
            color="warning"
            percentage="-5.1%"
            isPositive={true}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Bajas de Contratos"
            value="23"
            icon={FiTrendingDown}
            color="error"
            percentage="+12.4%"
            isPositive={false}
          />
        </Grid>

        {/* NUEVO KPI: CUMPLIENDO PUNTO 5 DEL AUDIO */}
        <Grid size={{ xs: 12, sm: 6, md: 2.4 }}>
          <CardMetric
            title="Sin Cobro (Auditoría)" //
            value={metrics?.contratosSinCobroAuditoria ?? 0}
            icon={FiAlertTriangle}
            color="error"
            percentage="Revisar"
            isPositive={false}
          />
        </Grid>
      </Grid>

      {/* Sección de Gráficos Separados */}
      <Typography variant="subtitle2" sx={{ mb: 2, fontWeight: 'bold', color: '#0d47a1', letterSpacing: '0.5px' }}>
        PUNTOS DE CONTROL MENSUAL COMPARATIVOS
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>

        {/* GRÁFICO 1: Curvas de Pago al Día 15 */}
        <Grid size={{ xs: 12, md: 4 }}>
          <Paper sx={{ p: 3, borderRadius: '8px', boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)', height: '100%' }}>
            <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 'bold', color: '#0d47a1' }}>
              1. Curvas de Pago (Día 15)
            </Typography>
            <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 2 }}>
              Evolución de montos recaudados en Cuenta Corriente.
            </Typography>
            <Box sx={{ width: '100%', height: 260 }}>
              <ResponsiveContainer>
                <ComposedChart data={datosCurva} margin={{ top: 10, left: 25, right: 15, bottom: 10 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                  <XAxis dataKey="periodo" tick={{ fill: '#64748b', fontSize: 12 }} />
                  <YAxis tick={{ fill: '#0d47a1', fontSize: 12 }} tickFormatter={(value) =>
                    new Intl.NumberFormat("es-AR").format(value)} />
                  <Tooltip formatter={(value) => `$${value.toLocaleString('es-AR')}`} />
                  <Bar dataKey="recaudadoAlDia15" name="Recaudado ($)" fill="#0d47a1" barSize={35} radius={[4, 4, 0, 0]} />
                </ComposedChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>


        {/* NUEVO GRÁFICO 2: HISTORIAL DEUDA CUENTA CORRIENTE (KPI 3) */}
        {/* GRÁFICO 2: Historial Deuda + Clientes Deudores */}
<Grid size={{ xs: 12, md: 4 }}>
  <Paper sx={{ p: 3, borderRadius: '8px', boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)', height: '100%' }}>
    <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 'bold', color: '#d32f2f' }}>
      2. Historial Deuda Cuenta Corriente
    </Typography>
    <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 2 }}>
      Evolución de la deuda en Cuenta Corriente vs. cantidad de clientes deudores.
    </Typography>
    <Box sx={{ width: '100%', height: 260 }}>
      <ResponsiveContainer>
        <ComposedChart data={datosDeudaYDeudores} margin={{ top: 10, left: 5, right: 15, bottom: 10 }}>
          <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
          <XAxis dataKey="periodo" tick={{ fill: '#64748b', fontSize: 12 }} />

          <YAxis
            yAxisId="izq"
            orientation="left"
            tick={{ fill: '#d32f2f', fontSize: 10 }}
            tickFormatter={(val) => `$${(val / 1000000).toFixed(1)}M`}
          />
          <YAxis
            yAxisId="der"
            orientation="right"
            tick={{ fill: '#0d47a1', fontSize: 10 }}
            allowDecimals={false}
          />

          <Tooltip
            formatter={(val, name) =>
              name === 'Deuda ($)' ? `$${Number(val).toLocaleString('es-AR')}` : `${val} clientes`
            }
          />
          <Legend iconSize={10} wrapperStyle={{ fontSize: '11px' }} />

          <Bar
            yAxisId="der"
            dataKey="deudoresCant"
            name="Clientes Deudores"
            fill="#93c5fd"
            barSize={30}
            radius={[6, 6, 0, 0]}
          />
          <Line
            yAxisId="izq"
            type="monotone"
            dataKey="deudaTotal"
            name="Deuda ($)"
            stroke="#d32f2f"
            strokeWidth={3}
            dot={{ r: 5, fill: '#d32f2f', strokeWidth: 2, stroke: '#fff' }}
            activeDot={{ r: 7 }}
          />
        </ComposedChart>
      </ResponsiveContainer>
    </Box>
  </Paper>
</Grid>

        {/* GRÁFICO 3: Curvas de Deudores */}
        {/* <Grid size={{ xs: 12, md: 3 }}>
          <Paper sx={{ p: 3, borderRadius: '8px', boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)', height: '100%' }}>
            <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 'bold', color: '#b91c1c' }}>
              2. Deudores e Impacto de Cortes
            </Typography>
            <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 2 }}>
              Saldo deudor fin de mes vs. Cortes.
            </Typography>
            <Box sx={{ width: '100%', height: 260 }}>
              <ResponsiveContainer>
                <ComposedChart data={datosDeudaYDeudores} margin={{ left: -15, right: -15 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                  <XAxis dataKey="periodo" tick={{ fill: '#64748b', fontSize: 12 }} />
                  <YAxis tick={{ fill: '#b91c1c', fontSize: 12 }} />
                  <Tooltip />
                  <Legend />
                  <Line type="monotone" dataKey="deudoresCant                      " name="Clientes Deudores" stroke="#ef4444" strokeWidth={3} dot={{ r: 5 }} />
                </ComposedChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid> */}

        {/* GRÁFICO 4: Bajas */}
        <Grid size={{ xs: 12, md: 3 }}>
          <Paper sx={{ p: 3, borderRadius: '8px', boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)', height: '100%' }}>
            <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 'bold', color: '#374151' }}>
              5. Desglose de Bajas (Churn)
            </Typography>
            <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 2 }}>
              Motivos de desconformidad.
            </Typography>
            <Box sx={{ width: '100%', height: 260 }}>
              <ResponsiveContainer>
                <ComposedChart data={mockDatosControl} margin={{ left: -15, right: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                  <XAxis dataKey="periodo" tick={{ fill: '#64748b', fontSize: 12 }} />
                  <YAxis tick={{ fill: '#4b5563', fontSize: 12 }} />
                  <Tooltip />
                  <Legend iconSize={10} wrapperStyle={{ fontSize: '11px' }} />
                  <Bar dataKey="bajasVoluntarias" name="Voluntarias" fill="#475569" barSize={15} stackId="bajas" />
                  <Bar dataKey="bajasPorFaltaPago" name="Por Falta de Pago" fill="#f43f5e" barSize={15} stackId="bajas" />
                </ComposedChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

      </Grid>

      {/* Módulo de Liquidación a Terceros */}
      <Grid container spacing={3}>
        <Grid size={{ xs: 12 }}>
          <Card sx={{ borderRadius: '8px', boxShadow: '0px 2px 12px rgba(0, 0, 0, 0.04)', borderTop: '4px solid #ff9800' }}>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="subtitle1" sx={{ fontWeight: 'bold', color: '#0d47a1', mb: 1 }}>
                Módulo Automatizado: Liquidación a Terceros
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Fórmula de tasación automatizada: 60% base de abonos compartidos + Costos de Postes + Enlaces Icasatti + Licenciamiento OLT Cloud.
              </Typography>

              <Divider sx={{ my: 2 }} />

              <Box display="flex" flexDirection={{ xs: 'column', sm: 'row' }} alignItems="center" justifyContent="space-between" gap={3}>
                <Button
                  variant="contained"
                  startIcon={<FiDollarSign />}
                  onClick={calcularCobroTerceros}
                  sx={{
                    backgroundColor: '#ff9800',
                    '&:hover': { backgroundColor: '#e65100' },
                    fontWeight: 'bold',
                    textTransform: 'none',
                    px: 4,
                    py: 1.5,
                    borderRadius: '6px',
                    minWidth: '260px'
                  }}
                >
                  Calcular Canon de Terceros
                </Button>

                {cobroTerceros && (
                  <Box sx={{ p: 2, px: 4, bgcolor: '#fff3e0', borderRadius: '6px', border: '1px solid #ffe0b2', flexGrow: 1, textAlign: 'center', maxWidth: '400px' }}>
                    <Typography variant="caption" color="warning.dark" sx={{ fontWeight: 'bold', display: 'block', textTransform: 'uppercase' }}>
                      Monto Neto a Facturar al Tercero:
                    </Typography>
                    <Typography variant="h4" fontWeight="900" color="#e65100">
                      {cobroTerceros}
                    </Typography>
                  </Box>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

    </Box>
  ); //QUEDAMOS EN UNIFICAR LOS GRAFICOS
}
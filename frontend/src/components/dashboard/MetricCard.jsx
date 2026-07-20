import React from 'react';
import { Card, CardContent, Typography, Box, Avatar } from '@mui/material';

export default function CardMetric({ title, value, icon: Icon, color, percentage, isPositive }) {

    // Mapeo manual de colores para encajar con la estética limpia de Velonet
    const getColorStyles = (severity) => {
        switch (severity) {
            case 'primary': // Clientes Activos (Azul Velonet)
                return { bg: '#e3f2fd', color: '#0d47a1' };
            case 'success': // Contratos Habilitados (Verde)
                return { bg: '#e8f5e9', color: '#2e7d32' };
            case 'warning': // Contratos sin cobro (Naranja de acento)
                return { bg: '#fff3e0', color: '#e65100' };
            case 'error': // Cortes / Alertas (Rojo)
                return { bg: '#ffebee', color: '#c62828' };
            default:
                return { bg: '#f5f5f5', color: '#757575' };
        }
    };

    const styles = getColorStyles(color);

    return (
        <Card
            elevation={0}
            sx={{
                borderRadius: '12px',
                backgroundColor: '#ffffff',
                border: '1px solid #e2e8f0',
                minHeight: '110px',
                display: 'flex',
                alignItems: 'center',
                // transition: 'all 0.2s ease-in-out',
                // '&:hover': { 
                //   transform: 'translateY(-2px)',
                //   boxShadow: '0px 6px 20px rgba(13, 71, 161, 0.08)' // Sutil destello azul al hacer hover
                // }
            }}
        >
            <CardContent sx={{ p: 2, width: '100%', '&:last-child': { pb: 2 } }}>
                <Box display="flex" alignItems="center" justifyContent="space-between">
                    <Box>
                        <Typography
                            variant="caption"
                            color="text.secondary"
                            fontWeight={700}
                            sx={{ textTransform: 'uppercase', letterSpacing: '0.3px', display: 'block', minHeight: '16px' }}
                        >
                            {title}
                        </Typography>
                        <Typography
                            variant="h5"
                            fontWeight={800}
                            sx={{ color: '#1e293b', mt: 0.2 }}
                        >
                            {value}
                        </Typography>

                        {/*comparativa*/}
                        {percentage && (
                            <Typography
                                variant="caption"
                                fontWeight={600}
                                sx={{
                                    color: isPositive ? '#2e7d32' : '#c62828',
                                    backgroundColor: isPositive ? '#e8f5e9' : '#ffebee',
                                    padding: '2px 6px',
                                    borderRadius: '4px',
                                    display: 'inline-block',
                                    mt: 0.5,
                                }}
                            >
                                {percentage} vs mes anterior
                            </Typography>
                        )}
                    </Box>

                    <Avatar
                        sx={{
                            bgcolor: styles.bg,
                            color: styles.color,
                            width: 40,
                            height: 40
                        }}
                    >
                        <Icon size={20} />
                    </Avatar>
                </Box>
            </CardContent>
        </Card>
    );
}
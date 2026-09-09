import React from 'react';
import { Card, CardContent, Typography, Box, Avatar } from '@mui/material';

export default function CardMetric({ title, value, icon: Icon, color }) {

    const getColorStyles = (severity) => {
        switch (severity) {
            case 'primary':
                return { bg: '#e0f7fa', color: '#0077b6' };

            case 'success':
                return { bg: '#e0f7fa', color: '#0077b6' };

            case 'warning':
                return { bg: '#fff3e0', color: '#e65100' };

            case 'error':
                return { bg: '#ffebee', color: '#c62828' };

            default:
                return { bg: '#f1f5f9', color: '#64748b' };
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
                minHeight: '140px',
                display: 'flex',
                alignItems: 'center',
                transition: 'all 0.2s ease',

                '&:hover': {
                    transform: 'translateY(-2px)',
                    boxShadow: '0 6px 18px rgba(3, 4, 94, 0.08)',
                    borderColor: '#90e0ef',
                },
            }}
        >
            <CardContent
                sx={{
                    p: 2.5,
                    '&:last-child': {
                        pb: 2.5,
                    },
                }}
            >
                <Box
                    display="flex"
                    alignItems="center"
                    justifyContent="space-between"
                >
                    <Box>
                        <Typography
                            variant="caption"
                            sx={{
                                color: '#64748b',
                                fontWeight: 700,
                                textTransform: 'uppercase',
                                letterSpacing: '0.4px',
                                display: 'block',
                                height: '36px',
                                lineHeight: 1.5,
                            }}
                        >
                            {title}
                        </Typography>

                        <Typography
                            variant="h4"
                            sx={{
                                color: '#03045e',
                                fontWeight: 800,
                                lineHeight: 1.2,
                            }}
                        >
                            {value}
                        </Typography>
                    </Box>

                    <Avatar
                        sx={{
                            bgcolor: styles.bg,
                            color: styles.color,
                            width: 46,
                            height: 46,
                            borderRadius: '12px',
                        }}
                    >
                        <Icon size={21} />
                    </Avatar>
                </Box>
            </CardContent>
        </Card>
    );
}
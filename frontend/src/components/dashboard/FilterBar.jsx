import React from "react";
import { Box, Button, Paper, Stack, Typography } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import SearchIcon from "@mui/icons-material/Search";

export default function FilterBar({ onSearch }) {
    const [startDate, setStartDate] = React.useState(null);
    const [endDate, setEndDate] = React.useState(null);

    const handleSearch = () => {
        if (onSearch) {
            onSearch({ startDate, endDate });
        }
    };

    return (
        <Paper
            elevation={2}
            sx={{
                p: 3,
                mb: 4,
                borderRadius: 2,
                backgroundColor: "#ffffff",
            }}
        >
            <Typography variant="h6" sx={{ mb:2, fontWeight: 600, color: "#333" }}>
                Filtros de Búsqueda
            </Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems="center">
                
                <DatePicker
                    label="Fecha desde"
                    value={startDate}
                    onChange={(newValue) => setStartDate(newValue)}
                    slotProps={{ textField: { fullWidth: true, size: 'small' } }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<SearchIcon />}
                    onClick={handleSearch}
                    fullWidth
                    sx={{ height: '40px',
                        maxWidth: { sm: '180px' },
                        textTransform: 'none',
                        fontWeight: 'bold',
                     }}
                >
                    Buscar
                </Button>
            </Stack>
        </Paper>
    )
}
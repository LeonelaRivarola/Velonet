import { AppBar, Toolbar, Typography, Avatar, Box } from "@mui/material";
import { drawerWidth } from "./../../constants/layout";

function Header() {
    return (
        <AppBar
            position="fixed"
            sx={{
                width: `calc(100% - ${drawerWidth}px)`,
                ml: `${drawerWidth}px`,
                zIndex: (theme) => theme.zIndex.drawer + 1,
            }}
        >
            <Toolbar sx={{ justifyContent: 'space-between' }}>
                <Box display="flex" alignItems="center" gap={1.5}>
                    {/* Simulación del Isotipo de Velonet en Naranja */}
                    <Box
                        sx={{
                            width: 12,
                            height: 12,
                            borderRadius: '50%',
                            backgroundColor: '#ff9800', // El naranja del logo
                            boxShadow: '0 0 8px #ff9800'
                        }}
                    />
                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 800,
                            letterSpacing: '0.5px',
                            fontFamily: '"Montserrat", "Roboto", sans-serif'
                        }}
                    >
                        VELONET <span style={{ fontWeight: 300, fontSize: '0.85em' }}>Dashboard</span>
                    </Typography>
                </Box>

                <Box display="flex" alignItems="center" gap={2}>
                    <Typography variant="body2" sx={{ fontWeight: 500, opacity: 0.95 }}>
                        Leonela
                    </Typography>
                    {/* Avatar usando el naranja de acento para contrastar sobre el fondo azul */}
                    <Avatar sx={{ bgcolor: "#ff9800", fontWeight: 'bold', fontSize: '14px' }}>L</Avatar>
                </Box>
            </Toolbar>
        </AppBar>
    );
}

export default Header;
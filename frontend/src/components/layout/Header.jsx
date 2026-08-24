import React from "react";
import { AppBar, Toolbar, Typography, IconButton, Chip, Box } from "@mui/material";
import MenuOpenIcon from "@mui/icons-material/MenuOpen";
import PersonIcon from "@mui/icons-material/Person";
import { drawerWidth } from "./../../constants/layout";

function Header({ open, toggleDrawer}) {
    const currentDrawerWidth = open ? drawerWidth : 70;
    
    return (
        <AppBar
            position="fixed"
            elevation={0}
            sx={{
                width: `calc(100% - ${currentDrawerWidth}px)`,
                ml: `${currentDrawerWidth}px`,
                backgroundColor: "#ffffff", 
                color: "#1a1a1a",
                borderBottom: "1px solid #0f0f0f", //f0f0f0
                transition: (theme) => theme.transitions.create(['width', 'margin'], {
                    easing: theme.transitions.easing.sharp,
                    duration: theme.transitions.duration.enteringScreen,
                }),
                zIndex: (theme) => theme.zIndex.drawer + 1,
            }}
        >
            <Toolbar sx={{ justifyContent: 'space-between', minHeight: '64px !important', px: 2 }}>
                {/*izq: boton menu + titulo*/}
                <Box display="flex" alignItems="center" gap={2}>
                    <IconButton
                        onClick={toggleDrawer}
                        sx={{
                            background: 'linear-gradient(135deg, #00b4d8 0%, #0077b6 100%)', 
                            color: "#ffffff",
                            borderRadius: '12px',
                            p: '8px 12px',
                            boxShadow: '0 2px 8px rgba(0, 180, 216, 0.3)',
                            '&: hover': {
                                background: 'linear-gradient(135deg, #0096c7 0%, #023e8a 100%)',
                            }
                        }}
                    >
                        <MenuOpenIcon sx={{ transform: open ? 'none' : 'rotate(180deg)' }}/>
                    </IconButton>

                    <Typography variant="h6" component="div" sx={{ fontWeight: 800, fontSize: '1.25rem', color:'#03045e', fontFamily: '"Montserrat", sans-serif' }}>
                        Histórico <span style={{ color: '#00b4d8' }}>Velonet</span>
                    </Typography>
                </Box>

                {/*der: chip usuario / pais + logo*/}
                    <Box display="flex" alingItems="center" gap={2}>
                    <Chip
                        icon={<PersonIcon style={{ color: '#0077b6', fontSize: 18}} />} 
                        label="Argentina"
                        sx={{
                            fontWeight: 700,
                            color: '#03045e',
                            borderRadius: '20px',
                            backgroundColor: '#e0f7fa', 
                            fontSize: '0.875rem',
                            border: '1px solid #e9d5ff',
                            px: 1,
                            py: 2.2,
                        }}
                    />

                    {/* Logo */}
                    <Box
                        component="img"
                        src="/path/to/your/logo.png"
                        alt="Velonet"
                        sx={{
                            height: 36,
                            objectFit: 'contain',
                        }}
                    />
                    </Box>
            </Toolbar>
        </AppBar>
    );
}

export default Header;
import {
    Drawer,
    Toolbar,
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    Box,
    ListItemIcon
} from "@mui/material"

import DashboardIcon from "@mui/icons-material/Dashboard"
import BarChartIcon from "@mui/icons-material/BarChart";
import AssessmentIcon from "@mui/icons-material/Assessment"
import SettingIcon from "@mui/icons-material/Settings"
import { drawerWidth } from "./../../constants/layout";

const menu = [
    { text: "Dashboard", icon: <DashboardIcon />},
    { text: "Reportes", icon: <AssessmentIcon />},
    { text: "Estadísticas", icon: <BarChartIcon />},
    { text: "Configuración", icon: <SettingIcon />}
];

function Sidebar() {
    return (

        <Drawer
            variant="permanent"
            sx={{
                width: open ? drawerWidth : 70,
                flexShrink: 0,
                whiteSpace: "nowrap",
                boxSizing: 'border-box',
                "& .MuiDrawer-paper": {
                    width: open ? drawerWidth : 70,
                    background: 'linear-gradient(180deg, #03045e 0%, #00b4d8 100%)',
                    color: "#ffffff",
                    overflowX: "hidden",
                    borderRight: 'none',
                    boxShadow: '4px 0 15px rgba(0,0,0,0.1)',
                    transition: (theme) => theme.transitions.create('width', {
                        easing: theme.transitions.easing.sharp,
                        duration: theme.transitions.duration.enteringScreen,
                    }),
                },
            }}
        >

            <Toolbar />
            <Box sx={{ overflow: 'auto', mt: 2, px: 1 }}>
            <List>
                {menu.map((item) => (
                        <ListItem key={item.text} disablePadding sx={{ display: 'block', mb: 1 }}>
                            <ListItemButton 
                                sx={{
                                    minHeight: 48,
                                    justifyContent: open ? 'initial' : 'center',
                                    px: 2.5,
                                    borderRadius: '10px',
                                    '&:hover': {
                                        backgroundColor: 'rgb(255, 255, 255, 0.15)',
                                        backdropFilter: 'blur(4px)',
                                    },
                                }}>
                                <ListItemIcon
                                    sx={{
                                        minWidth: 0,
                                        mr: open ? 2 : 'auto',
                                        justifyContent: 'center',
                                        color: '#90e0ef',
                                    }}    
                                >{item.icon}</ListItemIcon>
                                <ListItemText primary={item.text} 
                                    sx={{
                                        opacity: open ? 1 : 0,
                                        '& .MuiTypography-root': {
                                            fontWeight: 600,
                                            fontSize: '0.9rem',
                                        }
                                    }}
                                />
                            </ListItemButton>
                        </ListItem>
                    ))}
            </List>
            </Box>
        </Drawer>
    )
}

export default Sidebar;
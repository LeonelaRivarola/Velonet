import {
    Drawer,
    Toolbar,
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    ListItemIcon
} from "@mui/material"

import DashboardIcon from "@mui/icons-material/Dashboard"
import BarChartIcon from "@mui/icons-material/BarChart";
import AssessmentIcon from "@mui/icons-material/Assessment"
import SettingIcon from "@mui/icons-material/Settings"
import { drawerWidth } from "./../../constants/layout";

const menu = [
    {
        text: "Dashboard",
        icon: <DashboardIcon />
    },
    {
        text: "Reportes",
        icon: <AssessmentIcon />
    },
    {
        text: "Estadísticas",
        icon: <BarChartIcon />
    },
    {
        text: "Configuración",
        icon: <SettingIcon />
    }
]

function Sidebar() {
    return (

        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    boxSizing: "border-box",
                },
            }}
        >
            <Toolbar />

            <List>
                {
                    menu.map((item) => (

                        <ListItem 
                            key={item.text}
                            disablePadding
                        >
                            <ListItemButton>
                                <ListItemIcon>{item.icon}</ListItemIcon>
                                <ListItemText primary={item.text} />
                            </ListItemButton>
                        </ListItem>
                    ))
                }

            </List>
        </Drawer>
    )
}

export default Sidebar;
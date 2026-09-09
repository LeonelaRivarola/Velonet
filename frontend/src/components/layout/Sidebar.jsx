import {
  Drawer,
  Toolbar,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Box,
  ListItemIcon,
  IconButton,
  Divider
} from "@mui/material";

import DashboardIcon from "@mui/icons-material/Dashboard";
import BarChartIcon from "@mui/icons-material/BarChart";
import AssessmentIcon from "@mui/icons-material/Assessment";
import SettingsIcon from "@mui/icons-material/Settings";
import MenuOpenIcon from "@mui/icons-material/MenuOpen";

import { drawerWidth } from "./../../constants/layout";

const menu = [
  { text: "Dashboard", icon: <DashboardIcon /> },
  { text: "Reportes", icon: <AssessmentIcon /> },
  { text: "Estadísticas", icon: <BarChartIcon /> },
  { text: "Configuración", icon: <SettingsIcon /> }
];

function Sidebar({ open, toggleDrawer }) {

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: open ? drawerWidth : 70,
        flexShrink: 0,
        whiteSpace: "nowrap",

        "& .MuiDrawer-paper": {
          width: open ? drawerWidth : 70,

          backgroundColor: "#03045e",
          color: "#ffffff",

          overflowX: "hidden",
          borderRight: "none",

          transition: (theme) =>
            theme.transitions.create("width", {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.enteringScreen,
            }),
        },
      }}
    >

      {/* ESPACIO DEL HEADER */}
      <Toolbar
        sx={{
          minHeight: "64px !important",
          display: "flex",
          justifyContent: open ? "flex-end" : "center",
          px: 1,
        }}
      >

        {/* BOTÓN DEL MENÚ */}
        <IconButton
          onClick={toggleDrawer}
          sx={{
            color: "#ffffff",

            backgroundColor: "rgba(255,255,255,0.08)",

            borderRadius: "8px",

            "&:hover": {
              backgroundColor: "#0077b6",
            },
          }}
        >
          <MenuOpenIcon
            sx={{
              transform: open
                ? "none"
                : "rotate(180deg)",

              transition: "0.2s",
            }}
          />
        </IconButton>

      </Toolbar>

      <Divider
        sx={{
          borderColor: "rgba(255,255,255,0.12)",
        }}
      />

      {/* MENÚ */}
      <Box
        sx={{
          overflow: "hidden",
          mt: 2,
          px: 1,
        }}
      >

        <List>

          {menu.map((item) => (

            <ListItem
              key={item.text}
              disablePadding
              sx={{
                display: "block",
                mb: 0.5,
              }}
            >

              <ListItemButton
                sx={{
                  minHeight: 48,

                  justifyContent: open
                    ? "initial"
                    : "center",

                  px: 2,

                  borderRadius: "8px",

                  color: "#ffffff",

                  "&:hover": {
                    backgroundColor: "#0077b6",
                  },
                }}
              >

                <ListItemIcon
                  sx={{
                    minWidth: 0,

                    mr: open
                      ? 2
                      : "auto",

                    justifyContent: "center",

                    color: "#90e0ef",
                  }}
                >
                  {item.icon}
                </ListItemIcon>

                <ListItemText
                  primary={item.text}
                  sx={{
                    opacity: open ? 1 : 0,

                    "& .MuiTypography-root": {
                      fontWeight: 600,
                      fontSize: "0.9rem",
                    },
                  }}
                />

              </ListItemButton>

            </ListItem>

          ))}

        </List>

      </Box>

    </Drawer>
  );
}

export default Sidebar;
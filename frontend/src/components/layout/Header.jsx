import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Chip,
  Box
} from "@mui/material";

import MenuOpenIcon from "@mui/icons-material/MenuOpen";
import PersonIcon from "@mui/icons-material/Person";

import { drawerWidth } from "./../../constants/layout";

function Header({ open, toggleDrawer }) {

  const currentDrawerWidth = open ? drawerWidth : 70;

  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        width: `calc(100% - ${currentDrawerWidth}px)`,
        ml: `${currentDrawerWidth}px`,

        backgroundColor: "#ffffff",
        color: "#03045e",

        borderBottom: "2px solid #0077b6",

        transition: (theme) =>
          theme.transitions.create(
            ["width", "margin"],
            {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.enteringScreen,
            }
          ),

        zIndex: (theme) => theme.zIndex.drawer + 1,
      }}
    >

      <Toolbar
        sx={{
          minHeight: "64px !important",
          px: 2,
          display: "flex",
          justifyContent: "space-between",
        }}
      >

        {/* IZQUIERDA */}
        <Box
          display="flex"
          alignItems="center"
          gap={2}
        >

      

          <Typography
            variant="h6"
            sx={{
              fontWeight: 800,
              fontSize: "1.25rem",
              color: "#03045e",
            }}
          >
            Dashboard{" "}
            <Box
              component="span"
              sx={{ color: "#0077b6" }}
            >
              Velonet
            </Box>
          </Typography>

        </Box>


        {/* DERECHA */}
        <Box
          display="flex"
          alignItems="center"
          gap={2}
        >

          <Chip
            icon={
              <PersonIcon
                sx={{
                  color: "#0077b6 !important",
                }}
              />
            }
            label="Argentina"
            sx={{
              fontWeight: 700,
              color: "#03045e",

              backgroundColor: "#e0f7fa",

              border:
                "1px solid #90e0ef",

              borderRadius: "20px",

              px: 1,
              py: 2.2,
            }}
          />

          {/* LOGO */}
          <Box
            component="img"
            src="/path/to/your/logo.png"
            alt="Velonet"
            sx={{
              height: 38,
              width: "auto",
              objectFit: "contain",
            }}
          />

        </Box>

      </Toolbar>
    </AppBar>
  );
}

export default Header;
import { AppBar, Toolbar, Typography, Avatar, Box } from "@mui/material"

function Header() {
    return (
        <AppBar position="fixed"
            sx={{
                    zIndex: (theme)=> theme.zIndex.drawer + 1,
                    backgroundColor: "#1565C0"
            }}>
            <Toolbar>

                <Typography varian="h6"
                    sx={{
                        flexGrow: 1,
                        fontWeight: "bold"
                    }}>
                    Velonet Dashboard
                </Typography>

                <Box display="flex"
                    alingItems="center"
                    gap={2}
                >
                    <Typography>
                        Leonela
                    </Typography>

                    <Avatar>L</Avatar>
                    
                </Box>

            </Toolbar>
        </AppBar>
    )
}

export default Header;
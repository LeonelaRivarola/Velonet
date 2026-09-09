import { useState } from "react";
import { Box, Toolbar } from "@mui/material"

import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";

function MainLayout({ children }) {
    const [open, setOpen] = useState(false);
    const toggleDrawer = () => setOpen((prev) => !prev);

    return (
        <Box sx={{ display: "flex" }}>

            <Header />
            <Sidebar open={open} toggleDrawer={toggleDrawer} />

            <Box component="main"
                sx={{
                    flexGrow: 1,
                    bgcolor: "#f4f6f8",
                    minHeight: "100vh",
                    overflow: "auto"
                }}
            >
                <Toolbar />

                <Box
                    sx={{
                        p: 4,
                        maxWidth: "1800px",
                        mx: "auto"
                    }}
                >
                    {children}
                </Box>

            </Box>

        </Box>
    );
}

export default MainLayout
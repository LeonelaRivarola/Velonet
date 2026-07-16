import { Box, Toolbar } from "@mui/material"

import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";


function MainLayout({ children }){

    return (
        <Box sx={{display:"flex"}}>

            <Header />
            <Sidebar />
                
            <Box component="main" 
                sx={{
                    flexGrow: 1,
                    bgcolor:"#f4f6f8",
                    minHeight:"100vh",
                    p: 4,
                }}
            >
                <Toolbar />
                {children}

            </Box>

        </Box>
    );
}

export default MainLayout
import {Route, Routes} from "react-router-dom";

import PublicHeader from "./components/layout/header/PublicHeader";

import SignInPage from "@/pages/_auth.signin/route.tsx";
import UserManagementPage from "@/pages/_admin.user_management/route.tsx";

function App() {
    return (
        <Routes>
            <Route element={<PublicHeader/>} path="/header"/>
            <Route element={<UserManagementPage/>} path="/manage"/>
            <Route element={<SignInPage/>} path="/signin"/>
        </Routes>
    );
}

export default App;

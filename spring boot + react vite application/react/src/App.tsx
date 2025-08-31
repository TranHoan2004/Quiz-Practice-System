/* eslint-disable prettier/prettier */
import { Route, Routes } from "react-router-dom";

import SignInPage from "@/pages/_auth.signin/route.tsx";
import PublicHeader from "@/components/layout/header/PublicHeader.tsx";

function App() {
  return (
    <Routes>
      <Route element={<PublicHeader />} path="/header" />
      <Route element={<SignInPage />} path="/signin" />
    </Routes>
  );
}

export default App;

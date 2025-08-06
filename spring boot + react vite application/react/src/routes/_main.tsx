import {HeroUIProvider, ToastProvider} from "@heroui/react";
import {Route, Routes, useHref, useNavigate} from 'react-router'
import SignInPage from "./_auth.signin/route.tsx";

const Main = () => {
    const navigate = useNavigate()
    return (
        <div className="w-full h-full">
            <HeroUIProvider navigate={navigate} useHref={useHref}>
                <ToastProvider
                    placement="top-center"
                    maxVisibleToasts={1}
                    toastProps={{radius: 'sm', timeout: 5000}}
                />
                <Routes>
                    <Route path={'/signin'} element={<SignInPage/>}/>
                </Routes>
            </HeroUIProvider>
        </div>
    );
};

export default Main;

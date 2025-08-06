import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import Main from "./routes/_main.tsx";
import {BrowserRouter} from "react-router";
import {AuthProvider} from "./contexts/AuthContext.tsx";
import './app.css'

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <div className="w-full h-full">
                    <style>
                        {`
                        :root {
                            --left-sidebar-width: 4rem;
                            --right-sidebar-width: 0;
                        }
                        @media (min-width: 768px) {
                            :root {
                                --left-sidebar-width: clamp(180px, 180px + ((100vw - 1600px) * 0.46875), 300px);
                                --right-sidebar-width: 18rem;
                            }
                        }
                        `}
                    </style>
                    <Main/>
                </div>
            </AuthProvider>
        </BrowserRouter>
    </StrictMode>,
)

/* eslint-disable prettier/prettier */
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";

import App from "./App.tsx";
import { Provider } from "./provider.tsx";

import "@/styles/globals.css";
// import { AuthProvider } from "./contexts/AuthContext.tsx";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      {/*<AuthProvider>*/}
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
          <Provider>
            <App />
          </Provider>
        </div>
      {/*</AuthProvider>*/}
    </BrowserRouter>
  </React.StrictMode>,
);

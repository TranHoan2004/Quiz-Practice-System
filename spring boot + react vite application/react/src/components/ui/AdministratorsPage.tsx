import React, {useState} from "react";

import LeftSidebar from "@/components/layout/left-nav/LeftSidebar.tsx";
import AdministrationHeader from "@/components/layout/header/AdministrationHeader.tsx";

interface AdminPageProps {
    children: React.ReactNode;
}

const AdminPage = ({children}: AdminPageProps) => {
    return (
        <div className="flex h-screen w-full overflow-hidden">
            <div className="hidden sm:flex flex-[0.2] bg-gray-900 text-white">
                <LeftSidebar />
            </div>

            <div className="flex flex-col flex-1 min-w-0">
                <AdministrationHeader />
                <main className="flex-1 overflow-y-auto p-6 bg-gray-50 dark:bg-gray-900">
                    {children}
                </main>
            </div>
        </div>
    );
};

export default AdminPage;

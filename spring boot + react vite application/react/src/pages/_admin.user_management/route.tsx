import AdminPage from "@/components/ui/AdministratorsPage.tsx";
import UserManagement from "@/pages/_admin.user_management/UserManagement.tsx";

const UserManagementPage = () => {
    return (
        <AdminPage>
            <div className="flex flex-col gap-6">
                {/* Header */}
                <div className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-500 via-indigo-500 to-purple-500 text-transparent bg-clip-text">
                        Users Management
                    </h1>
                </div>
                <UserManagement/>
            </div>
        </AdminPage>
    );
};

export default UserManagementPage;

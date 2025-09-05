import {Button} from "@heroui/button";
import {Trash2} from "lucide-react";
import {
    Card, CardHeader, Chip
} from "@heroui/react";
import {useEffect, useState} from "react";

import {User} from "@/types/user.ts";
import SearchBar from "@/components/ui/SearchBar.tsx";
import {EditIcon, EyeIcon} from "@/components/icons.tsx";
import {DataTable} from "@/components/ui/Table.tsx";
import {getUsers} from "@/services/user.service.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {Role, ROLE, STATUS} from "@/constants/general.constant.ts";
import DataSelect from "@/components/ui/Select.tsx";
import CreateNewUser from "@/pages/_admin.user_management/CreateNewUser.tsx";

const columns = [
    {name: "NAME", uid: "fullName"},
    {name: "EMAIL", uid: "email"},
    {name: "ROLE", uid: "role"},
    {name: "CREATED DATE", uid: "createdDate"},
    {name: "PHONE NUMBER", uid: "phoneNumber"},
    {name: "STATUS", uid: "status"},
    {name: "ACTIONS", uid: "actions"},
];

const roles: Role[] = [
    ROLE.ADMIN,
    ROLE.MARKETING,
    ROLE.SALES,
    ROLE.EXPERT,
    ROLE.USER,
];

const status = [STATUS.ACTIVE, STATUS.INACTIVE];

type ResponseData = {
    content: User[];
    totalPages: number;
    totalElements: number;
}

const numberOfRecordsPerPage = [5, 10, 20];

const UserManagement = () => {
    const {user} = useAuth();
    const [users, setUsers] = useState<User[]>([]);

    const [search, setSearch] = useState("");

    const [filterRole, setFilterRole] = useState<string | undefined>(undefined);
    const [filterStatus, setFilterStatus] = useState<string | undefined>(undefined);

    const [page, setPage] = useState(1);
    const [pages, setPages] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState<number>(10);

    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        (async () => {
            // if (!user) return;
            setLoading(true);

            const data = await getUsers(
                // user.accessToken,
                page,
                rowsPerPage,
                {
                    role: filterRole,
                    status: filterStatus,
                    search: search,
                }
            );

            AssignData(data);
            setLoading(false);
        })();
    }, [user, page, rowsPerPage, filterRole, filterStatus, search]);

    const AssignData = (data: ResponseData) => {
        setUsers(data.content);
        setLoading(false);
        setPages(data.totalPages);
        setTotalElements(data.totalElements);
    }

    return (
        <>
            {/* Filters */}
            <div className="flex flex-wrap gap-4">
                <SearchBar
                    className="max-w-1/4"
                    placeholder="Search by name or email"
                    type="search"
                    value={search}
                    onChange={setSearch}
                />

                {/* Filter by role */}
                <DataSelect<string | undefined>
                    className="max-w-1/4"
                    data={filterRole}
                    placeholder="Filter by role"
                    props={{isClearable: true}}
                    selectItems={roles}
                    setData={(value) => {
                        setFilterRole(value)
                        setPage(1)
                    }}
                />

                {/* Filter by status */}
                <DataSelect<string | undefined>
                    className="max-w-1/4"
                    data={filterStatus}
                    placeholder="Filter by status"
                    props={{isClearable: true}}
                    selectItems={status}
                    setData={(value) => {
                        setFilterStatus(value)
                        setPage(1)
                    }}
                />

                <CreateNewUser roles={roles}/>
            </div>

            {/* User Table */}
            <Card className="shadow-lg rounded-2xl border border-gray-200 dark:border-gray-700 flex justify-between">
                <CardHeader>
                    <h3>Total records: {totalElements}</h3>
                    <DataSelect<number>
                        className="w-30 ml-auto"
                        data={rowsPerPage}
                        placeholder=""
                        selectItems={numberOfRecordsPerPage}
                        setData={(value) => {
                            setRowsPerPage(value)
                        }}
                    />
                </CardHeader>
                <DataTable<User>
                    columns={columns}
                    items={users}
                    loading={loading}
                    page={page}
                    renderCell={(item, col) => {
                        switch (col) {
                            case "fullName":
                                return item.fullName;
                            case "email":
                                return item.email;
                            case "role":
                                return (
                                    <span className="px-2 py-1 rounded-lg text-sm font-medium bg-gradient-to-r from-blue-100
                                        to-purple-100 text-blue-700 dark:from-gray-700 dark:to-gray-600 dark:text-gray-200">
                                        {item.role}
                                    </span>
                                );
                            case "createdDate":
                                return item.createdDate;
                            case "phoneNumber":
                                return item.phoneNumber;
                            case "status":
                                return (
                                    <Chip
                                        className="px-3 py-1 rounded-full text-sm font-medium text-white"
                                        color={item.status ? "success" : "danger"}
                                        size="sm"
                                    >
                                        {item.status ? "Active" : "Inactive"}
                                    </Chip>
                                );
                            case "actions":
                                return (
                                    <div className="flex justify-center gap-2">
                                        <Button
                                            isIconOnly
                                            variant="bordered"
                                            onPress={() => alert(`View ${item.id}`)}
                                        >
                                            <EyeIcon/>
                                        </Button>
                                        <Button
                                            isIconOnly
                                            variant="bordered"
                                            onPress={() => alert(`Edit ${item.id}`)}
                                        >
                                            <EditIcon/>
                                        </Button>
                                        <Button
                                            isIconOnly
                                            className="rounded-full border hover:border-red-400"
                                            variant="bordered"
                                            onPress={() => handleDelete(item.id)}
                                        >
                                            <Trash2 className="w-4 h-4 text-red-500"/>
                                        </Button>
                                    </div>
                                );
                            default:
                                return (item as any)[col];
                        }
                    }}
                    totalPages={pages}
                    onPageChange={setPage}
                />
            </Card>
        </>
    );
};

export default UserManagement;

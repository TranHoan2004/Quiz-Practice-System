import {Button} from "@heroui/button";
import React, {useState} from "react";

import {
    DashboardIcon,
    HomeIcon,
    PostDetailsIcon, QuestionsListIcon, QuizzesListIcon,
    RegistrationListIcon,
    SliderIcon,
    SubjectsListIcon
} from "@/components/icons.tsx";
import {useAuth} from "@/hooks/useAuth.ts";
import {ROLE, Role, WEB_NAME} from "@/constants/general.constant.ts";

interface MenuItems {
    key: string,
    label: string,
    icon: React.ReactNode,
    roles: Role[] | undefined
}

export const menuItems: MenuItems[] = [
    {
        key: 'dashboard',
        label: 'Dashboard',
        icon: (<DashboardIcon height={20} width={20}/>),
        roles: undefined
    },
    {
        key: 'homepage',
        label: 'Home Page',
        icon: (<HomeIcon height={20} width={20}/>),
        roles: undefined
    },
    {
        key: 'postDetails',
        label: 'Post Details',
        icon: (<PostDetailsIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN, ROLE.MARKETING]
    },
    {
        key: 'slider',
        label: 'Slider',
        icon: (<SliderIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN, ROLE.MARKETING]
    },
    {
        key: 'subjectList',
        label: 'Subject List',
        icon: (<SubjectsListIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN]
    },
    {
        key: 'registrationList',
        label: 'Registration List',
        icon: (<RegistrationListIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN, ROLE.SALES]
    },
    {
        key: 'questionsList',
        label: 'Questions List',
        icon: (<QuestionsListIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN, ROLE.EXPERT]
    },
    {
        key: 'quizzesList',
        label: 'Quizzes List',
        icon: (<QuizzesListIcon height={20} width={20}/>),
        roles: [ROLE.ADMIN, ROLE.EXPERT]
    }
]

const LeftSidebar = () => {
    const {user} = useAuth();
    // state se chuyen theo key. Element nao co key tuong ung voi state se thay doi className
    const [active, setActive] = useState<string | null>(null);

    return (
        <nav className="h-screen w-16 sm:w-48 md:w-64
        bg-gray-900 text-gray-100 flex flex-col shadow-lg dark:bg-neutral-800"
             id="sidebarNav">
            <a className="px-4 py-6 flex items-center gap-2 text-xl font-bold text-white border-b border-gray-700 justify-center"
               href="/dashboard">
                <div
                    className="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-purple-600 shadow-md">
                    <span className="text-white text-lg sm:text-xl">📘</span>
                </div>
                {WEB_NAME}
            </a>

            <ul className="flex-1 flex flex-col gap-1 px-2 py-4">
                {menuItems.map((item) => {
                    const status = active === item.key; // Kiem tra xem active co la key hay khong

                    // Phai chuyen ve !item sau khi test xong
                    if (item.roles && item.roles.includes(user?.role.toLowerCase() as "admin" | "user" | "marketing" | "expert" | "sales")) {
                        return;
                    } else {
                        return (
                            <li key={item.key}>
                                <Button
                                    key={item.key}
                                    as={'a'}
                                    className={
                                        `flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-700 justify-start 
                                        ${status ? 'bg-gray-600 text-white/90 font-medium' : 'text-gray-300 hover:text-white'}`
                                    }
                                    href="#"
                                    variant={'light'}
                                    onPress={() => setActive(item.key)}
                                >
                                    {item.icon}
                                    {item.label}
                                </Button>
                            </li>
                        );
                    }
                })}
            </ul>
        </nav>
    );
};

export default LeftSidebar;

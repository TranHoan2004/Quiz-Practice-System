import React, {useState} from "react";
import {Button} from "@heroui/button";

import NavigationItems from "@/components/ui/NavigationItem";
import {useAuth} from "@/hooks/useAuth";
import {LogoutIcon} from "@/components/icons.tsx";
import {menuItems} from "@/components/layout/left-nav/LeftSidebar.tsx";

const color = 'bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600'

const AdministrationHeader = () => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [darkMode, setDarkMode] = useState(false);
    const {user, logout} = useAuth();
    const [active, setActive] = useState<string | null>(null);

    /* Can phai sua lai phan avatarDescription */
    return (
        <NavigationItems
            avatarDescription={'Admin'}
            darkMode={darkMode}
            isMenuOpen={isMenuOpen}
            leftNavigation={undefined}
            navigation={undefined}
            navigationForMobile={
                <div className="flex flex-col gap-4">
                    <ul className="flex flex-col gap-2">
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
                                                `flex items-center gap-3 px-3 py-2 rounded-lg justify-start dark:hover:bg-gray-800
                                                ${status ? `bg-gradient-to-r ${color} text-white font-medium` : 'text-black-300 '}`
                                            }
                                            href="#"
                                            variant={'light'}
                                            onPress={() => {
                                                setActive(item.key)
                                                setIsMenuOpen(false)
                                            }}
                                        >
                                            {item.label}
                                        </Button>
                                    </li>
                                );
                            }
                        })}
                    </ul>

                    <div className="flex flex-col gap-3 mt-6 border-gray-200/50 dark:bg-black">
                        <Button
                            className={`rounded-xl bg-gradient-to-r ${color}
                                text-white font-semibold shadow-md w-full py-3`}
                            onPress={() => {
                                setIsMenuOpen(false)
                                logout()
                            }}
                        >
                            <LogoutIcon height={20} width={20}/>
                            Logout
                        </Button>
                    </div>
                </div>
            }
            setDarkMode={setDarkMode}
            setIsMenuOpen={setIsMenuOpen}
            user={user}
        />
    );
};

export default AdministrationHeader;

import {
    Navbar,
    NavbarContent,
    NavbarMenuToggle,
    NavbarMenu,
    NavbarItem,
    Button,
} from "@heroui/react";
import {Settings} from "lucide-react";
import React from "react";

import Avatar from "@/components/ui/Avatar.tsx";
import {MoonFilledIcon, SunFilledIcon} from "@/components/icons.tsx";
import NotificationDropdownItem from "@/pages/_main.notification/NotificationDropdownItem.tsx";
import {User} from "@/types/user";

type NavigationItemProps = {
    leftNavigation: React.ReactNode | undefined;
    navigation: React.ReactNode | undefined;
    navigationForMobile: React.ReactNode | undefined;
    isMenuOpen: boolean;
    setIsMenuOpen: React.Dispatch<React.SetStateAction<boolean>>;
    darkMode: boolean
    setDarkMode: React.Dispatch<React.SetStateAction<boolean>>;
    user: User | undefined;
    avatarDescription?: string;
}

const NavigationItems = ({
                             leftNavigation,
                             navigation,
                             navigationForMobile,
                             isMenuOpen, setIsMenuOpen,
                             darkMode, setDarkMode,
                             user,
                             avatarDescription,
                         }: NavigationItemProps) => {
    const toggleTheme = () => {
        setDarkMode((prev: boolean) => !prev);
        if (!darkMode) {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
    };

    return (
        <Navbar
            className="bg-white/30 backdrop-blur-md shadow-lg rounded-xl py-2 px-4 mx-auto"
            classNames={{
                wrapper: "px-4 sm:px-6 lg:px-8 mx-auto max-w-full",
            }}
            isMenuOpen={isMenuOpen}
            maxWidth="full"
            onMenuOpenChange={setIsMenuOpen}
        >
            {/* Mobile Menu Toggle */}
            <NavbarContent className="sm:hidden" justify="start">
                <NavbarMenuToggle
                    aria-label={isMenuOpen ? "Close menu" : "Open menu"}
                    className="text-gray-700 dark:text-white"
                />
            </NavbarContent>

            {leftNavigation}

            {navigation}

            {/* Right buttons */}
            <NavbarContent className="gap-3 ml-4" justify="end">
                {/* Điều kiện của user đang để ngược lại nhằm phục vụ test */}
                {!user ? (
                    <>
                        {/* Notification */}
                        <NotificationDropdownItem/>

                        {/* Settings */}
                        <NavbarItem>
                            <Button
                                isIconOnly
                                className="rounded-full hover:bg-blue-50 dark:hover:bg-gray-700
                                    transition dark:border-white"
                                variant="bordered"
                            >
                                <Settings className="w-5 h-5 text-gray-700 dark:text-gray-200"/>
                            </Button>
                        </NavbarItem>

                        {/* Dark/Light Toggle */}
                        <NavbarItem>
                            <Button
                                isIconOnly
                                className="rounded-full hover:bg-blue-50 dark:hover:bg-gray-700 dark:border-white transition"
                                variant="bordered"
                                onPress={toggleTheme}
                            >
                                {darkMode ? (
                                    <SunFilledIcon className="w-5 h-5 text-yellow-400"/>
                                ) : (
                                    <MoonFilledIcon className="w-5 h-5 text-gray-700 dark:text-gray-200"/>
                                )}
                            </Button>
                        </NavbarItem>

                        {/* User Avatar */}
                        <Avatar description={avatarDescription}/>
                    </>
                ) : (
                    <>
                        {/* Login button */}
                        <NavbarItem className="hidden sm:flex">
                            <Button
                                as="a"
                                className="rounded-xl border-gray-400 text-gray-700 hover:border-blue-500
                hover:text-blue-600 transition-all duration-300 px-4 py-2
                dark:text-white"
                                href="/login"
                                size="sm"
                                variant="bordered"
                            >
                                Login
                            </Button>
                        </NavbarItem>

                        {/* Register button */}
                        <NavbarItem>
                            <Button
                                as="a"
                                className="rounded-xl bg-gradient-to-r from-blue-600 via-indigo-600
                to-purple-600 text-white font-semibold shadow-md hover:shadow-xl hover:scale-[1.05] transition-all duration-300 px-4 py-2"
                                href="/register"
                                size="sm"
                            >
                                Register
                            </Button>
                        </NavbarItem>
                    </>
                )}
            </NavbarContent>

            {/* Mobile Menu */}
            <NavbarMenu
                className="bg-white/95 backdrop-blur-md border-t
                    Sborder-gray-200/50 pt-6 dark:bg-black"
            >
                {navigationForMobile}
            </NavbarMenu>
        </Navbar>
    );
};

export default NavigationItems;

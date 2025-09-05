import {Button} from "@heroui/button";
import {Dropdown, DropdownItem, DropdownMenu, DropdownTrigger} from "@heroui/dropdown";
import {NavbarContent, NavbarItem, NavbarMenuItem} from "@heroui/navbar";
import {Link} from "react-router-dom";
import {useState} from "react";

import NavigationItems from "@/components/ui/NavigationItem";
import {useAuth} from "@/hooks/useAuth";
import {LogoutIcon} from "@/components/icons.tsx";
import BrandLogo from "@/components/ui/BrandLogo.tsx";

const PublicHeader = () => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [darkMode, setDarkMode] = useState(false);
    const {user, logout} = useAuth();

    return (
        <NavigationItems
            darkMode={darkMode}
            isMenuOpen={isMenuOpen}
            leftNavigation={<BrandLogo/>}
            navigation={
                <NavbarContent className="hidden sm:flex flex-grow" justify="center">
                    {/* Desktop - Full navigation (lg và trở lên) */}
                    <div className="hidden lg:flex items-center gap-8 xl:gap-12">
                        <NavbarItem className="px-2">
                            <Link
                                className="text-gray-700 hover:text-blue-600 font-medium
                                    transition-all duration-300 hover:scale-105 py-2
                                    dark:text-white"
                                to="/"
                            >
                                Home
                            </Link>
                        </NavbarItem>
                        <NavbarItem className="px-2">
                            <Link
                                className="text-gray-700 hover:text-blue-600 font-medium
                                    transition-all duration-300 hover:scale-105 py-2
                                    dark:text-white"
                                to="/courses"
                            >
                                Courses
                            </Link>
                        </NavbarItem>
                        <NavbarItem className="px-2">
                            <Link
                                className="text-gray-700 hover:text-blue-600 font-medium
                                    transition-all duration-300 hover:scale-105 py-2
                                    dark:text-white"
                                to="/contact"
                            >
                                Contact
                            </Link>
                        </NavbarItem>

                        {/* Dropdown cho About và FAQ */}
                        <Dropdown>
                            <NavbarItem className="px-2">
                                <DropdownTrigger>
                                    <Button
                                        disableRipple
                                        className="p-2 bg-transparent text-gray-700 hover:text-blue-600
                                            font-medium transition-all duration-300 data-[hover=true]:bg-transparent
                                            dark:text-white"
                                        variant="light"
                                    >
                                        More ▼
                                    </Button>
                                </DropdownTrigger>
                            </NavbarItem>
                            <DropdownMenu
                                aria-label="More menu"
                                className="w-[200px] bg-white/70 backdrop-blur-md shadow-lg rounded-xl dark:bg-black/70"
                                itemClasses={{
                                    base: "gap-2 font-medium text-gray-700 rounded-lg",
                                }}
                            >
                                <DropdownItem key="about">
                                    <Link className="w-full block dark:text-white" to="/about">
                                        About
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="faq">
                                    <Link className="w-full block dark:text-white" to="/faq">
                                        FAQ
                                    </Link>
                                </DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </div>

                    {/* Tablet - Gom Home, Courses vào More (md đến lg) */}
                    <div className="hidden md:flex lg:hidden items-center gap-4">
                        <NavbarItem className="">
                            <Link
                                className="text-gray-700 hover:text-blue-600 font-medium
                                    transition-all duration-300 hover:scale-105 py-2
                                    dark:text-white"
                                to="/"
                            >
                                Home
                            </Link>
                        </NavbarItem>

                        <Dropdown>
                            <NavbarItem className="px-2">
                                <DropdownTrigger>
                                    <Button
                                        disableRipple
                                        className="p-2 bg-transparent text-gray-700 hover:text-blue-600
                                            font-medium transition-all duration-300 data-[hover=true]:bg-transparent
                                            dark:text-white"
                                        variant="light"
                                    >
                                        More ▼
                                    </Button>
                                </DropdownTrigger>
                            </NavbarItem>
                            <DropdownMenu
                                aria-label="More menu"
                                className="w-[200px] bg-white/70 backdrop-blur-md shadow-lg rounded-xl dark:bg-black/70"
                                itemClasses={{
                                    base: "gap-2 font-medium text-gray-700 rounded-lg",
                                }}
                            >
                                <DropdownItem key="courses">
                                    <Link className="w-full block dark:text-white" to="/courses">
                                        Courses
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="contact">
                                    <Link className="w-full block dark:text-white" to="/contact">
                                        Contact
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="about">
                                    <Link className="w-full block dark:text-white" to="/about">
                                        About
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="faq">
                                    <Link className="w-full block dark:text-white" to="/faq">
                                        FAQ
                                    </Link>
                                </DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </div>

                    {/* Small tablet - Chỉ còn More dropdown (sm đến md) */}
                    <div className="flex md:hidden items-center">
                        <Dropdown>
                            <NavbarItem className="px-2">
                                <DropdownTrigger>
                                    <Button
                                        disableRipple
                                        className="p-2 bg-transparent text-gray-700 hover:text-blue-600
                                            font-medium transition-all duration-300 data-[hover=true]:bg-transparent
                                            dark:text-white"
                                        variant="light"
                                    >
                                        Menu ▼
                                    </Button>
                                </DropdownTrigger>
                            </NavbarItem>
                            <DropdownMenu
                                aria-label="Navigation menu"
                                className="w-[200px] bg-white/70 backdrop-blur-md shadow-lg rounded-xl dark:bg-black/70"
                                itemClasses={{
                                    base: "gap-2 font-medium text-gray-700 rounded-lg",
                                }}
                            >
                                <DropdownItem key="home">
                                    <Link className="w-full block dark:text-white" to="/">
                                        Home
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="courses">
                                    <Link className="w-full block dark:text-white" to="/courses">
                                        Courses
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="contact">
                                    <Link className="w-full block dark:text-white" to="/contact">
                                        Contact
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="about">
                                    <Link className="w-full block dark:text-white" to="/about">
                                        About
                                    </Link>
                                </DropdownItem>
                                <DropdownItem key="faq">
                                    <Link className="w-full block dark:text-white" to="/faq">
                                        FAQ
                                    </Link>
                                </DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </div>
                </NavbarContent>
            }
            navigationForMobile={
                <div className="flex flex-col gap-4">
                    <NavbarMenuItem>
                        <Link
                            className="text-gray-700 hover:text-blue-600 font-medium text-lg
                                transition-all duration-300 block py-3 px-2 rounded-lg hover:bg-blue-50/30
                                dark:text-white"
                            to="/"
                            onClick={() => setIsMenuOpen(false)}
                        >
                            Home
                        </Link>
                    </NavbarMenuItem>

                    <NavbarMenuItem>
                        <Link
                            className="text-gray-700 hover:text-blue-600 font-medium text-lg
                                transition-all duration-300 block py-3 px-2 rounded-lg hover:bg-blue-50/30
                                dark:text-white"
                            to="/courses"
                            onClick={() => setIsMenuOpen(false)}
                        >
                            Courses
                        </Link>
                    </NavbarMenuItem>

                    <NavbarMenuItem>
                        <Link
                            className="text-gray-700 hover:text-blue-600 font-medium text-lg
                                transition-all duration-300 block py-3 px-2 rounded-lg hover:bg-blue-50/30
                                dark:text-white"
                            to="/contact"
                            onClick={() => setIsMenuOpen(false)}
                        >
                            Contact
                        </Link>
                    </NavbarMenuItem>

                    <NavbarMenuItem>
                        <Link
                            className="text-gray-700 hover:text-blue-600 font-medium text-lg
                                transition-all duration-300 block py-3 px-2 rounded-lg hover:bg-blue-50/30
                                dark:text-white"
                            to="/about"
                            onClick={() => setIsMenuOpen(false)}
                        >
                            About
                        </Link>
                    </NavbarMenuItem>

                    <NavbarMenuItem>
                        <Link
                            className="text-gray-700 hover:text-blue-600 font-medium text-lg
                                transition-all duration-300 block py-3 px-2 rounded-lg hover:bg-blue-50/30
                                dark:text-white"
                            to="/faq"
                            onClick={() => setIsMenuOpen(false)}
                        >
                            FAQ
                        </Link>
                    </NavbarMenuItem>

                    {/* Mobile Auth Buttons */}
                    {user ? (
                        <div className="flex flex-col gap-3 mt-6 pt-6 border-t border-gray-200/50 dark:bg-black">
                            <Button
                                as="a"
                                className="
                                rounded-xl border-gray-400 text-gray-700 hover:border-blue-500
                                hover:text-blue-600 font-medium w-full py-3 dark:text-white"
                                href="/login"
                                variant="bordered"
                                onPress={() => setIsMenuOpen(false)}
                            >
                                Login
                            </Button>
                            <Button
                                as="a"
                                className="rounded-xl bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 
                                text-white font-semibold shadow-md w-full py-3"
                                href="/register"
                                onPress={() => setIsMenuOpen(false)}
                            >
                                Register
                            </Button>
                        </div>
                    ) : (
                        <div className="flex flex-col gap-3 mt-6 pt-6 border-t border-gray-200/50 dark:bg-black">
                            <Button
                                className="rounded-xl bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600
                                text-white font-semibold shadow-md w-full py-3"
                                variant="bordered"
                                onPress={() => {
                                    setIsMenuOpen(false);
                                    logout();
                                }}
                            >
                                <LogoutIcon height={20} width={20}/>
                                Logout
                            </Button>
                        </div>
                    )}
                </div>
            }
            setDarkMode={setDarkMode}
            setIsMenuOpen={setIsMenuOpen}
            user={user}
        />
    );
};

export default PublicHeader;
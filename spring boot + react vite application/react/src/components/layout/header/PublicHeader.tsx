import {
  Navbar,
  NavbarContent,
  NavbarMenuToggle,
  NavbarMenu,
  NavbarMenuItem,
  NavbarItem,
  Button,
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from "@heroui/react";
import { Link } from "react-router-dom";
import { useState } from "react";
import { Settings } from "lucide-react";

import BrandLogo from "@/components/ui/BrandLogo.tsx";
import { useAuth } from "@/hooks/useAuth.ts";
import Avatar from "@/components/ui/Avatar.tsx";
import { MoonFilledIcon, SunFilledIcon } from "@/components/icons.tsx";
import NotificationDropdownItem from "@/pages/_main.notification/NotificationDropdownItem.tsx";

const PublicHeader = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [darkMode, setDarkMode] = useState(false);
  const { user } = useAuth();

  const toggleTheme = () => {
    setDarkMode((prev) => !prev);
    if (!darkMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  };

  return (
    <Navbar
      className="bg-white/30 backdrop-blur-md border-b border-white/20 shadow-lg rounded-xl py-2 px-4 mx-auto"
      classNames={{
        wrapper: "px-4 sm:px-6 lg:px-8 mx-auto",
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

      <BrandLogo />

      {/* Navigation */}
      <NavbarContent className="hidden sm:flex flex-grow" justify="center">
        <div className="flex items-center gap-8 lg:gap-12">
          <NavbarItem className="px-2">
            <Link
              className="text-gray-700 hover:text-blue-600 font-medium
                transition-all duration-300 hover:scale-105 py-2
              dark:text-white
              "
              to="/"
            >
              Home
            </Link>
          </NavbarItem>
          <NavbarItem className="px-2">
            <Link
              className="text-gray-700 hover:text-blue-600 font-medium
                transition-all duration-300 hover:scale-105 py-2
              dark:text-white
              "
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

          {/* Dropdown */}
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
      </NavbarContent>

      {/* Right buttons */}
      <NavbarContent className="gap-3 ml-4" justify="end">
        {!user ? (
          <>
            {/* Notification */}
            <NotificationDropdownItem />

            {/* Settings */}
            <NavbarItem>
              <Button
                isIconOnly
                className="rounded-full hover:bg-blue-50 dark:hover:bg-gray-700
                    transition dark:border-white"
                variant="bordered"
              >
                <Settings className="w-5 h-5 text-gray-700 dark:text-gray-200" />
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
                  <SunFilledIcon className="w-5 h-5 text-yellow-400" />
                ) : (
                  <MoonFilledIcon className="w-5 h-5 text-gray-700 dark:text-gray-200" />
                )}
              </Button>
            </NavbarItem>

            {/* User Avatar */}
            <Avatar />
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
      border-gray-200/50 pt-6 dark:bg-black"
      >
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
              className="rounded-xl bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 text-white font-semibold shadow-md w-full py-3"
              href="/register"
              onPress={() => setIsMenuOpen(false)}
            >
              Register
            </Button>
          </div>
        </div>
      </NavbarMenu>
    </Navbar>
  );
};

export default PublicHeader;

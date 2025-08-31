import { NavbarBrand, NavbarContent } from "@heroui/react";

const BrandLogo = () => {
  return (
    <NavbarContent className="sm:flex-grow-0" justify="start">
      <NavbarBrand className="gap-2">
        <div className="w-8 h-8 sm:w-10 sm:h-10 flex items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-purple-600 shadow-md">
          <span className="text-white text-lg sm:text-xl">📘</span>
        </div>
        <p
          className="
          font-bold text-base sm:text-lg bg-gradient-to-r
        from-blue-600 to-purple-600 bg-clip-text text-transparent
        dark:from-blue-400 dark:to-purple-400
        ">
          Quezee
        </p>
      </NavbarBrand>
    </NavbarContent>
  );
};

export default BrandLogo;

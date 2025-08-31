import { HeroUIProvider } from "@heroui/system";
import { useHref, useNavigate } from "react-router-dom";
import { ToastProvider } from "@heroui/toast";

export function Provider({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate();

  return (
    <HeroUIProvider navigate={navigate} useHref={useHref}>
      <ToastProvider
        maxVisibleToasts={5}
        placement="bottom-right"
        toastProps={{ radius: "sm", timeout: 5000 }}
      />
      {children}
    </HeroUIProvider>
  );
}

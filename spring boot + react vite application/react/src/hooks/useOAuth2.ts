import { useLocation, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { addToast } from "@heroui/toast";

import { useAuth } from "./useAuth";

export const useOAuth2 = () => {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (user?.accessToken) {
      return; // Skip if already logged in
    }

    const params = new URLSearchParams(location.search);
    const encodedData = params.get("data");

    if (encodedData) {
      try {
        const jsonString = atob(encodedData);
        const data = JSON.parse(jsonString);

        login(data);
        navigate("/", { replace: true });
      } catch (e) {
        addToast({
          title: "Error",
          description: "Failed to parse authentication data.",
          closeIcon: true,
          variant: "flat",
        });
      }
    }
  }, [location, login, navigate, user]);
};

import { useLocation, useNavigate } from "react-router";
import { useEffect } from "react";
import { useAuth } from "./useAuth";

export const useOAuth2 = () => {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (user?.accessToken) {
        console.log(user)
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
        console.error(e);
      }
    }
  }, [location, login, navigate, user]);
};

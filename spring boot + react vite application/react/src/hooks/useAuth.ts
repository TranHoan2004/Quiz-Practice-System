import { useContext } from "react";

import AuthContext from "../contexts/AuthContext";

import { AuthContextType } from "@/types/user";

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);

  if (!context) throw new Error("useAuth must be used within useAuth");

  return context;
};

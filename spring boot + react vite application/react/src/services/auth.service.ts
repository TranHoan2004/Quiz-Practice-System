import { addToast } from "@heroui/toast";

export const traditionalLogin = async (email: string, password: string) => {
  const apiUrl = `${import.meta.env.VITE_API_URL}/auth/login`;

  try {
    const res = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    });

    return await res.json();
  } catch (e) {
    addToast({
      title: "Login failed",
      description:
        "An error occurred while trying to log in. Please try again.",
      color: "danger",
      variant: "flat",
      closeIcon: true,
    });
  }
};

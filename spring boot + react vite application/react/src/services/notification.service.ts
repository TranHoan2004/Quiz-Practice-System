import { addToast } from "@heroui/toast";

const api = import.meta.env.VITE_API_URL;

export const getNotifications = async (accessToken: string, page?: number) => {
  const apiUrl = `${api}/notification/${page ? `?page=${page}` : ""}`;

  try {
    const res = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify({ token: accessToken }),
    });

    if (!res.ok) {
      throw new Error("Failed to get notification");
    }

    return await res.json();
  } catch (e) {
    addToast({
      title: "Notification Error",
      description:
        "An error occurred while trying to get notifications. Please try again.\n" +
        e,
      color: "danger",
      variant: "flat",
      closeIcon: true,
    });
  }
};

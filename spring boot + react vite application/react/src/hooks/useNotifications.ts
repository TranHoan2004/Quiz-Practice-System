import { addToast } from "@heroui/toast";

const api = import.meta.env.VITE_API_URL;

export const useMarkNotifications = async (
  notificationId: string,
  accessToken: string,
) => {
  try {
    const response = await fetch(`${api}/notification/read`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify({ notificationId, accessToken }),
    });

    if (!response.ok) {
      throw new Error("Failed to mark notification as read");
    }
  } catch (error: any) {
    addToast({
      title: "Marking failed",
      description: error.message,
    });
  }
};

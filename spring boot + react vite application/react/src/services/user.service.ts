import {addToast} from "@heroui/toast";

const api = import.meta.env.VITE_API_URL;

export const getUsers = async (
    // token: string,
    page: number,
    size: number,
    options?: { role?: string; status?: string; search?: string }
) => {
    try {
        const query = new URLSearchParams({
            page: String(page),
            size: String(size),
            ...(options?.role ? { role: options.role } : {}),
            ...(options?.status ? { status: options.status } : {}),
            ...(options?.search ? { search: options.search } : {}),
        });

        const response = await fetch(`${api}/user/all?${query.toString()}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                // Authorization: `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        return await response.json();
    } catch (e: Error | any) {
        console.error("Error fetching users:", e);
        addToast({
            title: "Error fetching users",
            description: e.message,
            color: "danger",
            closeIcon: true,
            variant: "flat",
        });
    }
};

export const createUser = async (
    // token: string,
    params: {
        name: string,
        email: string,
        role: string,
        phoneNumber: string,
    }
) => {
    try {
        const response = await fetch(`${api}/user/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                // Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(params)
        });

        if (response.ok) {
            addToast({
                title: "Create user successful",
                color: "success",
                closeIcon: true,
                variant: "flat",
            })
            setTimeout(() => {
                window.location.reload();
            }, 3000)
        }

    } catch (e: Error | any) {
        console.error("Error creating users:", e);
        addToast({
            title: "Error when creating users",
            description: e.message,
            color: "danger",
            closeIcon: true,
            variant: "flat",
        });
    }
}

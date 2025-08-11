import { createContext, useState, useEffect, type ReactNode, useRef } from "react";
import { useNavigate } from "react-router";

interface User {
    id: string,
    fullName: string,
    gender: string,
    phoneNumber: string,
    avatarUrl: string,
    username: string,
    accessToken: string,
    refreshToken: string,
    expiration: number,
    refreshExpiration?: number
}

interface AuthContextType {
    user?: User;
    loading: boolean;
    login: (data: User) => void;
    logout: () => void;
    updateUser: (data: Partial<User>) => void
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType>({
    user: undefined,
    loading: true,
    login: () => { },
    logout: () => { },
    updateUser: () => { },
    isAuthenticated: false
});

const EmptyUser: User = {
    id: "",
    fullName: "",
    gender: "",
    phoneNumber: "",
    avatarUrl: "",
    username: "",
    accessToken: "",
    refreshToken: "",
    expiration: 0,
    refreshExpiration: 0
}

const apiUrl = `${import.meta.env.VITE_API_URL}/auth/refresh-token`;

const isAccessTokenValid = (user: User) => {
    return user.accessToken && user.expiration > Date.now()
}

const isRefreshTokenValid = (user: User) => {
    return !!user.refreshExpiration && user.refreshExpiration > Date.now()
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState(() => {
        const saved = localStorage.getItem("auth");
        return saved ? JSON.parse(saved) : { ...EmptyUser };
    })
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const isAuthenticated = isRefreshTokenValid(user)

    // ngăn gửi thông tin 2 lần về backend
    const isMounted = useRef(true);
    const initialized = useRef(false);
    useEffect(() => {

        const initialize = async () => {
            if (initialized.current) return;
            initialized.current = true;

            if (isAccessTokenValid(user)) {
                setUser(user)
            } else if (isRefreshTokenValid(user)) {
                const saved = localStorage.getItem("auth");
                if (!saved) return

                try {
                    console.log("Go here")
                    const res = await fetch(apiUrl, {
                        method: "POST",
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${user.refreshToken}`
                        },
                        body: JSON.stringify({
                            refreshToken: user.refreshToken
                        })
                    })
                    const data = await res.json();

                    const savedUser = JSON.parse(saved);
                    const updatedUser = {
                        ...savedUser,
                        accessToken: data.accessToken,
                        refreshToken: data.refreshToken,
                        expiration: data.expiration,
                        refreshExpiration: data.refreshExpiration
                    }

                    localStorage.setItem("auth", JSON.stringify(updatedUser));
                    // setUser(updatedUser);
                    if (isMounted.current) setUser(updatedUser);
                } catch (e) {
                    console.error(e);
                    logout()
                }
            } else {
                logout()
            }
            setLoading(false);
        }
        initialize().then(r => {
            console.log(r)
        })

        return () => { isMounted.current = false; }
    }, [])

    const login = (data: User) => {
        localStorage.setItem("auth", JSON.stringify(data));
        setUser(data);
    }

    const logout = () => {
        localStorage.removeItem("auth");
        setUser(EmptyUser);
        navigate('/signin')
    }

    const updateUser = (data: Partial<User>) => {
        const saved = localStorage.getItem("auth");
        if (!saved) return;

        const user = JSON.parse(saved);
        const updatedUser = {
            ...user,
            ...data
        };
        localStorage.setItem("auth", JSON.stringify(updatedUser));
        setUser(updatedUser);
    }

    return (
        <AuthContext.Provider value={{ user, loading, login, logout, updateUser, isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContext;
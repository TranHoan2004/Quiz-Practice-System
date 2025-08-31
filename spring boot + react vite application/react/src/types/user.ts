export interface User {
  id: string;
  fullName: string;
  gender: string;
  phoneNumber: string;
  avatarUrl: string;
  username: string;
  accessToken: string;
  refreshToken: string;
  expiration: number;
  refreshExpiration?: number;
}

export interface AuthContextType {
  user?: User;
  loading: boolean;
  login: (data: User) => void;
  logout: () => void;
  updateUser: (data: Partial<User>) => void;
  isAuthenticated: boolean;
}

export interface User {
  id?: string;
  fullName: string;
  gender?: string;
  phoneNumber?: string;
  avatarUrl?: string;
  email: string;
  accessToken: string;
  refreshToken: string;
  expiration: number;
  refreshExpiration: number;
  role: string;
  createdDate?: string;
  dob?: string;
  status: boolean;
}

export interface AuthContextType {
  user: User | undefined;
  loading: boolean;
  login: (data: User) => void;
  logout: () => void;
  updateUser: (data: Partial<User>) => void;
  isAuthenticated: boolean;
}

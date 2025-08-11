import { z } from "zod";
import {
  PASSWORD_MIN_LENGTH,
  REGEX,
  SYSTEM_MESSAGE,
} from "../../constants/general.constant.ts";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@heroui/react";
import { useNavigate } from "react-router";
import { useOAuth2 } from "../../hooks/useOAuth2.ts";
import { traditionalLogin } from "../../services/auth.service.ts";
import { useAuth } from "../../hooks/useAuth.ts";
import { useState } from "react";
// import { useEffect, useState } from "react";

// Create schema for validation
const schema = z.object({
  email: z
    .string()
    .min(1, { message: "Email is required" })
    .email({ message: SYSTEM_MESSAGE.EMAIL_REGEX_MESSAGE }),
  password: z
    .string()
    .min(PASSWORD_MIN_LENGTH, {
      message: SYSTEM_MESSAGE.PASSWORD_MIN_LENGTH_MESSAGE,
    })
    .regex(REGEX.PASSWORD_REGEX, {
      message: SYSTEM_MESSAGE.PASSWORD_REGEX_MESSAGE,
    }),
});

const SignInForm = () => {
  // const [rememberMe, setRememberMe] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const { control, handleSubmit } = useForm({
    defaultValues: {
      email: "",
      password: "",
    },
    resolver: zodResolver(schema),
  });
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const onSubmit = async (data: z.infer<typeof schema>) => {
    const email = data.email;
    const password = data.password;
    const response = await traditionalLogin(email, password);
    console.log(response)
    if (response) {
      if (response.status === 500) {
        setErrorMessage('Wrong email or password')
      } else {
        login(response);
        navigate("/");
      }
    }
  };

  useOAuth2();

  const HandleLoginByGoogle = () => {
    const link = import.meta.env.VITE_GOOGLE_AUTHORIZATION_LINK;
    window.location.href = link;
  };

  // const handleRememberMe = () => {

  // }

  return (
    <>
      <form className="w-full space-y-6" onSubmit={handleSubmit(onSubmit)}>
        {/*Email*/}
        <div className="w-full">
          <Controller
            control={control}
            name="email"
            render={({
              field: { name, value, onChange, onBlur, ref },
              fieldState: { invalid, error },
            }) => (
              <div className="relative w-full">
                <input
                  id="email"
                  name={name}
                  value={value}
                  ref={ref}
                  onChange={onChange}
                  onBlur={onBlur}
                  type="email"
                  placeholder=" "
                  className={
                    `peer h-14 w-full px-4 pt-6 text-base rounded-xl bg-white/60 backdrop-blur-sm border border-gray-200/50 hover:bg-white/80 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-300 
                    ${invalid ? "border-red-500" : ""}`
                  }
                  required
                />
                <label
                  htmlFor="email"
                  className="absolute left-4 top-4 text-gray-700 text-sm font-semibold transition-all duration-300 peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-2 peer-focus:text-sm peer-focus:text-gray-700"
                >
                  Email
                </label>
                {error && (
                  <p className="mt-1 text-sm text-red-500">{error.message}</p>
                )}
              </div>
            )}
          />
        </div>

        {/*Password*/}
        <div className="w-full">
          <Controller
            control={control}
            name="password"
            render={({
              field: { name, value, onChange, onBlur, ref },
              fieldState: { invalid, error },
            }) => (
              <div className="relative w-full">
                <input
                  id="password"
                  name={name}
                  value={value}
                  ref={ref}
                  onChange={onChange}
                  onBlur={onBlur}
                  type="password"
                  placeholder=" "
                  className={`peer h-14 w-full px-4 pt-6 text-base rounded-xl bg-white/60 backdrop-blur-sm border border-gray-200/50 hover:bg-white/80 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-300 ${invalid ? "border-red-500" : ""
                    }`}
                  required
                />
                <label
                  htmlFor="password"
                  className="absolute left-4 top-4 text-gray-700 text-sm font-semibold transition-all duration-300 peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-2 peer-focus:text-sm peer-focus:text-gray-700"
                >
                  Password
                </label>
                {error && (
                  <p className="mt-1 text-sm text-red-500">{error.message}</p>
                )}
                {errorMessage && (
                  <p className="mt-1 text-sm text-red-500">{errorMessage}</p>
                )}
              </div>
            )}
          />
        </div>

        {/* <div className="flex gap-2">
          <Checkbox
            id="rememberMe"
            size="lg"
            onChange={(e) => setRememberMe(e.target.checked)}
          />
          <label htmlFor="rememberMe" className="text-gray-700 text-sm font-medium">Remember me</label>
        </div> */}

        {/* Sign In Button */}
        <div className="w-full">
          <Button
            color="primary"
            type="submit"
            fullWidth
            className="h-14 text-base font-bold bg-gradient-to-r from-blue-600 via-blue-700 to-purple-600 hover:from-blue-700 hover:via-blue-800 hover:to-purple-700 text-white rounded-xl shadow-lg hover:shadow-xl transform hover:scale-[1.02] transition-all duration-300"
          >
            Sign In
          </Button>
        </div>
      </form>

      <div className="mt-8">
        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300/50"></div>
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-4 bg-white/80 backdrop-blur-sm text-gray-500 font-medium">
              Or continue with
            </span>
          </div>
        </div>

        <div className="w-full mt-6">
          <Button
            onPress={HandleLoginByGoogle}
            className="w-full h-14 text-base font-semibold bg-white/80 backdrop-blur-sm border border-gray-300/50 text-gray-700 rounded-xl hover:bg-white hover:border-gray-400 hover:shadow-lg transform hover:scale-[1.02] transition-all duration-300 flex items-center justify-center"
          >
            <svg className="w-6 h-6 mr-3 flex-shrink-0" viewBox="0 0 24 24">
              <path
                fill="#4285F4"
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
              />
              <path
                fill="#34A853"
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
              />
              <path
                fill="#FBBC05"
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
              />
              <path
                fill="#EA4335"
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
              />
            </svg>
            <span>Login with Google</span>
          </Button>
        </div>
      </div>
    </>
  );
};

export default SignInForm;

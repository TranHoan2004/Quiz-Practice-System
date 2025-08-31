import { z } from "zod";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";
import { addToast } from "@heroui/toast";
import { useState } from "react";
import { Button } from "@heroui/button";

import { traditionalLogin } from "@/services/auth.service.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import {
  PASSWORD_MIN_LENGTH,
  REGEX,
  SYSTEM_MESSAGE,
} from "@/constants/general.constant.ts";
import { useOAuth2 } from "@/hooks/useOAuth2.ts";
import { GoogleIcon } from "@/components/icons.tsx";

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

    if (response) {
      if (response.status === 500) {
        setErrorMessage("Wrong email or password");
      } else {
        addToast({
          title: "Login Successful",
          description: "You have successfully logged in.",
          closeIcon: true,
          variant: "flat",
        });
        setTimeout(() => {
          login(response);
          navigate("/");
        }, 3000);
      }
    }
  };

  useOAuth2();

  const HandleLoginByGoogle = () => {
    window.location.href = import.meta.env.VITE_GOOGLE_AUTHORIZATION_LINK;
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
                  ref={ref}
                  required
                  className={`peer h-14 w-full px-4 pt-6 text-base rounded-xl bg-white/60 backdrop-blur-sm border border-gray-200/50 hover:bg-white/80 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-300 
                  ${invalid ? "border-red-500" : ""}`}
                  id="email"
                  name={name}
                  placeholder=" "
                  type="email"
                  value={value}
                  onBlur={onBlur}
                  onChange={onChange}
                />
                <label
                  className="absolute left-4 top-4 text-gray-700 text-sm font-semibold transition-all duration-300 peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-2 peer-focus:text-sm peer-focus:text-gray-700"
                  htmlFor="email"
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
                  ref={ref}
                  required
                  className={`peer h-14 w-full px-4 pt-6 text-base rounded-xl bg-white/60 backdrop-blur-sm border border-gray-200/50 hover:bg-white/80 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-300 
                                ${invalid ? "border-red-500" : ""}`}
                  id="password"
                  name={name}
                  placeholder=" "
                  type="password"
                  value={value}
                  onBlur={onBlur}
                  onChange={onChange}
                  onClick={() => setErrorMessage("")}
                />
                <label
                  className="absolute left-4 top-4 text-gray-700 text-sm font-semibold transition-all duration-300 peer-placeholder-shown:top-4 peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-500 peer-focus:top-2 peer-focus:text-sm peer-focus:text-gray-700"
                  htmlFor="password"
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
            fullWidth
            className="h-14 text-base font-bold bg-gradient-to-r from-blue-600 via-blue-700 to-purple-600 hover:from-blue-700 hover:via-blue-800 hover:to-purple-700 text-white rounded-xl shadow-lg hover:shadow-xl transform hover:scale-[1.02] transition-all duration-300"
            color="primary"
            type="submit"
          >
            Sign In
          </Button>
        </div>
      </form>
      <div className="mt-8">
        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300/50" />
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-4 bg-white/80 backdrop-blur-sm text-gray-500 font-medium">
              Or continue with
            </span>
          </div>
        </div>

        <div className="w-full mt-6">
          <Button
            className="w-full h-14 text-base font-semibold bg-white/80 backdrop-blur-sm border border-gray-300/50 text-gray-700 rounded-xl hover:bg-white hover:border-gray-400 hover:shadow-lg transform hover:scale-[1.02] transition-all duration-300 flex items-center justify-center"
            onPress={HandleLoginByGoogle}
          >
            <GoogleIcon />
            <span>Login with Google</span>
          </Button>
        </div>
      </div>
    </>
  );
};

export default SignInForm;

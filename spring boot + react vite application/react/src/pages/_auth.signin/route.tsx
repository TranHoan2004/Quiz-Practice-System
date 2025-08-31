import { Link } from "react-router-dom";

import SignInForm from "./SignInForm.tsx";

import { LockIcon } from "@/components/icons.tsx";

const SignInPage = () => {
  return (
    <div className="w-full min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 relative overflow-hidden">
      {/* Animated background elements */}
      <div className="absolute inset-0">
        <div className="absolute top-0 left-0 w-72 h-72 bg-blue-400/10 rounded-full mix-blend-multiply filter blur-xl animate-pulse" />
        <div className="absolute top-0 right-0 w-72 h-72 bg-purple-400/10 rounded-full mix-blend-multiply filter blur-xl animate-pulse animation-delay-2000" />
        <div className="absolute -bottom-8 left-20 w-72 h-72 bg-pink-400/10 rounded-full mix-blend-multiply filter blur-xl animate-pulse animation-delay-4000" />
      </div>

      {/* Floating particles */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-bounce" />
        <div className="absolute top-1/3 right-1/3 w-1 h-1 bg-purple-400/40 rounded-full animate-bounce animation-delay-1000" />
        <div className="absolute bottom-1/4 left-1/3 w-1.5 h-1.5 bg-pink-400/30 rounded-full animate-bounce animation-delay-2000" />
        <div className="absolute top-2/3 right-1/4 w-1 h-1 bg-indigo-400/40 rounded-full animate-bounce animation-delay-3000" />
      </div>

      <div className="relative z-10 flex items-center justify-center w-full min-h-screen p-4">
        <div className="w-full flex justify-center">
          <div className="w-full sm:w-96 md:w-[450px] lg:w-[500px] xl:w-[520px] 2xl:w-[540px] mx-auto p-4 sm:p-6 md:p-8 bg-white/80 backdrop-blur-sm rounded-2xl shadow-2xl border border-white/20 relative overflow-hidden">
            {/* Background decoration */}
            <div className="absolute inset-0 bg-gradient-to-br from-blue-50/50 via-transparent to-purple-50/50" />
            <div className="absolute -top-4 -right-4 w-24 h-24 bg-gradient-to-br from-blue-400/20 to-purple-400/20 rounded-full blur-xl" />
            <div className="absolute -bottom-4 -left-4 w-32 h-32 bg-gradient-to-tr from-purple-400/20 to-pink-400/20 rounded-full blur-xl" />

            <div className="relative z-10">
              <div className="mb-8 text-center">
                <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl mx-auto mb-4 flex items-center justify-center shadow-lg">
                  <LockIcon color={"white"} size={32} />
                </div>
                <h2 className="text-3xl font-bold bg-gradient-to-r from-gray-900 to-gray-700 bg-clip-text text-transparent mb-2">
                  Welcome Back
                </h2>
                <p className="text-gray-600 text-lg">Sign in to your account</p>
              </div>

              <SignInForm />

              <div className="mt-8 flex flex-col items-center gap-2">
                <p className="text-sm text-gray-600">
                  Don&apos;t have an account?{" "}
                  <Link
                    className="font-semibold text-blue-600 hover:text-blue-500 transition-colors duration-200 border-b decoration-2 underline-offset-2"
                    to="/signup"
                  >
                    Sign up
                  </Link>
                </p>
                <Link
                  className="text-sm text-gray-600 hover:text-blue-600 transition-colors duration-200 font-medium hover:border-b decoration-2 underline-offset-2"
                  to="/forgot-password"
                >
                  Forgot your password?
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignInPage;

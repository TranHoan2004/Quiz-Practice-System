import * as React from "react";

import {IconSvgProps} from "@/types";

export const Logo: React.FC<IconSvgProps> = ({
                                                 size = 36,
                                                 height,
                                                 ...props
                                             }) => (
    <svg
        fill="none"
        height={size || height}
        viewBox="0 0 32 32"
        width={size || height}
        {...props}
    >
        <path
            clipRule="evenodd"
            d="M17.6482 10.1305L15.8785 7.02583L7.02979 22.5499H10.5278L17.6482 10.1305ZM19.8798 14.0457L18.11 17.1983L19.394 19.4511H16.8453L15.1056 22.5499H24.7272L19.8798 14.0457Z"
            fill="currentColor"
            fillRule="evenodd"
        />
    </svg>
);

export const DiscordIcon: React.FC<IconSvgProps> = ({
                                                        size = 24,
                                                        width,
                                                        height,
                                                        ...props
                                                    }) => {
    return (
        <svg
            height={size || height}
            viewBox="0 0 24 24"
            width={size || width}
            {...props}
        >
            <path
                d="M14.82 4.26a10.14 10.14 0 0 0-.53 1.1 14.66 14.66 0 0 0-4.58 0 10.14 10.14 0 0 0-.53-1.1 16 16 0 0 0-4.13 1.3 17.33 17.33 0 0 0-3 11.59 16.6 16.6 0 0 0 5.07 2.59A12.89 12.89 0 0 0 8.23 18a9.65 9.65 0 0 1-1.71-.83 3.39 3.39 0 0 0 .42-.33 11.66 11.66 0 0 0 10.12 0q.21.18.42.33a10.84 10.84 0 0 1-1.71.84 12.41 12.41 0 0 0 1.08 1.78 16.44 16.44 0 0 0 5.06-2.59 17.22 17.22 0 0 0-3-11.59 16.09 16.09 0 0 0-4.09-1.35zM8.68 14.81a1.94 1.94 0 0 1-1.8-2 1.93 1.93 0 0 1 1.8-2 1.93 1.93 0 0 1 1.8 2 1.93 1.93 0 0 1-1.8 2zm6.64 0a1.94 1.94 0 0 1-1.8-2 1.93 1.93 0 0 1 1.8-2 1.92 1.92 0 0 1 1.8 2 1.92 1.92 0 0 1-1.8 2z"
                fill="currentColor"
            />
        </svg>
    );
};

export const TwitterIcon: React.FC<IconSvgProps> = ({
                                                        size = 24,
                                                        width,
                                                        height,
                                                        ...props
                                                    }) => {
    return (
        <svg
            height={size || height}
            viewBox="0 0 24 24"
            width={size || width}
            {...props}
        >
            <path
                d="M19.633 7.997c.013.175.013.349.013.523 0 5.325-4.053 11.461-11.46 11.461-2.282 0-4.402-.661-6.186-1.809.324.037.636.05.973.05a8.07 8.07 0 0 0 5.001-1.721 4.036 4.036 0 0 1-3.767-2.793c.249.037.499.062.761.062.361 0 .724-.05 1.061-.137a4.027 4.027 0 0 1-3.23-3.953v-.05c.537.299 1.16.486 1.82.511a4.022 4.022 0 0 1-1.796-3.354c0-.748.199-1.434.548-2.032a11.457 11.457 0 0 0 8.306 4.215c-.062-.3-.1-.611-.1-.923a4.026 4.026 0 0 1 4.028-4.028c1.16 0 2.207.486 2.943 1.272a7.957 7.957 0 0 0 2.556-.973 4.02 4.02 0 0 1-1.771 2.22 8.073 8.073 0 0 0 2.319-.624 8.645 8.645 0 0 1-2.019 2.083z"
                fill="currentColor"
            />
        </svg>
    );
};

export const GithubIcon: React.FC<IconSvgProps> = ({
                                                       size = 24,
                                                       width,
                                                       height,
                                                       ...props
                                                   }) => {
    return (
        <svg
            height={size || height}
            viewBox="0 0 24 24"
            width={size || width}
            {...props}
        >
            <path
                clipRule="evenodd"
                d="M12.026 2c-5.509 0-9.974 4.465-9.974 9.974 0 4.406 2.857 8.145 6.821 9.465.499.09.679-.217.679-.481 0-.237-.008-.865-.011-1.696-2.775.602-3.361-1.338-3.361-1.338-.452-1.152-1.107-1.459-1.107-1.459-.905-.619.069-.605.069-.605 1.002.07 1.527 1.028 1.527 1.028.89 1.524 2.336 1.084 2.902.829.091-.645.351-1.085.635-1.334-2.214-.251-4.542-1.107-4.542-4.93 0-1.087.389-1.979 1.024-2.675-.101-.253-.446-1.268.099-2.64 0 0 .837-.269 2.742 1.021a9.582 9.582 0 0 1 2.496-.336 9.554 9.554 0 0 1 2.496.336c1.906-1.291 2.742-1.021 2.742-1.021.545 1.372.203 2.387.099 2.64.64.696 1.024 1.587 1.024 2.675 0 3.833-2.33 4.675-4.552 4.922.355.308.675.916.675 1.846 0 1.334-.012 2.41-.012 2.737 0 .267.178.577.687.479C19.146 20.115 22 16.379 22 11.974 22 6.465 17.535 2 12.026 2z"
                fill="currentColor"
                fillRule="evenodd"
            />
        </svg>
    );
};

export const MoonFilledIcon = ({
                                   size = 24,
                                   width,
                                   height,
                                   ...props
                               }: IconSvgProps) => (
    <svg
        aria-hidden="true"
        focusable="false"
        height={size || height}
        role="presentation"
        viewBox="0 0 24 24"
        width={size || width}
        {...props}
    >
        <path
            d="M21.53 15.93c-.16-.27-.61-.69-1.73-.49a8.46 8.46 0 01-1.88.13 8.409 8.409 0 01-5.91-2.82 8.068 8.068 0 01-1.44-8.66c.44-1.01.13-1.54-.09-1.76s-.77-.55-1.83-.11a10.318 10.318 0 00-6.32 10.21 10.475 10.475 0 007.04 8.99 10 10 0 002.89.55c.16.01.32.02.48.02a10.5 10.5 0 008.47-4.27c.67-.93.49-1.519.32-1.79z"
            fill="currentColor"
        />
    </svg>
);

export const SunFilledIcon = ({
                                  size = 24,
                                  width,
                                  height,
                                  ...props
                              }: IconSvgProps) => (
    <svg
        aria-hidden="true"
        focusable="false"
        height={size || height}
        role="presentation"
        viewBox="0 0 24 24"
        width={size || width}
        {...props}
    >
        <g fill="currentColor">
            <path d="M19 12a7 7 0 11-7-7 7 7 0 017 7z"/>
            <path
                d="M12 22.96a.969.969 0 01-1-.96v-.08a1 1 0 012 0 1.038 1.038 0 01-1 1.04zm7.14-2.82a1.024 1.024 0 01-.71-.29l-.13-.13a1 1 0 011.41-1.41l.13.13a1 1 0 010 1.41.984.984 0 01-.7.29zm-14.28 0a1.024 1.024 0 01-.71-.29 1 1 0 010-1.41l.13-.13a1 1 0 011.41 1.41l-.13.13a1 1 0 01-.7.29zM22 13h-.08a1 1 0 010-2 1.038 1.038 0 011.04 1 .969.969 0 01-.96 1zM2.08 13H2a1 1 0 010-2 1.038 1.038 0 011.04 1 .969.969 0 01-.96 1zm16.93-7.01a1.024 1.024 0 01-.71-.29 1 1 0 010-1.41l.13-.13a1 1 0 011.41 1.41l-.13.13a.984.984 0 01-.7.29zm-14.02 0a1.024 1.024 0 01-.71-.29l-.13-.14a1 1 0 011.41-1.41l.13.13a1 1 0 010 1.41.97.97 0 01-.7.3zM12 3.04a.969.969 0 01-1-.96V2a1 1 0 012 0 1.038 1.038 0 01-1 1.04z"/>
        </g>
    </svg>
);

export const HeartFilledIcon = ({
                                    size = 24,
                                    width,
                                    height,
                                    ...props
                                }: IconSvgProps) => (
    <svg
        aria-hidden="true"
        focusable="false"
        height={size || height}
        role="presentation"
        viewBox="0 0 24 24"
        width={size || width}
        {...props}
    >
        <path
            d="M12.62 20.81c-.34.12-.9.12-1.24 0C8.48 19.82 2 15.69 2 8.69 2 5.6 4.49 3.1 7.56 3.1c1.82 0 3.43.88 4.44 2.24a5.53 5.53 0 0 1 4.44-2.24C19.51 3.1 22 5.6 22 8.69c0 7-6.48 11.13-9.38 12.12Z"
            fill="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={1.5}
        />
    </svg>
);

export const SearchIcon = (props: IconSvgProps) => (
    <svg
        aria-hidden="true"
        fill="none"
        focusable="false"
        height="1em"
        role="presentation"
        viewBox="0 0 24 24"
        width="1em"
        {...props}
    >
        <path
            d="M11.5 21C16.7467 21 21 16.7467 21 11.5C21 6.25329 16.7467 2 11.5 2C6.25329 2 2 6.25329 2 11.5C2 16.7467 6.25329 21 11.5 21Z"
            stroke="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
        />
        <path
            d="M22 22L20 20"
            stroke="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
        />
    </svg>
);

export const GoogleIcon: React.FC<IconSvgProps> = ({
                                                       size = 24,
                                                       width,
                                                       height,
                                                       ...props
                                                   }) => (
    <svg
        height={size || height}
        viewBox="0 0 24 24"
        width={size || width}
        {...props}
    >
        <path
            d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
            fill="#4285F4"
        />
        <path
            d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
            fill="#34A853"
        />
        <path
            d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
            fill="#FBBC05"
        />
        <path
            d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
            fill="#EA4335"
        />
    </svg>
);

export const LockIcon: React.FC<IconSvgProps> = ({
                                                     size = 24,
                                                     width,
                                                     height,
                                                     ...props
                                                 }) => (
    <svg
        fill="none"
        height={size || height}
        stroke="currentColor"
        viewBox="0 0 24 24"
        width={size || width}
        {...props}
    >
        <path
            d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
        />
    </svg>
);

export const LogoutIcon = ({className = "", ...props}: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} fill="none" viewBox="0 0 24 24" {...props}>
        <path d="M16 17l5-5m0 0l-5-5m5 5H9" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round"
              strokeWidth="2"/>
        <path d="M13 7V5a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2v-2" stroke="currentColor"
              strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"/>
    </svg>
);

export const DashboardIcon = ({className = "", ...props}: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} fill="none" viewBox="0 0 24 24" {...props}>
        <rect height="9" rx="2" stroke="url(#bluePurpleGradient)" strokeWidth="2" width="7" x="3" y="3"/>
        <rect height="5" rx="2" stroke="url(#bluePurpleGradient)" strokeWidth="2" width="7" x="14" y="3"/>
        <rect height="9" rx="2" stroke="url(#bluePurpleGradient)" strokeWidth="2" width="7" x="14" y="12"/>
        <rect height="5" rx="2" stroke="url(#bluePurpleGradient)" strokeWidth="2" width="7" x="3" y="16"/>
    </svg>
);

export const CloseIcon = ({className = "", ...props}: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} fill="none" viewBox="0 0 24 24" {...props}>
        <line stroke="currentColor" strokeLinecap="round" strokeWidth="2" x1="18" x2="6" y1="6" y2="18"/>
        <line stroke="currentColor" strokeLinecap="round" strokeWidth="2" x1="6" x2="18" y1="6" y2="18"/>
    </svg>
);

const gradientId = "bluePurpleGradient";

// HomeIcon
export const HomeIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        className="size-6"
        height={size || height}
        viewBox="0 0 24 24"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props}
    >
        <defs>
            <linearGradient id={gradientId} x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                {/* blue-400 */}
                <stop offset="50%" stopColor="#818CF8"/>
                {/* indigo-400 */}
                <stop offset="100%" stopColor="#A78BFA"/>
                {/* violet-400 */}
            </linearGradient>
        </defs>
        <path
            d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125
         1.125 1.125H9.75v-4.875c0-.621.504-1.125
         1.125-1.125h2.25c.621 0 1.125.504
         1.125 1.125V21h4.125c.621 0 1.125-.504
         1.125-1.125V9.75M8.25 21h8.25"
            fill="none"
            stroke="url(#bluePurpleGradient)"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={1.5}
        />
    </svg>
);

export const PostDetailsIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg className="size-6"
         fill="none"
         height={size || height}
         stroke="white"
         strokeWidth={0.3}
         viewBox="0 0 24 24"
         width={size || width}
         xmlns="http://www.w3.org/2000/svg" {...props}>
        <defs>
            <linearGradient id="bluePurpleGradientLighter" x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#3B82F6"/>
                <stop offset="50%" stopColor="#6366F1"/>
                <stop offset="100%" stopColor="#9333EA"/>
            </linearGradient>
        </defs>
        <path
            d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"
            fill="url(#bluePurpleGradientLighter)" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
);

export const SliderIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        height={size || height}
        stroke="white"
        strokeWidth={0.3}
        viewBox="0 0 24 24"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props}
    >
        <defs>
            <linearGradient id={gradientId} x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                <stop offset="50%" stopColor="#818CF8"/>
                <stop offset="100%" stopColor="#A78BFA"/>
            </linearGradient>
        </defs>
        <path d="M3 6h18v4H3V6zm4 8h14v4H7v-4z" fill="url(#bluePurpleGradient)"/>
    </svg>
);

export const SubjectsListIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        height={size || height}
        stroke="white"
        strokeWidth={1.8}
        viewBox="0 0 448 512"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props} >
        <defs>
            <linearGradient id="bluePurpleGradient" x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                <stop offset="50%" stopColor="#818CF8"/>
                <stop offset="100%" stopColor="#A78BFA"/>
            </linearGradient>
        </defs>
        <path
            d="M341.268 32v43.278c14.56-6.382 27.323-13.761 42.547-19.013v49.526c14.492-1.994 27.588-4.986 41.615-5.584v295.697C358.088 424.024 290.944 452.012 223.8 480c-67.143-27.988-133.954-55.975-201.23-84.096V99.941c12.963 1.729 25.527 3.39 39.421 5.252V56.265c15.423 5.252 28.32 12.498 43.145 19.544V32c48.463 35.965 90.145 74.59 118.2 126.974C251.19 106.59 293.07 68.164 341.267 32M117.235 55.733c-1.063 91.74-2.06 181.487-3.058 270.435c40.552 32.309 76.983 66.878 104.438 110.554v-8.442c0-78.445-.133-156.89.2-235.335c.066-10.77-2.327-20.409-7.114-29.783c-21.938-43.144-56.174-75.52-94.466-107.43m-82.7 332.327a960781 960781 0 0 0 174.507 72.994c-8.908-12.033-17.617-20.941-27.19-29.052c-33.638-28.519-72.328-48.396-112.88-64.816c-3.856-1.596-11.235-4.454-11.235-4.454c1.263-80.373 2.526-163.87 3.789-245.041l-26.99-4.255zM74.092 74.28c-1.53 93.868-2.992 187.005-4.455 280.275c44.74 17.949 86.689 39.887 124.05 70.002c-24.797-37.029-57.504-66.014-91.342-92.738c.864-81.636 1.728-162.075 2.593-242.381c-10.172-5.585-19.678-10.77-30.846-15.158m153.765 362.442c26.325-44.806 64.883-78.91 104.57-110.62c-.996-89.88-1.994-179.094-2.99-270.635c-9.973 8.642-18.615 15.755-26.725 23.334c-27.589 25.594-52.984 53.05-69.271 87.619c-3.058 6.448-5.784 12.897-5.784 20.475zm113.346-18.48c23.999-10.039 47.998-20.077 72.329-30.315V113.104c-9.773 1.662-19.146 3.191-29.051 4.853c1.263 81.436 2.46 164.934 3.722 245.24l-9.307 3.257c-17.616 6.848-34.768 14.626-51.388 23.6c-31.311 16.82-61.027 35.7-84.295 63.222l-5.517 7.778zm-91.342 10.17a28 28 0 0 1 1.729-1.595c.864-.731 1.728-1.463 2.526-2.26c36.364-30.58 77.98-52.253 122.188-70.002c-1.396-93.403-2.792-186.54-4.255-280.74c-10.902 5.385-21.14 9.44-30.314 15.822c.864 80.838 1.795 161.344 2.659 241.85c-11.368 9.572-22.802 18.68-33.638 28.452c-21.34 19.213-41.815 39.356-57.837 63.488z"
            fill="url(#bluePurpleGradient)"/>
    </svg>);

export const RegistrationListIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        height={size || height}
        stroke="white"
        strokeWidth={0.1}
        viewBox="0 0 24 24"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props}
    >
        <defs>
            <linearGradient id={gradientId} x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                <stop offset="50%" stopColor="#818CF8"/>
                <stop offset="100%" stopColor="#A78BFA"/>
            </linearGradient>
        </defs>
        <path
            d="M2 4h1v16h2V10h4v10h2V6h4v14h2v-6h4v7H2zm16
         11v5h2v-5zm-6-8v13h2V7zm-6 4v9h2v-9z"
            fill="url(#bluePurpleGradient)"
        />
    </svg>
);

export const QuestionsListIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        className="size-6"
        height={size || height}
        stroke="white"
        strokeWidth={0.5}
        viewBox="0 0 24 24"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props}
    >
        <defs>
            <linearGradient id={gradientId} x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                <stop offset="50%" stopColor="#818CF8"/>
                <stop offset="100%" stopColor="#A78BFA"/>
            </linearGradient>
        </defs>
        <path
            d="M9.879 7.519c1.171-1.025 3.071-1.025
         4.242 0 1.172 1.025 1.172 2.687 0
         3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45
         1.827v.75M21 12a9 9 0 1
         1-18 0 9 9 0 0 1 18 0Zm-9
         5.25h.008v.008H12v-.008Z"
            fill="none"
            stroke="url(#bluePurpleGradient)"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={1.5}
        />
    </svg>
);

export const QuizzesListIcon: React.FC<IconSvgProps> = ({size = 24, width, height, ...props}) => (
    <svg
        height={size || height}
        viewBox="0 0 24 24"
        width={size || width}
        xmlns="http://www.w3.org/2000/svg"
        {...props}
    >
        <defs>
            <linearGradient id={gradientId} x1="0%" x2="100%" y1="0%" y2="100%">
                <stop offset="0%" stopColor="#60A5FA"/>
                <stop offset="50%" stopColor="#818CF8"/>
                <stop offset="100%" stopColor="#A78BFA"/>
            </linearGradient>
        </defs>
        <path
            d="M3 4h18v2H3V4zm0 4h14v2H3V8zm0
         4h18v2H3v-2zm0 4h14v2H3v-2zm16
         0h2v2h-2v-2z"
            fill="url(#bluePurpleGradient)"
        />
    </svg>
);

export const EditIcon = (props: IconSvgProps) => {
    return (
        <svg
            aria-hidden="true"
            fill="none"
            focusable="false"
            height="1em"
            role="presentation"
            viewBox="0 0 20 20"
            width="1em"
            {...props}
        >
            <path
                d="M11.05 3.00002L4.20835 10.2417C3.95002 10.5167 3.70002 11.0584 3.65002 11.4334L3.34169 14.1334C3.23335 15.1084 3.93335 15.775 4.90002 15.6084L7.58335 15.15C7.95835 15.0834 8.48335 14.8084 8.74168 14.525L15.5834 7.28335C16.7667 6.03335 17.3 4.60835 15.4583 2.86668C13.625 1.14168 12.2334 1.75002 11.05 3.00002Z"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeMiterlimit={10}
                strokeWidth={1.5}
            />
            <path
                d="M9.90833 4.20831C10.2667 6.50831 12.1333 8.26665 14.45 8.49998"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeMiterlimit={10}
                strokeWidth={1.5}
            />
            <path
                d="M2.5 18.3333H17.5"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeMiterlimit={10}
                strokeWidth={1.5}
            />
        </svg>
    );
};

export const EyeIcon = (props: IconSvgProps) => {
    return (
        <svg
            aria-hidden="true"
            fill="none"
            focusable="false"
            height="1em"
            role="presentation"
            viewBox="0 0 20 20"
            width="1em"
            {...props}
        >
            <path
                d="M12.9833 10C12.9833 11.65 11.65 12.9833 10 12.9833C8.35 12.9833 7.01666 11.65 7.01666 10C7.01666 8.35 8.35 7.01666 10 7.01666C11.65 7.01666 12.9833 8.35 12.9833 10Z"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
            />
            <path
                d="M9.99999 16.8916C12.9417 16.8916 15.6833 15.1583 17.5917 12.1583C18.3417 10.9833 18.3417 9.00831 17.5917 7.83331C15.6833 4.83331 12.9417 3.09998 9.99999 3.09998C7.05833 3.09998 4.31666 4.83331 2.40833 7.83331C1.65833 9.00831 1.65833 10.9833 2.40833 12.1583C4.31666 15.1583 7.05833 16.8916 9.99999 16.8916Z"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
            />
        </svg>
    );
};
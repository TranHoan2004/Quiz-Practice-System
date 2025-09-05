import {
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownTrigger,
    User,
} from "@heroui/react";

import {useAuth} from "@/hooks/useAuth.ts";

type AvatarProps = {
    description?: string
}

const Avatar = ({description}: AvatarProps) => {
    const {user, logout} = useAuth();

    return (
        <div>
            {/*{user && (*/}
            {/*  <>*/}
            {/*    <Dropdown>*/}
            {/*      <DropdownTrigger>*/}
            {/*        <User*/}
            {/*          avatarProps={{*/}
            {/*            src: user.avatarUrl,*/}
            {/*            alt: user.fullName,*/}
            {/*          }}*/}
            {/*          className="cursor-pointer"*/}
            {/*          name={user.fullName}*/}
            {/*        />*/}
            {/*      </DropdownTrigger>*/}
            {/*      <DropdownMenu aria-label={"Static actions of user"}>*/}
            {/*        <DropdownItem key={"profile"}>Profile</DropdownItem>*/}
            {/*      </DropdownMenu>*/}
            {/*    </Dropdown>*/}
            {/*  </>*/}
            {/*)}*/}
            {/* Sample avatar */}
            <Dropdown>
                <DropdownTrigger>
                    <User
                        avatarProps={{
                            src: "https://avatars.githubusercontent.com/u/123456789?v=4",
                            alt: "User Avatar",
                        }}
                        className="cursor-pointer"
                        description={description}
                        name={"User Name"}
                    />
                </DropdownTrigger>
                <DropdownMenu aria-label={"Static actions"}>
                    <DropdownItem key={"profile"}>Profile</DropdownItem>
                    <DropdownItem
                        key="logout"
                        className="font-semibold"
                        color={"danger"}
                        onPress={logout}
                    >
                        Logout
                    </DropdownItem>
                </DropdownMenu>
            </Dropdown>
        </div>
    );
};

export default Avatar;

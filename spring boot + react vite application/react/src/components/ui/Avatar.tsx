import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  User,
} from "@heroui/react";

// import { useAuth } from "@/hooks/useAuth.ts";

const Avatar = () => {
  // const { user } = useAuth();

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
            name={"User Name"}
          />
        </DropdownTrigger>
        <DropdownMenu aria-label={"Static actions"}>
          <DropdownItem key={"profile"}>Profile</DropdownItem>
        </DropdownMenu>
      </Dropdown>
    </div>
  );
};

export default Avatar;

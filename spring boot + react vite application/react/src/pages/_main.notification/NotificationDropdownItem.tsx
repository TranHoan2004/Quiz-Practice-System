import { useEffect, useState } from "react";
import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  NavbarItem,
} from "@heroui/react";
import { Bell } from "lucide-react";

import { useAuth } from "@/hooks/useAuth.ts";
import { getNotifications } from "@/services/notification.service.ts";
import { Notification } from "@/types/notification.ts";
import { useMarkNotifications } from "@/hooks/useNotifications";

const NotificationDropdownItem = () => {
  const [numberOfNoti, setNumberOfNoti] = useState(0);
  const [noti, setNoti] = useState<Notification[]>([]);
  const [page, setPage] = useState(0);
  const [numberOfPage, setNumberOfPage] = useState(0);
  const [refresh, setRefresh] = useState(0);
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      (async () => {
        const data = await getNotifications(
          user.accessToken,
          page === 0 ? undefined : page,
        );
        const normalized = data.notifications.map((n: Notification) => ({
          ...n,
          createdDate: new Date(n.createdDate),
        }));

        if (page === 0) {
          setNoti(normalized); // load lần đầu
        } else {
          setNoti((prev) => [...prev, ...normalized]); // nối thêm khi view more
        }
        setNumberOfNoti(data.totalItems);
        setNumberOfPage(data.totalPages);
      })();
    }
  }, [page, refresh]);

  const timeAgo = (date: Date) => {
    const now = new Date();
    const diff = now.getTime() - date.getTime(); // chênh lệch ms

    const minute = 60 * 1000;
    const hour = 60 * minute;
    const day = 24 * hour;

    if (diff < hour) {
      const m = Math.floor(diff / minute);

      return m > 1 ? `${m} minutes ago` : "1 minute ago";
    } else if (diff < day) {
      const h = Math.floor(diff / hour);

      return h > 1 ? `${h} hours ago` : "1 hour ago";
    } else {
      const d = Math.floor(diff / day);

      return d > 1 ? `${d} days ago` : "1 day ago";
    }
  };

  const handleViewMoreButton = () => {
    if (page < numberOfPage) {
      setRefresh((prev) => prev + 1);
      setPage((prev) => prev + 1);
    }
  };

  const HandleReadEvent = (notificationId: string) => {
    // @ts-ignore
    useMarkNotifications(notificationId, user.accessToken).then((r) =>
      console.log(r),
    );
  };

  return (
    <>
      <NavbarItem>
        <Dropdown>
          <div className="relative">
            <DropdownTrigger>
              <Button
                isIconOnly
                className="rounded-full hover:bg-blue-50  dark:border-white transition"
                variant="bordered"
              >
                <Bell className="w-5 h-5 text-gray-700 dark:text-gray-200" />
              </Button>
            </DropdownTrigger>

            <span
              className="absolute -top-1 -right-1 bg-red-600 text-white text-xs font-bold
                    rounded-full w-4 h-4 flex items-center justify-center z-10"
            >
              {numberOfNoti}
            </span>
          </div>
          <DropdownMenu
            aria-label="Notifications List"
            classNames={{
              list: "max-h-150 overflow-y-auto",
            }}
            closeOnSelect={false}
          >
            <>
              {noti?.map((notification: Notification) => (
                <DropdownItem
                  key={notification.id}
                  className={`flex flex-col items-start text-start ${notification.status ? "bg-gray-100 dark:bg-gray-800" : ""}`}
                  onClick={() => {
                    if (notification.link) {
                      window.open(notification.link, "_blank"); // external link
                    }
                    HandleReadEvent(notification.id);
                    setRefresh((prev) => prev + 1);
                  }}
                >
                  <p className="font-semibold text-base">
                    {notification.title}
                  </p>
                  <p className="text-base">{notification.message}</p>
                  <p className="text-xs text-gray-400">
                    {timeAgo(notification.createdDate)}
                  </p>
                </DropdownItem>
              ))}
            </>
            <DropdownItem
              key="view-more"
              className="text-center font-semibold hover:bg-blue-50"
              onClick={() => handleViewMoreButton()}
            >
              View more
            </DropdownItem>
            {/* Sample structure */}
            {/* Unread */}
            {/* <DropdownItem
                key={1}
                className={`flex flex-col items-start text-start p-2 bg-gray-100 dark:bg-gray-800`}
              >
                <p className="font-semibold text-base">Hehe</p>
                <p className="text-base">Description</p>
                <p className="text-xs text-gray-400">20 minutes ago</p>
              </DropdownItem> */}

            {/* Read */}
            {/* <DropdownItem
                key={2}
                className={`flex flex-col items-start text-start p-2`}
              >
                <p className="font-semibold text-base">Hehe</p>
                <p className="text-base">Description</p>
                <p className="text-xs text-gray-400">20 minutes ago</p>
              </DropdownItem> */}
          </DropdownMenu>
        </Dropdown>
      </NavbarItem>
    </>
  );
};

export default NotificationDropdownItem;

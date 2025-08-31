export type Notification = {
  id: string;
  title: string;
  message: string;
  createdDate: Date;
  status: boolean; // true: unread, false: read
  link?: string; // Optional link to redirect when clicked
};

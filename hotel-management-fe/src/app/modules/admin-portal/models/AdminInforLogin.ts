export interface AdminInforLogin {
  id: string,
  fullName: string,
  username: string;
  token: string;
  refreshToken: string;
  permissions: any;
  isManager: any;
}

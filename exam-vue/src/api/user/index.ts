import myRequest from "@/utils/request.ts";
import {ApiResult} from "@/utils/type.ts";

const urlBase = '/user';

enum API {
    LOGIN_URL = urlBase + '/login',
    REGISTER_URL = urlBase + '/register',
    UPDATE_URL = urlBase + '/updatePassword',
    FORGETPW_URL = urlBase + '/forgetpw'
}

// 登录接口
export const reqLogin = (data: object) => myRequest.post<any, ApiResult>(API.LOGIN_URL, data);
export const reqRegister = (data: object) => myRequest.post<any, ApiResult>(API.REGISTER_URL, data);
export const reqUpdatePassword = (data: object) => myRequest.put<any, ApiResult>(API.UPDATE_URL, data);
export const reqForgetPW = (data: object) => myRequest.post<any, ApiResult>(API.FORGETPW_URL, data);
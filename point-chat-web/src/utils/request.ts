// plugins/axios.ts
import axios from 'axios';
import { useUserStore } from '../store/token.ts';
import router from '../router'; // 直接导入路由实例
import { useRouter } from 'vue-router';

const baseURL = '/';
const instance = axios.create({
  baseURL: baseURL
})

// const router = useRouter();
instance.interceptors.request.use(config => {
  const userStore = useUserStore(); // 获取Pinia store实例
  const token = window.sessionStorage.getItem("token"); // 从store获取token
  const currentPath = router.currentRoute.value.path;
  if (currentPath !== '/login') {
    if (token) {
      config.headers['Authorization'] = token; // 设置请求头中的Authorization字段

    } else if (token === null || !token) {
      // console.log("未找到token和user");
      router.replace({
        path: '/login',
        query: {
          _t: Date.now()
        },
        force: true
      });
    }
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// 新增响应拦截器处理401
instance.interceptors.response.use(res => {
  // 检查业务逻辑上的状态码
  // console.log("response:", res);
  if (res.data.code === 401) {
    // 抛出错误，可以在catch中捕获
    window.sessionStorage.clear();
    router.replace({
      path: '/login',
      query: {
        _t: Date.now()
      },
      force: true
    }).then(() => {
      // alert("登录会话过期，请重新登录");
      throw new Error(res.data.message);
    });
  }
  return res;
}, error => {
  // 处理错误响应
  return Promise.reject(error);
});
export default instance;
# 💬 Point-Chat 聊天系统

`Point-Chat` 是一个基于前后端分离架构的即时通讯系统，支持单聊、群聊、AI 智能回复等功能，界面高仿微信，前端采用 Vue3 + TypeScript，后端基于 Spring Boot 构建，使用 WebSocket 实现实时通信，并结合 Redis 和 OSS 等组件，提升系统性能与扩展性。

📍 体验地址：[http://8.138.238.174:8080](http://8.138.238.174:8080)

---

## 🧩 项目结构

- **前端项目**：[`point-chat-web`](./point-chat-web/)
- **后端项目**：[`point-chat`](./point-chat/)

---

## 🚀 技术栈

### 🔹 前端技术

- [Vue3](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket)
- [Vite](https://vitejs.dev/) 构建工具

### 🔹 后端技术

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySQL](https://www.mysql.com/)
- [Redis](https://redis.io/)
- [WebSocket（基于 TIO封装）](https://gitee.com/tywo45/tio-websocket-showcase)
- [OSS](https://help.aliyun.com/document_detail/31837.html) 文件上传服务

---

## 💡 功能列表

### ✅ 聊天功能

- [x] 用户单聊
- [x] 群聊（创建、加入、聊天）
- [x] 文本消息发送
- [x] 图片上传与发送
- [x] 文件上传与下载
- [x] AI 智能自动回复（DeepSeek 接入）

### ✅ 用户系统

- [x] 用户注册与登录
- [x] 修改头像、昵称、签名等信息
- [x] 搜索并添加好友
- [x] 好友列表管理



---

## 🔧 快速开始

### 🖥️ 前端启动

```bash
cd point-chat-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 默认访问地址：http://localhost:8080
```

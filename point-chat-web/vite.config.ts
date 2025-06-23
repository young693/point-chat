import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0',
    port: 8080,
    proxy: {
      '/api': {//获取路径中包含了/api的请求
        target: 'http://127.0.0.1:8089',//后台服务所在的源
        changeOrigin: true,//修改源
        rewrite: (path) => path.replace(/^\/api/, '')///api替换为''
      },
      '/ws': {
        // target: 'ws://8.138.238.174:9326',
        target: 'ws://127.0.0.1:9326',
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/ws/, '')
      }
    }
  }
})

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://3.25.82.234:8080',
        changeOrigin: true,
      },
      '/actuator': {
        target: 'http://3.25.82.234:8080',
        changeOrigin: true,
      }
    }
  }
})


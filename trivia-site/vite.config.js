import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/questions': 'http://localhost:8080',
      '/checkanswers': 'http://localhost:8080'
    }
  }
})

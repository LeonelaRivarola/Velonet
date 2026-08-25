// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'

// // https://vite.dev/config/
// export default defineConfig({
//   plugins: [react()],
// })
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      // Esto fuerza a que apunte directamente a la carpeta instalada
      '@emotion/styled': path.resolve(__dirname, './node_modules/@emotion/styled'),
      '@emotion/react': path.resolve(__dirname, './node_modules/@emotion/react'),
    },
  },
})
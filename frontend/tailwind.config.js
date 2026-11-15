/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        ousl: {
          blue: '#002B49',
          navy: '#001A2E',
          gold: '#C59B27',
          accent: '#1E5AA8',
          light: '#F4F7FB'
        }
      }
    },
  },
  plugins: [],
}


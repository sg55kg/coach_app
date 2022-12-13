/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    colors: {
      'gray-100': '#1f232f',
      'gray-shade': '#020202',
      'gray-200': '#2a2e3b',
      'gray-300': '#12141c',
      'textgray': '#cfc9bc',
      'textblue': '#adbdd2',
      'link': '#65c9e7',
      'link-shade': '#389fbe',
      'red': '#d9583e',
      'red-shade': '#d24c0f',
      'orange': '#dd9f18',
      'orange-shade': '#d9911b',
      'yellow-lt': '#fceea9',
      'yellow': '#fde577',
      'yellow-shade': '#fddd4c',
      'white': '#efefef',
      'black': '#030303',
    },
    extend: {},
  },
  plugins: [],
}

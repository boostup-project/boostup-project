/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./pages/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        SCDream1: ["SCDream1"],
        SCDream2: ["SCDream2"],
        SCDream3: ["SCDream3"],
        SCDream4: ["SCDream4"],
        SCDream5: ["SCDream5"],
        SCDream6: ["SCDream6"],
        SCDream7: ["SCDream7"],
        SCDream8: ["SCDream8"],
        SCDream9: ["SCDream9"],
      },
      colors: {
        bgColor: "#F8F9FD",
        textColor: "#424242",
        pointColor: "#0A65F2",
        modalImgTxt: "#A9C8FA",
        borderColor: "#A8A7A7",
        modalBgColor: "rgba(204, 204, 204, 0.73)",
        javascript: "#0A83F2",
        python: "#6ED5F6",
        java: "#92B7FF",
        go: "#ADE2F2",
        kotlin: "#CAD1FA",
        swift: "#F09AB9",
        c: "#A69AF0",
        php: "#527ACC",
        negativeMessage: "#FF000F",
        disabledBtnColor: "#5090F2",
      },
    },
    screens: {
      desktop: "1024px",
      tablet: "764px",
    },
  },
  plugins: [],
};

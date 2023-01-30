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
        markBgColor: "#F8F8F8",
        Javascript: "#0A83F2",
        Python: "#6ED5F6",
        Java: "#92B7FF",
        Go: "#ADE2F2",
        Kotlin: "#CAD1FA",
        Swift: "#F09AB9",
        "C#": "#A69AF0",
        PHP: "#527ACC",
        negativeMessage: "#FF000F",
        disabledBtnColor: "#5090F2",
      },
    },
    screens: {
      desktop: "1024px",
      tablet: "764px",
    },
  },
  plugins: [
    require("tailwind-scrollbar"),
    require("tailwind-scrollbar-hide"),
    require("@tailwindcss/typography"),
  ],
  variants: {
    scrollbar: ["rounded"],
  },
};

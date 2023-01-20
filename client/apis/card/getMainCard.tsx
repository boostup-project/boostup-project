// import axios from "axios";

// const getMainCard = async () => {
//   return await axios.get(`/lesson`, {
//     baseURL: process.env.NEXT_PUBLIC_API_URL,
//     headers: {
//       "content-Type": `application/json`,
//       "ngrok-skip-browser-warning": "69420",
//     },
//   });
// };
// export default getMainCard;

import type { GetStaticProps } from "next";

type Card = {
  title: string;
};
export const getStaticProps: GetStaticProps = async context => {
  const response = await fetch("https://dd27-182-226-233-7.jp.ngrok.io");
  const card: Card = await response.json();

  return {
    props: {
      card,
    },
  };
};

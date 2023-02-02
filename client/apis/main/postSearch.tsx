import axios from "axios";

interface SearchInputData {
  name: string;
  address: number | string;
  career: number | string;
  language: number | string;
  startCost: number | string;
  endCost: number | string;
}

const postSearch = (searchInputData: SearchInputData) => {
  const url = "/lesson/search";
  return axios.post(url, searchInputData, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "application/json",
      "ngrok-skip-browser-warning": "63490",
    },
  });
};

export default postSearch;

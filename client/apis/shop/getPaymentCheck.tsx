import axios from "axios";

const getPaymentCheck = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/payment/check`;

  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "application/json",
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getPaymentCheck;

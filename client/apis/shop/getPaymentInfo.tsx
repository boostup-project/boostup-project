import axios from "axios";

const getPaymentInfo = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/payment/info`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "application/json",
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getPaymentInfo;

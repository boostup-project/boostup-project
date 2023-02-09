import axios from "axios";

const getResetChatAlarm = (roomId: number) => {
  return axios.get(`/alarm/room/${roomId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      //   "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getResetChatAlarm;

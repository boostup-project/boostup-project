import axios from "axios";

const getChatRoomList = () => {
  return axios.get("/chat/room", {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      //   "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getChatRoomList;

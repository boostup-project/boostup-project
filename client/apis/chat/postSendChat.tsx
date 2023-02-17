import axios from "axios";

interface IMessage {
  user: string;
  message: string;
}

const postSendChat = ({ message }: IMessage) => {
  return axios.post("/api/chat", message, {
    baseURL: process.env.NEXT_SOCKET_API_URL,
  });
};

export default postSendChat;

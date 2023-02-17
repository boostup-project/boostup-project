import { useMutation } from "@tanstack/react-query";
import postSendChat from "apis/chat/postSendChat";

const usePostSendChat = () => {
  return useMutation(postSendChat);
};

export default usePostSendChat;

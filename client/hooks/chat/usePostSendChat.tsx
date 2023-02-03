import { useMutation } from "@tanstack/react-query";
import postSendChat from "apis/chat/postSendChat";

const usePostSendChat = () => {
  return useMutation(postSendChat, {
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default usePostSendChat;

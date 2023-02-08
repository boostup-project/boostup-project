import { useQuery } from "@tanstack/react-query";
import getChatRoomList from "apis/chat/getChatRoomList";

const useGetChatRoomList = () => {
  return useQuery(["get/ChatRoomList"], getChatRoomList, {
    onSuccess: res => {
      // console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetChatRoomList;

import { useQuery } from "@tanstack/react-query";
import getChatRoomList from "apis/chat/getChatRoomList";
import { useSetRecoilState } from "recoil";
import { chatRoomListState } from "atoms/chat/chatAtom";

const useGetChatRoomList = () => {
  const setChatRoomList = useSetRecoilState(chatRoomListState);

  return useQuery(["get/ChatRoomList"], getChatRoomList, {
    onSuccess: res => {
      setChatRoomList([...res?.data]);
    },
    onError: err => {
      console.log(err);
    },
    refetchOnWindowFocus: false,
  });
};

export default useGetChatRoomList;

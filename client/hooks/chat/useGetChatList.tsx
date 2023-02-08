import { useQuery } from "@tanstack/react-query";
import getChatList from "apis/chat/getChatList";
import { useRecoilState } from "recoil";
import { chatListState, roomIdState } from "atoms/chat/chatAtom";

const useGetChatList = (roomId: number) => {
  const [chatList, setChatList] = useRecoilState(chatListState);
  const [roomNumber, setRoomNumber] = useRecoilState(roomIdState);

  return useQuery(["get/chatList"], () => getChatList(roomId), {
    enabled: false,
    onSuccess: res => {
      setChatList(res.data);
      setRoomNumber(roomId);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetChatList;

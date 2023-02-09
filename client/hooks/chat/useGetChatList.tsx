import { useQuery } from "@tanstack/react-query";
import getChatList from "apis/chat/getChatList";
import { useRecoilState } from "recoil";
import { chatListState, roomIdState, chatActive } from "atoms/chat/chatAtom";

const useGetChatList = (roomId: number) => {
  const [chatList, setChatList] = useRecoilState(chatListState);
  // const [roomNumber, setRoomNumber] = useRecoilState(roomIdState);
  const [active, setActive] = useRecoilState(chatActive);

  return useQuery(["get/chatList"], () => getChatList(roomId), {
    enabled: false,
    onSuccess: res => {
      setChatList(res.data);
      setActive(true);
    },
    onError: err => {
      console.log(err);
    },
    refetchOnWindowFocus: false,
  });
};

export default useGetChatList;

import { useQuery } from "@tanstack/react-query";
import getResetChatAlarm from "apis/chat/getResetChatAlarm";
import { useQueryClient } from "@tanstack/react-query";

const useGetResetChatAlarm = (roomId: number) => {
  const queryClient = useQueryClient();

  return useQuery(["get/resetChatAlarm"], () => getResetChatAlarm(roomId), {
    enabled: false,
    onSuccess: res => {
      queryClient.invalidateQueries(["get/ChatRoomList"]);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetResetChatAlarm;

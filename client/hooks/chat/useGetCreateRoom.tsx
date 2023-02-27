import { useQuery } from "@tanstack/react-query";
import getCreateRoom from "apis/chat/getCreateRoom";
import { useRouter } from "next/router";

const useGetCreateRoom = (lessonId: number) => {
  const router = useRouter();

  return useQuery(["get/createRoom"], () => getCreateRoom(lessonId), {
    enabled: false,
    onSuccess: res => {
      router.push(`/chat/${lessonId}`);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetCreateRoom;

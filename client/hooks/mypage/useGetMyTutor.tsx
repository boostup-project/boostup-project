import { useQuery } from "@tanstack/react-query";
import getMyTutor from "apis/mypage/getMyTutor";

const useGetMyTutor = () => {
  return useQuery(["get/MyTutor"], () => getMyTutor(), {
    // enabled: true,
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetMyTutor;

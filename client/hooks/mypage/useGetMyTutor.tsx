import { useQuery } from "@tanstack/react-query";
import getMyTutor from "apis/mypage/getMyTutor";

const useGetMyTutor = () => {
  return useQuery(["get/MyTutor"], () => getMyTutor(), {
    retry: 1,
  });
};

export default useGetMyTutor;

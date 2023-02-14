import { useQuery } from "@tanstack/react-query";
import getAllBookmark from "apis/mypage/getAllBookmark";

const useGetAllBookmark = () => {
  return useQuery(["getAllBookmark"], () => getAllBookmark(), {
    enabled: true,
    onSuccess: res => {},
    onError: error => {},
    retry: 1,
  });
};

export default useGetAllBookmark;

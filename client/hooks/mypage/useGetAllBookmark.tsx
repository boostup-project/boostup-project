import { useQuery } from "@tanstack/react-query";
import getAllBookmark from "apis/mypage/getAllBookmark";

const useGetAllBookmark = () => {
  return useQuery(["getAllBookmark"], () => getAllBookmark(), {
    enabled: true,
    onSuccess: res => {
      console.log(res.data.bookmark);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetAllBookmark;

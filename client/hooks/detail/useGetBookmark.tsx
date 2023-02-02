import { useQuery } from "@tanstack/react-query";
import getBookmark from "apis/detail/getBookmark";
const useGetBookmark = (lessonId: number) => {
  return useQuery(["get/Bookmark"], () => getBookmark(lessonId), {
    enabled: false,
    // staleTime: 5 * 60000,
    onSuccess: res => {
      console.log("sucess");
    },
    onError: res => {
      console.log("failed");
    },
  });
};

export default useGetBookmark;

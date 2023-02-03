import { useQuery } from "@tanstack/react-query";
import getBookmark from "apis/detail/getBookmark";
const useGetBookmark = (lessonId: number) => {
  return useQuery(["get/Bookmark"], () => getBookmark(lessonId), {
    // enabled: true,
    // staleTime: 5 * 60000,
    onSuccess: res => {
      console.log(res);
    },
    onError: res => {
      console.log("failed");
    },
  });
};

export default useGetBookmark;

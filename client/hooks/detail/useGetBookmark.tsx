import { useQuery } from "@tanstack/react-query";
import getBookmark from "apis/detail/getBookmark";
const useGetBookmark = (lessonId: number) => {
  return useQuery(["get/Bookmark"], () => getBookmark(lessonId), {
    onSuccess: res => {
      console.log("success");
    },
    onError: res => {
      console.log("failed");
    },
  });
};

export default useGetBookmark;

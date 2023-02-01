import { useQuery } from "@tanstack/react-query";
import getBookmarkModi from "apis/detail/getBookmarkModi";

const useGetBookmarkModi = (lessonId: number) => {
  return useQuery(["get/BookmarkModi"], () => getBookmarkModi(lessonId), {
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetBookmarkModi;

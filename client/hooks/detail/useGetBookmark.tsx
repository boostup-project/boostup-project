import { useQuery } from "react-query";
import getBookmark from "apis/detail/getBookmark";

const useGetBookmark = (lessonId: number) => {
  return useQuery(["getAllBookmark"], () => getBookmark(lessonId), {
    enabled: false,
    onSuccess: res => {
      console.log(res.data.bookmark);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetBookmark;

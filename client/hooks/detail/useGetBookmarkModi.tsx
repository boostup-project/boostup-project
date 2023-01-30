import { useQuery } from "react-query";
import getBookmarkModi from "apis/detail/getBookmarkModi";

const useGetBookmarkModi = (lessonId: number) => {
  return useQuery(["getBookmarkModi"], () => getBookmarkModi(lessonId), {
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

export default useGetBookmarkModi;

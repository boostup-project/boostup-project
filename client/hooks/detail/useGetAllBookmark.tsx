import { useQuery } from "react-query";
import getAllBookmark from "apis/detail/getAllBookmark";

const useGetAllBookmark = (lessonId: number) => {
  return useQuery(["getAllBookmark"], () => getAllBookmark(lessonId), {
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

export default useGetAllBookmark;

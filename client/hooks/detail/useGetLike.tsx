import { useQuery } from "react-query";
import getLike from "apis/detail/getLike";

const useGetLike = (lessonId: number) => {
  return useQuery(["getLike"], () => getLike(lessonId), {
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

export default useGetLike;

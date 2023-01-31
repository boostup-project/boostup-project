import { useQuery } from "react-query";
import getDetailReview from "apis/detail/getDetailReview";

const useGetDetailReview = (lessonId: number) => {
  return useQuery(["get/review"], () => getDetailReview(lessonId), {
    enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetDetailReview;

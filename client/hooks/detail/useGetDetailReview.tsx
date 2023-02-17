import { useQuery } from "@tanstack/react-query";
import getDetailReview from "apis/detail/getDetailReview";

const useGetDetailReview = (lessonId: number) => {
  return useQuery(["get/review"], () => getDetailReview(lessonId), {
    enabled: false,
  });
};

export default useGetDetailReview;

import { useQuery } from "@tanstack/react-query";
import getMyReview from "apis/mypage/getMyReview";

const useGetMyReview = () => {
  return useQuery(["get/MyReview"], () => getMyReview(), {
    retry: 1,
  });
};

export default useGetMyReview;

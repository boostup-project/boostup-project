import { useQuery } from "@tanstack/react-query";
import getMyReview from "apis/mypage/getMyReview";

const useGetMyReview = () => {
  return useQuery(["get/MyReview"], () => getMyReview(), {
    // enabled: true,
    // onSuccess: res => {
    //   console.log(res);
    // },
    // onError: error => {
    //   console.log(error);
    // },
    retry: 1,
  });
};

export default useGetMyReview;

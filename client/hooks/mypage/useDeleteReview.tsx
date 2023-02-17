import deleteReview from "apis/mypage/deleteReview";
import { useMutation } from "@tanstack/react-query";

const useDeleteReview = () => {
  return useMutation(deleteReview, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteReview;

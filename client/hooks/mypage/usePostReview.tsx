import { useMutation, useQueryClient } from "@tanstack/react-query";
import postReview from "apis/mypage/postReview";

const usePostReview = () => {
  return useMutation(postReview, {
    onSuccess: (res: any) => {},
    onError: (err: any) => {},
  });
};

export default usePostReview;

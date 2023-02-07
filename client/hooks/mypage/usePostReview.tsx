import { useMutation, useQueryClient } from "@tanstack/react-query";
import postReview from "apis/mypage/postReview";

const usePostReview = () => {
  return useMutation(postReview, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostReview;

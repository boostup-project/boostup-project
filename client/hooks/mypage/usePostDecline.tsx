import { useMutation, useQueryClient } from "@tanstack/react-query";
import postDecline from "apis/mypage/postDecline";

const usePostDecline = () => {
  return useMutation(postDecline, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostDecline;

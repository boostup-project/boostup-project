import { useMutation, useQueryClient } from "@tanstack/react-query";
import postDecline from "apis/mypage/postDecline";

const usePostDecline = () => {
  return useMutation(postDecline, {
    onSuccess: (res: any) => {},
    onError: (err: any) => {},
  });
};

export default usePostDecline;

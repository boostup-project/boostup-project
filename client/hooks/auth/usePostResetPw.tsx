import { useMutation } from "@tanstack/react-query";
import postResetPw from "apis/auth/postResetPw";

const usePostResetPw = () => {
  return useMutation(postResetPw, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostResetPw;

import { useMutation } from "@tanstack/react-query";
import postCheckEmail from "apis/auth/postCheckEmail";

const usePostCheckEmail = () => {
  return useMutation(postCheckEmail, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostCheckEmail;

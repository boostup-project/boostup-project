import { useMutation, useQueryClient } from "@tanstack/react-query";
import postApply from "apis/detail/postApply";

const usePostApply = () => {
  return useMutation(postApply, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostApply;

import { useMutation, useQueryClient } from "@tanstack/react-query";
import postApply from "apis/detail/postApply";

const usePostApply = () => {
  return useMutation(postApply, {
    onSuccess: (res: any) => {},
    onError: (err: any) => {},
  });
};

export default usePostApply;

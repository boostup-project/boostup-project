import { useMutation, useQueryClient } from "react-query";
import postApply from "apis/detail/postApply";

const usePostApply = () => {
  return useMutation(postApply, {
    onSuccess: res => {
      console.log("success");
    },
    onError: err => {
      console.log("");
    },
  });
};

export default usePostApply;

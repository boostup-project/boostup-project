import { useMutation, useQueryClient } from "react-query";
import getApply from "apis/detail/getApply";

const useGetApply = () => {
  return useMutation(getApply, {
    onSuccess: res => {
      console.log("success");
    },
    onError: err => {
      console.log("success");
    },
  });
};

export default useGetApply;

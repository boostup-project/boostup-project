import deleteApply from "apis/mypage/deleteApply";
import { useMutation } from "@tanstack/react-query";

const useDeleteApply = () => {
  return useMutation(deleteApply, {
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};
export default useDeleteApply;

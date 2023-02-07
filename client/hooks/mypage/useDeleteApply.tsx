import deleteApply from "apis/mypage/deleteApply";
import { useMutation } from "@tanstack/react-query";

const useDeleteApply = () => {
  return useMutation(deleteApply, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteApply;

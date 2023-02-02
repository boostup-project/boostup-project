import deleteDetail from "apis/detail/deleteDetail";
import { useMutation } from "@tanstack/react-query";

const useDeleteDetail = () => {
  return useMutation(deleteDetail, {
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};
export default useDeleteDetail;

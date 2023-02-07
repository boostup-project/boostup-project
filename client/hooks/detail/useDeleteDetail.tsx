import deleteDetail from "apis/detail/deleteDetail";
import { useMutation } from "@tanstack/react-query";

const useDeleteDetail = () => {
  return useMutation(deleteDetail, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteDetail;

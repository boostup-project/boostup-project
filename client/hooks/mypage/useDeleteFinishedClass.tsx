import deleteFinishedClass from "apis/mypage/deleteFinishedClass";
import { useMutation } from "@tanstack/react-query";

const useDeleteFinishedClass = () => {
  return useMutation(deleteFinishedClass, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteFinishedClass;

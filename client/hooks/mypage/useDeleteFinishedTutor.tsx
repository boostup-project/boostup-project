import deleteFinishedTutor from "apis/mypage/deleteFinishedTutor";
import { useMutation } from "@tanstack/react-query";

const useDeleteFinishedTutor = () => {
  return useMutation(deleteFinishedTutor, {
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};
export default useDeleteFinishedTutor;

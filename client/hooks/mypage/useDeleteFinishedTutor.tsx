import deleteFinishedTutor from "apis/mypage/deleteFinishedTutor";
import { useMutation } from "@tanstack/react-query";
//과외 종료 삭제(강사용)
const useDeleteFinishedTutor = () => {
  return useMutation(deleteFinishedTutor, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteFinishedTutor;

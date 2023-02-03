import { useMutation } from "@tanstack/react-query";
import postMemberModi from "apis/mypage/postMemberModi";
import { toast } from "react-toastify";

const usePostMemberModi = () => {
  return useMutation(postMemberModi, {
    onSuccess: res => {
      console.log("member modified");
      toast.success("성공적으로 변경되었습니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
    onError: res => {
      console.log("member modi failed");
    },
  });
};

export default usePostMemberModi;

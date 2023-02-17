import { useMutation } from "@tanstack/react-query";
import postPwdChange from "apis/mypage/postPwdChange";
import { toast } from "react-toastify";

const usePostPwdChange = () => {
  return useMutation(postPwdChange, {
    onSuccess: res => {
      toast.success("비밀번호가 변경되었습니다", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
    onError: (res: any) => {
      toast.error(res.response.data.message, {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};

export default usePostPwdChange;

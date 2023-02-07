import { useMutation, useQueryClient } from "@tanstack/react-query";
import postPwdCheck from "apis/mypage/postPwdCheck";
import { toast } from "react-toastify";

const usePostPwdCheck = () => {
  const queryClient = useQueryClient();

  return useMutation(postPwdCheck, {
    onSuccess: res => {
      toast.info("비밀변호가 확인되었습니다", {
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

export default usePostPwdCheck;

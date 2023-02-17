import { useMutation } from "@tanstack/react-query";
import postNameCheck from "apis/mypage/postNameCheck";
import { toast } from "react-toastify";

const usePostNameCheck = () => {
  return useMutation(postNameCheck, {
    onSuccess: res => {
      console.log("name checked");
      toast.info("변경이 가능한 이름입니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
    onError: (res: any) => {
      // console.log("name check failed");
      console.log(res.response.data.message);
      toast.error(res.response.data.message, {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};

export default usePostNameCheck;

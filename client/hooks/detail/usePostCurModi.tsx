import postCurModi from "apis/detail/postCurModi";
import { useMutation } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { useSetRecoilState } from "recoil";
import { refetchToggle } from "atoms/detail/detailAtom";

export const usePostCurModi = () => {
  const setToggle = useSetRecoilState(refetchToggle);

  return useMutation(postCurModi, {
    onSuccess: res => {
      toast.success("진행상황이 수정되었습니다", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
      setToggle(prev => !prev);
    },
    onError: res => {
      toast.error("전송이 실패되었습니다. 다시 작성해주세요", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};

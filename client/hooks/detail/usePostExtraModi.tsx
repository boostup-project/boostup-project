import postExtraModi from "apis/detail/postExtraModi";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { useSetRecoilState } from "recoil";
import { refetchToggle } from "atoms/detail/detailAtom";

const usePostExtraModi = () => {
  const setToggle = useSetRecoilState(refetchToggle);

  return useMutation(postExtraModi, {
    onSuccess: res => {
      toast.success("정보가 수정되었습니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
      setToggle(prev => !prev);
    },
    onError: err => {
      console.log("extraModiFailed");
    },
  });
};

export default usePostExtraModi;

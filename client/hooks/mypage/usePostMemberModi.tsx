import postMemberModi from "apis/mypage/postMemberModi";
import { useMutation } from "@tanstack/react-query";
import { isMemberEdited } from "atoms/mypage/myPageAtom";
import { toast } from "react-toastify";
import { useSetRecoilState } from "recoil";

const usePostMemberModi = () => {
  const setIsMemEdit = useSetRecoilState(isMemberEdited);
  return useMutation(postMemberModi, {
    onSuccess: res => {
      console.log("member modified");
      localStorage.setItem("memberImage", res.data.memberImage);
      localStorage.setItem("name", res.data.name);
      toast.success("유저정보가 변경되었습니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
      setIsMemEdit(prev => !prev);
    },
    onError: res => {
      console.log("member modi failed");
    },
  });
};

export default usePostMemberModi;

import { useMutation } from "@tanstack/react-query";
import postAuthPw from "apis/auth/postAuthEmail";
import { useRecoilState } from "recoil";
import { resetPwStep } from "atoms/auth/authAtom";

const usePostEmailAuth = () => {
  const [step, setStep] = useRecoilState(resetPwStep);
  return useMutation(postAuthPw, {
    // onSuccess: () => {
    //   setStep(2);
    // },
    onError: error => {
      console.log(error);
      alert("요청실패!");
    },
  });
};

export default usePostEmailAuth;

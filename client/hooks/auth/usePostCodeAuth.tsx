import { useMutation } from "@tanstack/react-query";
import postAuthCode from "apis/auth/postAuthCode";
// import { useRecoilState } from "recoil";
// import { resetPwStep } from "atoms/auth/authAtom";

const usePostCodeAuth = () => {
  // const [step, setStep] = useRecoilState(resetPwStep);
  return useMutation(postAuthCode, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostCodeAuth;

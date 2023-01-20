import { useMutation } from "react-query";
import postAuthCode from "apis/auth/postAuthCode";
import { useRecoilState } from "recoil";
import { resetPwStep } from "atoms/auth/authAtom";

const usePostCodeAuth = () => {
  const [step, setStep] = useRecoilState(resetPwStep);
  return useMutation(postAuthCode, {
    onSuccess: (res: any) => {
      setStep(3);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostCodeAuth;

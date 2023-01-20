import { useMutation } from "react-query";
import postSignUp from "apis/auth/postSignUp";
import { useRecoilState } from "recoil";
import { signUpErrorMessage } from "atoms/auth/authAtom";

const usePostSignUp = () => {
  const [error, setError] = useRecoilState(signUpErrorMessage);
  return useMutation(postSignUp, {
    onError: (error: any) => {
      setError(error.response.data.message);
    },
  });
};

export default usePostSignUp;

import { useMutation } from "@tanstack/react-query";
import postLogin from "apis/auth/postLogin";
import { useRecoilState, useSetRecoilState } from "recoil";
import { loginErrorMessage, logUser } from "atoms/auth/authAtom";

const usePostLogin = () => {
  const [error, setError] = useRecoilState(loginErrorMessage);
  const setLog = useSetRecoilState<boolean>(logUser);

  return useMutation(postLogin, {
    onSuccess: (res: any) => {
      console.log(res);
      localStorage.setItem("token", res.headers.authorization);
      localStorage.setItem("email", res.data.email);
      localStorage.setItem("name", res.data.name);
      localStorage.setItem("memberId", res.data.memberId);
      localStorage.setItem("memberImage", res.data.memberImage);

      setLog(prev => !prev);
    },
    onError: (err: any) => {
      setError(err.response.data.message);
    },
  });
};

export default usePostLogin;

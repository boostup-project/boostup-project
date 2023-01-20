import { useMutation } from "react-query";
import postLogin from "apis/auth/postLogin";
import { useRecoilState } from "recoil";
import { loginErrorMessage } from "atoms/auth/authAtom";

const usePostLogin = () => {
  const [error, setError] = useRecoilState(loginErrorMessage);

  return useMutation(postLogin, {
    onSuccess: (res: any) => {
      localStorage.setItem("token", res.headers.authorization);
      localStorage.setItem("refresh_token", res.headers.refreshtoken);
      localStorage.setItem("email", res.data.email);
      localStorage.setItem("name", res.data.name);
    },
    onError: (err: any) => {
      setError(err.response.data.message);
    },
  });
};

export default usePostLogin;

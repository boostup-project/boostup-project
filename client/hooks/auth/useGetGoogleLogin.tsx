import { useQuery } from "@tanstack/react-query";
import getGoogleLogin from "apis/auth/getGoogleLogin";

const useGetGoogleLogin = () => {
  return useQuery(["get/googleLogin"], () => getGoogleLogin(), {
    enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetGoogleLogin;

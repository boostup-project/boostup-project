import { useQuery } from "@tanstack/react-query";
import getKakaoLogin from "apis/auth/getKakaoLogin";

const useGetKakaoLogin = () => {
  return useQuery(["get/kakaoLogin"], () => getKakaoLogin(), {
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

export default useGetKakaoLogin;

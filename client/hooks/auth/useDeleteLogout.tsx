import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { toast } from "react-toastify";
import deleteLogout from "apis/auth/deleteLogout";
import { useSetRecoilState } from "recoil";
import { logUser } from "atoms/auth/authAtom";

const useDeleteLogout = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const setLog = useSetRecoilState(logUser);
  return useMutation(deleteLogout, {
    onSuccess: res => {
      queryClient.invalidateQueries(["cards"]);
      toast.success("로그아웃 되었습니다.", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
      localStorage.clear();
      setLog(prev => !prev);
      router.push("/");
    },
    onError: res => {
      console.log("logout failed");
    },
  });
};

export default useDeleteLogout;

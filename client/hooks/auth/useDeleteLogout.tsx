import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/router";
import { toast } from "react-toastify";
import deleteLogout from "apis/auth/deleteLogout";
import { useRecoilState, useSetRecoilState } from "recoil";
import { logUser } from "atoms/auth/authAtom";
import { refetchBookmark } from "atoms/detail/detailAtom";

const useDeleteLogout = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const setLog = useSetRecoilState(logUser);

  const [toggle, setToggle] = useRecoilState(refetchBookmark);
  return useMutation(deleteLogout, {
    onSuccess: res => {
      // queryClient.invalidateQueries(["cards"]);
      setToggle(!toggle);
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

import deleteAccount from "apis/mypage/deleteAccount";
import { useMutation } from "@tanstack/react-query";

const useDeleteAccount = () => {
  return useMutation(deleteAccount, {
    onSuccess: res => {},
    onError: err => {},
  });
};
export default useDeleteAccount;

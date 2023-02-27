import { useMutation, useQueryClient } from "@tanstack/react-query";
import postAccept from "apis/mypage/postAccept";

const usePostAccept = () => {
  return useMutation(postAccept, {
    onSuccess: (res: any) => {},
    onError: (err: any) => {},
  });
};

export default usePostAccept;

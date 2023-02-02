import { useMutation, useQueryClient } from "react-query";
import postAccept from "apis/mypage/postAccept";

const usePostAccept = () => {
  return useMutation(postAccept, {
    onSuccess: (res: any) => {
      console.log(res);
    },
    onError: (err: any) => {
      console.log(err);
    },
  });
};

export default usePostAccept;

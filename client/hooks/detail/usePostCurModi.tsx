import postDetailModi from "apis/detail/postDetailModi";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";

export const usePostCurModi = () => {
  const queryClient = useQueryClient();

  return useMutation(postDetailModi, {
    onSuccess: res => {
      toast.success("진행상황이 수정되었습니다", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
      queryClient.invalidateQueries("useGetCurriculum");
    },
    onError: res => {
      toast.error("전송이 실패되었습니다. 다시 작성해주세요", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};

import { useMutation, useQueryClient } from "@tanstack/react-query";
import patchReview from "apis/mypage/patchReview";
import { toast } from "react-toastify";
import { useRecoilState } from "recoil";
import { powerEditReviewModal } from "atoms/main/mainAtom";

const usePatchReview = () => {
  const [powerModal, setPowerModal] = useRecoilState(powerEditReviewModal);
  const queryClient = useQueryClient();

  return useMutation(patchReview, {
    onSuccess: res => {
      toast.success("리뷰가 수정되었습니다!", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
      setPowerModal(false);
      queryClient.invalidateQueries(["get/MyReview"]);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default usePatchReview;

import { useMutation, useQueryClient } from "react-query";
import postBasicModi from "apis/detail/postBasicModi";
import { useRecoilState } from "recoil";
import { editMode, powerBasicEditModal } from "../../atoms/detail/detailAtom";

const usePostBasicModi = () => {
  const [mode, setMode] = useRecoilState(editMode);
  const [power, setPower] = useRecoilState(powerBasicEditModal);

  const queryClient = useQueryClient();

  return useMutation(postBasicModi, {
    onSuccess: res => {
      console.log("success");
      setMode(false);
      setPower(false);
      queryClient.invalidateQueries(["get/detailBasicInfo"]);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default usePostBasicModi;

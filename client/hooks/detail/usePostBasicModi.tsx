import { useMutation, useQueryClient } from "@tanstack/react-query";
import postBasicModi from "apis/detail/postBasicModi";
import { useRecoilState } from "recoil";
import {
  editMode,
  powerBasicEditModal,
  refetchToggle,
} from "../../atoms/detail/detailAtom";

const usePostBasicModi = () => {
  const [mode, setMode] = useRecoilState(editMode);
  const [power, setPower] = useRecoilState(powerBasicEditModal);
  const [toggle, setToggle] = useRecoilState(refetchToggle);
  const queryClient = useQueryClient();

  return useMutation(postBasicModi, {
    onSuccess: res => {
      setMode(false);
      setPower(false);
      setToggle(!toggle);
      console.log(res);
      return queryClient.invalidateQueries(["get/detailBasicInfo"]);
    },
    onError: err => {
      console.log(err);
    },
  });
};

export default usePostBasicModi;

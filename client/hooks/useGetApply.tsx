import { useMutation, useQueryClient } from "react-query";
import getApply from "apis/detail/getApply";

const useGetApply = () => {
  const queryClient = useQueryClient();

  return useMutation(getApply, {
    onSuccess: res => {
      console.log("success");
    },
    onError: err => {},
  });
};

export default useGetApply;

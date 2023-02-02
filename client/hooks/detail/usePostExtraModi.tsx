import postExtraModi from "apis/detail/postExtraModi";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const usePostExtraModi = () => {
  const queryClient = useQueryClient();

  return useMutation(postExtraModi, {
    onSuccess: res => {
      console.log("extraModied");
    },
    onError: err => {
      console.log("extraModiFailed");
    },
  });
};

export default usePostExtraModi;

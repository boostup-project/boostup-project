import { useMutation, useQueryClient } from "react-query";
import postWrite from "apis/postWrite";

const usePostWrite = () => {
  const queryClient = useQueryClient();

  return useMutation(postWrite, {
    onSuccess: res => {
      // queryClient.invalidateQueries({ queryKey; ['Cards']})
      console.log("success");
    },
    onError: err => {},
  });
};

export default usePostWrite;

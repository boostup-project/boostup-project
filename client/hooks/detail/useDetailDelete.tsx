import detailDelete from "apis/detail/deleteDetail";
import { useMutation } from "react-query";

const useDetailDelete = () => {
  return useMutation(detailDelete, {
    onSuccess: res => {
      console.log(res);
    },
    onError: err => {
      console.log(err);
    },
  });
};
export default useDetailDelete;

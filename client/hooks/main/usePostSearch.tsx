import { useMutation } from "@tanstack/react-query";
import postSearch from "apis/main/postSearch";
import { toast } from "react-toastify";

const usePostSearch = () => {
  return useMutation(postSearch, {
    onSuccess: res => {
      console.log("Searched");
    },
    onError: res => {
      toast.error("검색 조건이 맞지 않거나 부정확합니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};

export default usePostSearch;

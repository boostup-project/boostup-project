import { useMutation } from "@tanstack/react-query";
import postSearch from "apis/main/postSearch";

const usePostSearch = () => {
  return useMutation(postSearch, {
    onSuccess: res => {
      console.log("Searched");
    },
    onError: res => {
      console.log("Search Failed");
    },
  });
};

export default usePostSearch;

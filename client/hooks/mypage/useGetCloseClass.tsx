import { useQuery } from "@tanstack/react-query";
import getCloseClass from "apis/mypage/getCloseClass";

const useGetCloseClass = (suggestId: number) => {
  return useQuery(["get/CloseClass"], () => getCloseClass(suggestId), {
    enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetCloseClass;

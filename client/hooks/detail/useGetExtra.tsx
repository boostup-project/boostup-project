import getExtra from "apis/detail/getExtra";
import { useQuery } from "@tanstack/react-query";

const useGetExtra = (lessonId: number) => {
  return useQuery(["get/Extra"], () => getExtra(lessonId), {
    staleTime: 5 * 60000,
    cacheTime: 5 * 60000,
    retry: 10,
    enabled: false,
    onSuccess: res => {
      console.log("sucess");
    },
    onError: res => {
      console.log("failed");
    },
  });
};
export default useGetExtra;

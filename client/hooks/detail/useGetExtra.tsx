import getExtra from "apis/detail/getExtra";
import { useQuery } from "@tanstack/react-query";

const useGetExtra = (lessonId: number) => {
  return useQuery(["get/Extra"], () => getExtra(lessonId), {
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

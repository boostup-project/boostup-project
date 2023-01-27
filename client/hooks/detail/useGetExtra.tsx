import getExtra from "apis/detail/getExtra";
import { useQuery } from "react-query";

const useGetExtra = (lessonId: number) => {
  return useQuery(["get/Extra"], () => getExtra(lessonId), {
    enabled: false,
    staleTime: 5 * 60000,
    onSuccess: res => {
      console.log("sucess");
    },
    onError: res => {
      console.log("failed");
    },
  });
};
export default useGetExtra;

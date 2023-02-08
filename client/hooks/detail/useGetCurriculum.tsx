import getCurriculum from "apis/detail/getCurriculum";
import { useQuery } from "@tanstack/react-query";

const useGetCurriculum = (lessonId: number) => {
  return useQuery(["get/Curriculum"], () => getCurriculum(lessonId), {
    enabled: false,
    // staleTime: 5 * 60000,
    onSuccess: res => {
      console.log("sucess");
    },
    onError: res => {
      console.log("failed");
    },
  });
};
export default useGetCurriculum;

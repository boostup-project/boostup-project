import getCurriculum from "apis/detail/getCurriculum";
import { useQuery } from "@tanstack/react-query";

const useGetCurriculum = (lessonId: number) => {
  return useQuery(["get/Curriculum"], () => getCurriculum(lessonId), {
    enabled: false,
  });
};
export default useGetCurriculum;

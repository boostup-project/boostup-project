import { useQuery } from "@tanstack/react-query";
import getBasicInfo from "apis/detail/getBasicInfo";

const useGetBasicInfo = (lessonId: number) => {
  return useQuery(["get/detailBasicInfo"], () => getBasicInfo(lessonId), {
    // enabled: lessonId ? true : false,
    enabled: false,
    // 5분동안은 refetch 실행되지 않습니다!
    staleTime: 1000 * 60 * 5,
    cacheTime: 5 * 60000,
    // refetchOnMount: true,
  });
};

export default useGetBasicInfo;

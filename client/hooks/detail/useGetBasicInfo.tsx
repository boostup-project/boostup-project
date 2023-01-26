import { useQuery } from "react-query";
import getBasicInfo from "apis/detail/getBasicInfo";

const useGetBasicInfo = (lessonId: number) => {
  return useQuery(["get/detailBasicInfo"], () => getBasicInfo(lessonId), {
    enabled: false,
    // 5분동안은 refetch 실행되지 않습니다!
    staleTime: 60000 * 5,
    onError: err => {
      console.log(err);
    },
  });
};

export default useGetBasicInfo;

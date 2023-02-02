import { useQuery } from "@tanstack/react-query";
import getTutorInfo from "apis/mypage/getTutorInfo";

const useGetTutorInfo = (lessonId: number, tabId: number) => {
  return useQuery(["get/TutorInfo"], () => getTutorInfo(lessonId, tabId), {
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

export default useGetTutorInfo;

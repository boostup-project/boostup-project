import { useQuery } from "react-query";
import getStudentInfo from "apis/mypage/getStudentInfo";

const useGetStudentInfo = () => {
  return useQuery(["get/TutorInfo"], () => getStudentInfo(), {
    // enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetStudentInfo;

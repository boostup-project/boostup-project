import { useQuery } from "@tanstack/react-query";
import getStudentInfo from "apis/mypage/getStudentInfo";

const useGetStudentInfo = () => {
  return useQuery(["get/StudentInfo"], () => getStudentInfo(), {
    // enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    // onError: error => {
    //   console.log(error);
    // },
    retry: 1,
  });
};

export default useGetStudentInfo;

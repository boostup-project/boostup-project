import { useQuery } from "@tanstack/react-query";
import getFilteredBoard from "apis/board/getFilteredBoard";

const useGetFilteredBoard = (languageId: number) => {
  return useQuery(["get/FilteredBoard"], () => getFilteredBoard(languageId), {
    enabled: false,
    onSuccess: res => {
      console.log(res);
    },
    onError: error => {
      console.log(error);
    },
    retry: 2,
  });
};

export default useGetFilteredBoard;

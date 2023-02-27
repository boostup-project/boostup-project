import { useQuery } from "@tanstack/react-query";
import getFilteredBoard from "apis/board/getFilteredBoard";
import { useRecoilState } from "recoil";
import { mainCardInfo } from "atoms/main/mainAtom";
import { totalCard } from "atoms/main/mainAtom";
const useGetFilteredBoard = (languageId: number) => {
  const [cardInfo, setCardInfo] = useRecoilState(mainCardInfo);
  const [totalCards, setTotalCards] = useRecoilState(totalCard);
  return useQuery(["get/FilteredBoard"], () => getFilteredBoard(languageId), {
    enabled: false,
    onSuccess: res => {
      setCardInfo(res.data.data);
      setTotalCards(res.data.data.length);
    },
    onError: error => {
      console.log(error);
    },
    retry: 2,
  });
};

export default useGetFilteredBoard;

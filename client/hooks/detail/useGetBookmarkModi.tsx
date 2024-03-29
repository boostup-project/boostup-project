import { useQuery } from "@tanstack/react-query";
import getBookmarkModi from "apis/detail/getBookmarkModi";
import { useRecoilState } from "recoil";
import { refetchBookmark } from "atoms/detail/detailAtom";
const useGetBookmarkModi = (lessonId: number) => {
  const [toggle, setToggle] = useRecoilState(refetchBookmark);
  return useQuery(["get/BookmarkModi"], () => getBookmarkModi(lessonId), {
    enabled: false,
    onSuccess: res => {
      setToggle(!toggle);
    },
    onError: error => {},
    retry: 1,
  });
};

export default useGetBookmarkModi;

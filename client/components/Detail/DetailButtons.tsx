import DetailBtn from "../reuse/btn/DetailBtn";
import { IconEmptyheart, IconFullheart } from "assets/icon";
import ApplyModal from "./ApplyModal";
import Swal from "sweetalert2";
import { useQuery } from "react-query";
import { useState, useCallback } from "react";
import useDeleteDetail from "hooks/detail/useDeleteDetail";
import useGetBookmarkModi from "hooks/detail/useGetBookmarkModi";
import useGetBookmark from "hooks/detail/useGetBookmark";
import { useRouter } from "next/router";
import getBookmark from "apis/detail/getBookmark";
const DetailButtons = () => {
  const [isOpenModal, setOpenModal] = useState<boolean>(false);
  const router = useRouter();
  const lessonId = Number(router.query.id);
  const { refetch: bookmarkRefetch, data: bookmarkData } =
    useGetBookmarkModi(lessonId);

  const [mark, setMark] = useState<boolean>(true || false);
  const { isLoading, isError, data } = useQuery(
    ["getBookmark"],
    () => getBookmark(lessonId),
    {
      enabled: true,
      onSuccess: data => {
        // 성공시 호출
        console.log(data.data.bookmark);
        setMark(data.data.bookmark);
      },
      onError: error => {},
      retry: 1,
    },
  );

  const { mutate } = useDeleteDetail();
  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);

  const chatNow = () => {
    return;
  };

  const saveBookmark = () => {
    setMark(bookmarkData?.data.bookmark);
    bookmarkRefetch();
    console.log(mark, bookmarkData?.data.bookmark);
  };
  const deletePost = () => {
    Swal.fire({
      title: "과외를 삭제하시겠습니까?",
      text: "다시 되돌릴 수 없습니다.",
      icon: "question",

      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        mutate(lessonId);
        router.push("/");
        return Swal.fire({
          text: "삭제가 완료되었습니다",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };

  return isLoading ? (
    <></>
  ) : (
    <div className="flex w-full h-full flex-col ">
      {isOpenModal && <ApplyModal onClickToggleModal={onClickToggleModal} />}
      <div className="flex flex-col desktop:w-full desktop:h-fit tablet:w-[97%] w-[97%] justify-center">
        <DetailBtn bold={true} remove={false} onClick={onClickToggleModal}>
          신청하기
        </DetailBtn>
        <DetailBtn bold={false} remove={false} onClick={chatNow}>
          실시간 채팅
        </DetailBtn>
        <div className="relative justify-center items-center">
          <DetailBtn bold={false} remove={false} onClick={saveBookmark}>
            <div className="flex w-full h-1/3 absolute justify-center items-center">
              {mark ? (
                <IconFullheart width="25" heigth="25" />
              ) : (
                <IconEmptyheart width="25" heigth="25" />
              )}
              저장하기
            </div>
          </DetailBtn>
        </div>
        <div className="mt-10">
          <DetailBtn bold={false} remove={true} onClick={deletePost}>
            삭제하기
          </DetailBtn>
        </div>
      </div>
    </div>
  );
};
export default DetailButtons;

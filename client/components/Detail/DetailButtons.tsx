import DetailBtn from "../reuse/btn/DetailBtn";
import { IconEmptyheart, IconFullheart } from "assets/icon";
import ApplyModal from "./ApplyModal";
import Swal from "sweetalert2";
import { useState, useCallback, useEffect } from "react";
import useDeleteDetail from "hooks/detail/useDeleteDetail";
import useGetBookmarkModi from "hooks/detail/useGetBookmarkModi";
import useGetBookmark from "hooks/detail/useGetBookmark";
import { useRouter } from "next/router";
import { useRecoilValue } from "recoil";
import { refetchBookmark } from "atoms/detail/detailAtom";
const DetailButtons = (basicInfo: any) => {
  const [isOpenModal, setOpenModal] = useState<boolean>(false);

  const toggle = useRecoilValue(refetchBookmark);

  const router = useRouter();
  const lessonId = Number(router.query.id);
  const { refetch: bookmarkRefetch } = useGetBookmarkModi(lessonId);
  const {
    data: bookmarkData,
    isLoading,
    refetch: getBookmark,
  } = useGetBookmark(lessonId);
  const [mark, setMark] = useState<boolean>(false);

  useEffect(() => {
    getBookmark();
  }, [toggle]);

  useEffect(() => {
    setMark(bookmarkData?.data.bookmark);
  }, [bookmarkData]);

  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);

  const chatNow = () => {
    return;
  };

  const saveBookmark = (lessonId: any) => {
    if (localStorage.getItem("token")) {
      bookmarkRefetch(lessonId);
    } else {
      return Swal.fire({
        text: "로그인 후 원하는 선생님을 찜 해보세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  };

  const { mutate } = useDeleteDetail();
  const deletePost = (lessonId: number) => {
    console.log(lessonId);
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
    <div className="flex w-full h-full flex-col desktop:mt-0 mt-3">
      {isOpenModal && <ApplyModal onClickToggleModal={onClickToggleModal} />}
      <div className="flex flex-col desktop:w-full desktop:h-fit tablet:w-[97%] w-[97%] justify-center items-center">
        <DetailBtn bold={true} remove={false} onClick={onClickToggleModal}>
          신청하기
        </DetailBtn>
        <DetailBtn bold={false} remove={false} onClick={chatNow}>
          실시간 채팅
        </DetailBtn>
        <div className="relative justify-center items-center w-full flex flex-col">
          <DetailBtn
            bold={false}
            remove={false}
            onClick={() => saveBookmark(lessonId)}
          >
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
        {basicInfo.basicInfo?.editable ? (
          <div className="mt-10 w-full flex flex-col justify-center items-center">
            <DetailBtn
              bold={false}
              remove={true}
              onClick={() => deletePost(lessonId)}
            >
              삭제하기
            </DetailBtn>
          </div>
        ) : null}
      </div>
    </div>
  );
};
export default DetailButtons;

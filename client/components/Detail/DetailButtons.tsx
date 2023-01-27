import DetailBtn from "../reuse/btn/DetailBtn";
import { IconEmptyheart, IconFullheart } from "assets/icon";
import ApplyModal from "./ApplyModal";
import { powerApplyModal } from "atoms/detail/detailAtom";
import { useRecoilState } from "recoil";
import detailDelete from "apis/detail/deleteDetail";
import Swal from "sweetalert2";
import { useState, useCallback } from "react";
import useDetailDelete from "hooks/detail/useDetailDelete";
import { useRouter } from "next/router";
const DetailButtons = () => {
  const [isOpenModal, setOpenModal] = useState<boolean>(false);
  const router = useRouter();

  const { mutate } = useDetailDelete();
  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);

  const chatNow = () => {
    return;
  };

  const bookmarking = () => {
    return;
  };
  const deletePost = () => {
    //삭제기능테스트완료했으나 lessonId를 받아오고,
    //토큰이 없는 경우 삭제버튼이 보이지 않는 기능 구현이 필요
    const lessonId = 3;
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
          text: "삭제완료",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };

  return (
    <div className="flex w-full h-full flex-col ">
      {isOpenModal && <ApplyModal onClickToggleModal={onClickToggleModal} />}
      <div className="flex flex-col desktop:w-1/5 desktop:h-fit tablet:w-[97%] w-[97%] justify-center">
        <DetailBtn bold={true} remove={false} onClick={onClickToggleModal}>
          신청하기
        </DetailBtn>
        <DetailBtn bold={false} remove={false} onClick={chatNow}>
          실시간 채팅
        </DetailBtn>
        <div className="relative justify-center items-center">
          <DetailBtn bold={false} remove={false} onClick={bookmarking}>
            <div className="flex w-1/2 h-1/3 absolute justify-center items-center">
              {/* {false ? (
                <IconFullheart width="25" heigth="25" />
              ) : (
                <IconEmptyheart width="25" heigth="25" />
              )} */}
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

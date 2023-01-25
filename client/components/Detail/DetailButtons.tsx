import DetailBtn from "../reuse/btn/DetailBtn";
import { IconEmptyheart, IconFullheart } from "assets/icon";
import ApplyModal from "./ApplyModal";
import { powerApplyModal } from "atoms/detail/detailAtom";
import { useRecoilState } from "recoil";
import detailDelete from "apis/detail/detailDelete";
import Swal from "sweetalert2";
const DetailButtons = () => {
  const [powerApply, setPowerApply] = useRecoilState(powerApplyModal);

  const toApply = () => {
    setPowerApply(prev => !prev);
  };
  const chatNow = () => {
    return;
  };

  const bookmarking = () => {
    return;
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
        detailDelete;
      }
    });
  };

  return (
    <>
      {powerApply && <ApplyModal />}
      <div className="flex flex-col w-1/5 h-fit">
        <DetailBtn bold={true} remove={false} onClick={toApply}>
          신청하기
        </DetailBtn>
        <DetailBtn bold={false} remove={false} onClick={chatNow}>
          실시간 채팅
        </DetailBtn>
        <div className="relative">
          <div className="flex w-1/3 h-1/3 absolute top-4 left-8">
            {true ? <IconFullheart /> : <IconEmptyheart />}
          </div>

          <DetailBtn bold={false} remove={false} onClick={bookmarking}>
            선생님 찜하기
          </DetailBtn>
        </div>
        <div className="mt-10">
          <DetailBtn bold={false} remove={true} onClick={deletePost}>
            삭제하기
          </DetailBtn>
        </div>
      </div>
    </>
  );
};
export default DetailButtons;

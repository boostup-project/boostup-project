import DetailBtn from "../reuse/btn/DetailBtn";
import { IconEmptyheart, IconFullheart } from "assets/icon";
const DetailButtons = () => {
  const apply = () => {
    return;
  };
  const chatNow = () => {
    return;
  };

  const bookmarking = () => {
    return;
  };
  const deletePost = () => {
    return;
  };

  return (
    <div className="flex flex-col w-1/5 h-fit">
      <DetailBtn bold={true} remove={false} onClick={apply}>
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
  );
};
export default DetailButtons;

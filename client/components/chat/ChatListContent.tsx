interface Props {
  onClick: () => void;
}

const ChatListContent = ({ onClick }: Props) => {
  return (
    <>
      <div
        className="flex flex-row w-[95%] h-14 border-b border-borderColor/30 px-3 py-2 hover:bg-pointColor/5 active:bg-pointColor/10"
        onClick={onClick}
      >
        {/* 채팅방 이름, 최근 메세지 */}
        <div className="flex flex-col w-4/5 h-full justify-center items-start">
          <div className="w-fit h-fit font-SCDream5 text-textColor text-sm">
            과외쌤 이름
          </div>
          <div className="w-fit h-fit font-SCDream3 text-textColor text-xs mt-1">
            가장 최근 메세지
          </div>
        </div>
        {/* 메세지 수신/발신 시간 */}
        <div className="flex flex-col w-2/5 h-full justify-start items-end">
          <div className="w-fit h-fit font-SCDream3 text-textColor/70 text-[10px] ">
            오후 4시 24분
          </div>
          <div className="w-4 h-4 p-1 rounded-full mt-2 text-white font-SCDream5 text-[10px] bg-pointColor flex flex-col justify-center items-center">
            N
          </div>
        </div>
      </div>
    </>
  );
};

export default ChatListContent;

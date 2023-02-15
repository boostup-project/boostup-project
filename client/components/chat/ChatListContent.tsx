import useTranslateTIme from "hooks/chat/useTranslateTime";

interface Props {
  onClick: () => void;
  displayName: string;
  message: string;
  createAt: string;
  key: number;
  alarmCount: number;
}

const ChatListContent = ({
  onClick,
  displayName,
  message,
  createAt,
  alarmCount,
}: Props) => {
  let currTime = new Date();
  let createTime = new Date(createAt);

  const utc = createTime.getTime() + createTime.getTimezoneOffset() * 60 * 1000;
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000 + 22 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  // 메시지 뒷부분 자르기
  let finalMessage = "";
  if (message?.length > 16) {
    finalMessage += `${message.slice(0, 16)}...`;
  } else {
    finalMessage = message;
  }

  return (
    <>
      <div
        className="flex flex-row w-[95%] h-14 border-b border-borderColor/30 px-3 py-2 hover:bg-pointColor/5 active:bg-pointColor/10"
        onClick={onClick}
      >
        {/* 채팅방 이름, 최근 메세지 */}
        <div className="flex flex-col w-4/5 h-full justify-center items-start">
          <div className="flex flex-col justify-center items-center w-fit h-fit font-SCDream5 text-textColor text-sm">
            {displayName}
          </div>
          <div className="flex flex-col justify-center items-center w-fit h-fit font-SCDream3 text-textColor text-xs mt-1 pb-1">
            {finalMessage}
          </div>
        </div>
        {/* 메세지 수신/발신 시간 */}
        <div className="flex flex-col w-2/5 h-full justify-start items-end">
          <div className="w-fit h-fit font-SCDream3 text-textColor/70 text-[10px] ">
            {useTranslateTIme({ currTime, kr_curr })}
          </div>
          {alarmCount > 0 ? (
            <div className="w-4 h-4 p-1 rounded-full mt-2 text-white font-SCDream5 text-[10px] bg-pointColor flex flex-col justify-center items-center">
              N
            </div>
          ) : null}
        </div>
      </div>
    </>
  );
};

export default ChatListContent;

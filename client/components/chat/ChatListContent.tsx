interface Props {
  onClick: () => void;
  displayName: string;
  message: string;
  createAt: string;
  key: number;
}

const ChatListContent = ({
  onClick,
  displayName,
  message,
  createAt,
  key,
}: Props) => {
  let currTime = new Date();
  let createTime = new Date(createAt);

  const utc = createTime.getTime() + createTime.getTimezoneOffset() * 60 * 1000;
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000 + 22 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  let [currYear, currMonth, currDay, currHour, currMinute, currSecond] = [
    currTime.getFullYear(),
    currTime.getMonth() + 1,
    currTime.getDate(),
    currTime.getHours(),
    currTime.getMinutes(),
    currTime.getSeconds(),
  ];

  let [
    createYear,
    createMonth,
    createDay,
    createHour,
    createMinute,
    createSecond,
  ] = [
    kr_curr.getFullYear(),
    kr_curr.getMonth() + 1,
    kr_curr.getDate(),
    kr_curr.getHours(),
    kr_curr.getMinutes(),
    kr_curr.getSeconds(),
  ];

  let finalCreatedAt = "";

  if (
    currYear === createYear &&
    currMonth === createMonth &&
    currDay === createDay
  ) {
    if (createHour > 12) {
      if (createHour - 12 < 10 && createMinute < 10) {
        finalCreatedAt = `오후 0${createHour - 12}:0${createMinute}`;
      } else if (createHour - 12 < 10) {
        finalCreatedAt = `오후 0${createHour - 12}:${createMinute}`;
      } else if (createMinute < 10) {
        finalCreatedAt = `오후 ${createHour - 12}:0${createMinute}`;
      }
    } else {
      if (createHour < 10 && createMinute < 10) {
        finalCreatedAt = `오전 0${createHour}:0${createMinute}`;
      } else if (createHour < 10) {
        finalCreatedAt = `오전 0${createHour}:${createMinute}`;
      } else if (createMinute < 10) {
        finalCreatedAt = `오전 ${createHour}:0${createMinute}`;
      }
    }
  } else if (
    currYear === createYear &&
    currMonth === createMonth &&
    currDay - createDay === 1
  ) {
    finalCreatedAt = `어제`;
  } else {
    finalCreatedAt = `${createYear}-${createMonth}-${createDay}`;
  }

  // 메시지 뒷부분 자르기
  let finalMessage = "";
  if (message.length > 16) {
    finalMessage += `${message.slice(0, 16)}...`;
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
            {`${finalCreatedAt}`}
          </div>
          {/* <div className="w-4 h-4 p-1 rounded-full mt-2 text-white font-SCDream5 text-[10px] bg-pointColor flex flex-col justify-center items-center">
            N
          </div> */}
        </div>
      </div>
    </>
  );
};

export default ChatListContent;

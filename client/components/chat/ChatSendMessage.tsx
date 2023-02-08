interface Props {
  content: string;
  time: string;
}

const ChatSendMessage = ({ content, time }: Props) => {
  let currTime = new Date();
  let createTime = new Date(time);

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

  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-start mt-3">
        <div className="w-full h-fit flex flex-row justify-end items-end">
          {/* time */}
          <div className="w-fit h-fit flex flex-col justify-start items-end font-SCDream3 text-textColor text-[10px] mr-3">
            {finalCreatedAt}
          </div>
          <div className="w-fit h-fit flex flex-col justify-center items-center p-2 bg-pointColor rounded-xl font-SCDream3 text-white text-sm">
            {content}
          </div>
        </div>
      </div>
    </>
  );
};

export default ChatSendMessage;

import useTranslateTIme from "hooks/chat/useTranslateTime";

interface Props {
  content: string;
  time: string;
}

const ChatNoticeMessage = ({ content, time }: Props) => {
  let currTime = new Date();
  let createTime = new Date(time);

  const utc = createTime.getTime() + createTime.getTimezoneOffset() * 60 * 1000;
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000 + 22 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-center my-3">
        <div className="w-2/3 h-fit font-SCDream3 text-[10px] text-textColor/80 flex flex-col justify-center items-center rounded-xl p-1">
          {useTranslateTIme({ currTime, kr_curr })}
        </div>
        <div className="w-2/3 h-fit font-SCDream3 text-xs text-white bg-textColor/30 flex flex-col justify-center items-center rounded-xl p-1">
          {content}
        </div>
      </div>
    </>
  );
};

export default ChatNoticeMessage;

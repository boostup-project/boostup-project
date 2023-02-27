import useTranslateTIme from "hooks/chat/useTranslateTime";

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

  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-start mt-3">
        <div className="w-full h-fit flex flex-row justify-end items-end">
          {/* time */}
          <div className="w-fit h-fit flex flex-col justify-start items-end font-SCDream3 text-textColor text-[10px] mr-3">
            {useTranslateTIme({ currTime, kr_curr })}
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

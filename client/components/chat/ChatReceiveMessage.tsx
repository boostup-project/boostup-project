import useTranslateTIme from "hooks/chat/useTranslateTime";

interface Props {
  name: string;
  content: string;
  time: string;
}

const ChatReceiveMessage = ({ name, content, time }: Props) => {
  let currTime = new Date();
  let createTime = new Date(time);

  const utc = createTime.getTime() + createTime.getTimezoneOffset() * 60 * 1000;
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000 + 22 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-start mt-3">
        <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-textColor text-xs mb-1">
          {name}
        </div>
        <div className="w-full h-fit flex flex-row justify-start items-end">
          <div className="max-w-[80%] w-fit h-fit flex flex-col justify-center items-center p-2 bg-bgColor rounded-xl font-SCDream3 text-textColor border border-borderColor text-sm mr-3">
            {content}
          </div>
          {/* time */}
          <div className="w-fit h-fit flex flex-col justify-start items-end font-SCDream3 text-textColor text-[10px]">
            {useTranslateTIme({ currTime, kr_curr })}
          </div>
        </div>
      </div>
    </>
  );
};

export default ChatReceiveMessage;

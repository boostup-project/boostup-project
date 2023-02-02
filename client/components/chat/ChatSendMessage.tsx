interface Props {
  content: string;
  time: string;
}

const ChatSendMessage = ({ content, time }: Props) => {
  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-start mt-3">
        {/* <div className="w-full h-fit flex flex-col justify-center items-end font-SCDream3 text-textColor text-xs">
          박성훈
        </div> */}
        <div className="w-full h-fit flex flex-row justify-end items-end">
          {/* time */}
          <div className="w-fit h-fit flex flex-col justify-start items-end font-SCDream3 text-textColor text-[10px] mr-3">
            {time}
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

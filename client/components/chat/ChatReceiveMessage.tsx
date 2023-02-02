interface Props {
  name: string;
  content: string;
  time: string;
}

const ChatReceiveMessage = ({ name, content, time }: Props) => {
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
            {time}
          </div>
        </div>
      </div>
    </>
  );
};

export default ChatReceiveMessage;

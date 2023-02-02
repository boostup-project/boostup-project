import ChatSendMessage from "./ChatSendMessage";
import ChatReceiveMessage from "./ChatReceiveMessage";

const ChatContent = () => {
  return (
    <>
      <div className="w-full tablet:h-4/5 h-[82%] flex flex-col justify-start items-center pb-3 px-3 overflow-auto">
        <ChatSendMessage content={"안녕하세요"} time={"오후 12:38"} />
        <ChatReceiveMessage
          name={"박성훈"}
          content={"안녕하세요"}
          time={"오후 12:39"}
        />
        <ChatSendMessage content={"졸려죽겠어요"} time={"오후 12:40"} />
        <ChatReceiveMessage
          name={"박성훈"}
          content={"그럼 주무시죠"}
          time={"오후 12:41"}
        />
        <ChatSendMessage content={"네 그럴려고요"} time={"오후 12:38"} />
        <ChatReceiveMessage
          name={"박성훈"}
          content={
            "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ"
          }
          time={"오후 12:39"}
        />
        <ChatSendMessage content={"왜 웃으시죠?"} time={"오후 12:38"} />
        <ChatReceiveMessage
          name={"박성훈"}
          content={"그냥 해본거에요"}
          time={"오후 12:39"}
        />
      </div>
    </>
  );
};

export default ChatContent;

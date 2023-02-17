import ChatSendMessage from "./ChatSendMessage";
import ChatReceiveMessage from "./ChatReceiveMessage";
import ChatNoticeMessage from "./ChatNoticeMessage";
import { useEffect, useRef } from "react";

interface Props {
  chatList: any;
}

const ChatContent = ({ chatList }: Props) => {
  const list = [...chatList].reverse();
  const messageBoxRef = useRef<any>();

  const scrollToBottom = () => {
    if (messageBoxRef.current) {
      messageBoxRef.current.scrollTop = messageBoxRef.current.scrollHeight;
    }
  };

  useEffect(() => {
    scrollToBottom();
  }, [list]);

  return (
    <>
      <div
        ref={messageBoxRef}
        className="w-full tablet:h-4/5 h-[82%] flex flex-col justify-start items-center pb-3 px-3 overflow-auto"
      >
        {list.map((el: any, idx: number) => {
          return (
            <div
              className="w-full h-fit flex flex-col justify-center items-center"
              key={idx}
            >
              {el.messageType === "ALARM" ? (
                <ChatNoticeMessage content={el.message} time={el.createdAt} />
              ) : el.displayName ===
                localStorage.getItem("name")?.toString() ? (
                <ChatSendMessage content={el.message} time={el.createdAt} />
              ) : (
                <ChatReceiveMessage
                  name={el.displayName}
                  content={el.message}
                  time={el.createdAt}
                />
              )}
            </div>
          );
        })}
      </div>
    </>
  );
};

export default ChatContent;

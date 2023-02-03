import ChatSendMessage from "./ChatSendMessage";
import ChatReceiveMessage from "./ChatReceiveMessage";

interface Props {
  chatList: any;
}

const ChatContent = ({ chatList }: Props) => {
  return (
    <>
      <div className="w-full tablet:h-4/5 h-[82%] flex flex-col justify-start items-center pb-3 px-3 overflow-auto">
        {chatList.map((el: any) => {
          return (
            <>
              {el.user === localStorage.getItem("name")?.toString() ? (
                <ChatSendMessage content={el.comment} time={"오후 12:30"} />
              ) : (
                <ChatReceiveMessage
                  name={el.user}
                  content={el.comment}
                  time={"오후 12:39"}
                />
              )}
            </>
          );
        })}
      </div>
    </>
  );
};

export default ChatContent;

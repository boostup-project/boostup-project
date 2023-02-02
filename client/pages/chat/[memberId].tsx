import ChatContainer from "components/chat/ChatContainer";
import DesktopChatComp from "components/chat/DesktopChatComp";
import MobileChatComp from "components/chat/MobileChatComp";
import useWindowSize from "hooks/useWindowSize";

const Chat = () => {
  const widthSize = useWindowSize();

  return (
    <>
      <div className="w-full h-screen flex flex-col justify-start items-center pt-2">
        <ChatContainer>
          {widthSize > 764 ? <DesktopChatComp /> : <MobileChatComp />}
        </ChatContainer>
      </div>
    </>
  );
};

export default Chat;

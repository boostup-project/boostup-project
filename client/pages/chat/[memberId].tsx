import ChatContainer from "components/chat/ChatContainer";
import DesktopChatComp from "components/chat/DesktopChatComp";
import MobileChatComp from "components/chat/MobileChatComp";
import useWindowSize from "hooks/useWindowSize";
import { connectSocket, subscribeRoomList } from "hooks/chat/socket";
import { useState } from "react";
import { SyncLoader } from "react-spinners";
import { useEffect } from "react";

const Chat = () => {
  const widthSize = useWindowSize();
  const [connectState, setConnectState] = useState<boolean>(false);
  const [subscribeState, setSubscribeState] = useState<boolean>(false);

  const handleConnectSocket = (state: boolean) => {
    setConnectState(state);
  };

  useEffect(() => {
    connectSocket({ handleConnectSocket });

    if (connectState) {
      setTimeout(() => {
        subscribeRoomList(Number(localStorage.getItem("memberId")));
        setSubscribeState(true);
      }, 5000);
    }
  }, [connectState]);

  return (
    <>
      {subscribeState ? (
        <div className="w-full h-screen flex flex-col justify-start items-center pt-2">
          <ChatContainer>
            {widthSize > 764 ? <DesktopChatComp /> : <MobileChatComp />}
          </ChatContainer>
        </div>
      ) : connectState ? (
        <div className="w-screen h-screen flex flex-col justify-center items-center">
          <SyncLoader color="#0A65F2" />
          <div className="w-full h-fit flex flex-col justify-center items-center font-SCDream3 text-xl text-textColor mt-5">
            채팅목록을 불러오고 있어요🙂
          </div>
        </div>
      ) : (
        <div className="w-screen h-screen flex flex-col justify-center items-center">
          <SyncLoader color="#0A65F2" />
          <div className="w-full h-fit flex flex-col justify-center items-center font-SCDream3 text-xl text-textColor mt-5">
            서버에 연결하고 있어요🙂
          </div>
        </div>
      )}
    </>
  );
};

export default Chat;

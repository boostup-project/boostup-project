import ChatContainer from "components/chat/ChatContainer";
import DesktopChatComp from "components/chat/DesktopChatComp";
import MobileChatComp from "components/chat/MobileChatComp";
import useWindowSize from "hooks/useWindowSize";
import { connectSocket, subscribeRoomList } from "hooks/chat/socket";
import { chatRoomListState, newChatRoomState } from "atoms/chat/chatAtom";
import { useRecoilState } from "recoil";
import { useState } from "react";
import { SyncLoader } from "react-spinners";
import { useEffect } from "react";
import SeoHead from "components/reuse/SEO/SeoHead";

const Chat = () => {
  const widthSize = useWindowSize();
  const [connectState, setConnectState] = useState<boolean>(false);
  const [subscribeState, setSubscribeState] = useState<boolean>(false);
  const [newChatRoom, setnewChatRoom] = useRecoilState(newChatRoomState);
  const [chatRoomList, setchatRoomList] = useRecoilState(chatRoomListState);

  const handleConnectSocket = (state: boolean) => {
    setConnectState(state);
  };

  const handleSubChatRoom = (data: any) => {
    setnewChatRoom(data);
  };

  useEffect(() => {
    connectSocket({ handleConnectSocket });
    let memberId = Number(localStorage.getItem("memberId"));

    if (connectState) {
      setTimeout(() => {
        subscribeRoomList({ memberId, handleSubChatRoom });
        setSubscribeState(true);
      }, 5000);
    }
  }, [connectState]);

  useEffect(() => {
    if (newChatRoom) {
      // chatRoomList 깊은복사
      let currData = chatRoomList.slice();
      let elIdx = 0;
      let currReceiverId = 0;

      currData.forEach((el: any, idx: number) => {
        if (el.chatRoomId === newChatRoom.chatRoomId) {
          elIdx = idx;
          currReceiverId = el.receiverId;
        }
      });

      let name = currData[elIdx]?.displayName;
      let { alarmCount, chatRoomId, createdAt, latestMessage } = newChatRoom;

      const newData = {
        alarmCount,
        chatRoomId,
        createdAt,
        latestMessage,
        receiverId: currReceiverId,
        displayName: name,
      };

      currData.splice(elIdx, 1);
      currData.unshift(newData);

      setchatRoomList(currData);
    }
  }, [newChatRoom]);

  return (
    <>
      <SeoHead metaType="Chat" />
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

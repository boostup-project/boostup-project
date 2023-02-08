import ChatListContent from "./ChatListContent";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { chatActive, chatListState, newDataState } from "atoms/chat/chatAtom";
import useGetChatRoomList from "hooks/chat/useGetChatRoomList";
import { subscribeRoom, unSubscribeRoom } from "hooks/chat/socket";
import useGetChatList from "hooks/chat/useGetChatList";

const ChatListComp = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [roomId, setRoomId] = useState<number>(0);
  const [newData, setNewData] = useRecoilState(newDataState);
  const [chatList, setChatList] = useRecoilState(chatListState);

  const { refetch: chatRoomFetch, data } = useGetChatRoomList();
  const { refetch: chatListFetch } = useGetChatList(roomId);

  const handleSocketData = (data: any) => {
    setNewData(data);
  };

  const handleClickChat = (roomId: number) => {
    if (active) {
      unSubscribeRoom();
    }
    setActive(true);
    subscribeRoom({ roomId, handleSocketData });
    setRoomId(roomId);
  };

  useEffect(() => {
    chatListFetch();
  }, [roomId]);

  useEffect(() => {
    if (newData) {
      setChatList([newData, ...chatList]);
    }
  }, [newData]);

  let chatRoomList: any = [];

  if (data) {
    chatRoomList = [...data?.data];
  }

  return (
    <>
      <div className="tablet:w-2/5 w-full h-full overflow-auto flex flex-col justify-start items-center bg-borderColor/5">
        <div className="w-[95%] min-h-[50px] flex flex-row justify-center items-center border-b border-borderColor">
          <div className="w-fit h-fit font-SCDream4 text-textColor tablet:text-md text-sm">
            {localStorage.getItem("name")}
          </div>
        </div>
        {/* map */}
        {chatRoomList.map(
          (el: {
            chatRoomId: number;
            displayName: string;
            message: string;
            createdAt: string;
          }) => {
            return (
              <ChatListContent
                key={el.chatRoomId}
                onClick={() => handleClickChat(el.chatRoomId)}
                displayName={el.displayName}
                message={el.message}
                createAt={el.createdAt}
              />
            );
          },
        )}
      </div>
    </>
  );
};

export default ChatListComp;

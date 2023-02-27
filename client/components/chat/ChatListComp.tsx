import ChatListContent from "./ChatListContent";
import { useEffect } from "react";
import { useRecoilState } from "recoil";
import {
  chatActive,
  chatListState,
  newDataState,
  roomIdState,
  chatDisplayName,
  receiverIdState,
  chatRoomListState,
} from "atoms/chat/chatAtom";
import useGetChatRoomList from "hooks/chat/useGetChatRoomList";
import useGetResetChatAlarm from "hooks/chat/useGetResetChatAlarm";
import { subscribeRoom, unSubscribeRoom } from "hooks/chat/socket";
import useGetChatList from "hooks/chat/useGetChatList";

const ChatListComp = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [roomId, setRoomId] = useRecoilState(roomIdState);
  const [newData, setNewData] = useRecoilState(newDataState);
  const [chatList, setChatList] = useRecoilState(chatListState);
  const [chatRoomName, setChatRoomName] = useRecoilState(chatDisplayName);
  const [receiverId, setReceiverId] = useRecoilState(receiverIdState);
  const [chatRoomList, setChatRoomList] = useRecoilState(chatRoomListState);

  useGetChatRoomList();
  const { refetch: chatListFetch } = useGetChatList(roomId);
  const { refetch: chatRoomAlarmResetFetch } = useGetResetChatAlarm(roomId);

  const handleSocketData = (data: any) => {
    setNewData(data);
  };

  const handleClickChat = (
    roomIdNum: number,
    displayName: string,
    receiverIdNum: number,
  ) => {
    if (active) {
      unSubscribeRoom();
    }
    subscribeRoom({ roomIdNum, handleSocketData });
    setRoomId(roomIdNum);
    setReceiverId(receiverIdNum);
    setChatRoomName(displayName);
  };

  useEffect(() => {
    if (roomId !== 0) {
      chatRoomAlarmResetFetch();
      chatListFetch();
    }
  }, [roomId]);

  useEffect(() => {
    if (newData) {
      setChatList([newData, ...chatList]);
    }
  }, [newData]);

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
          (
            el: {
              receiverId: number;
              chatRoomId: number;
              displayName: string;
              latestMessage: string;
              createdAt: string;
              alarmCount: number;
            },
            idx: number,
          ) => {
            return (
              <ChatListContent
                key={idx}
                onClick={() =>
                  handleClickChat(el.chatRoomId, el.displayName, el.receiverId)
                }
                displayName={el.displayName}
                message={el.latestMessage}
                createAt={el.createdAt}
                alarmCount={el.alarmCount}
              />
            );
          },
        )}
      </div>
    </>
  );
};

export default ChatListComp;

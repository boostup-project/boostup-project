import BackIcon from "assets/icon/BackIcon";
import { useRecoilState } from "recoil";
import { useEffect } from "react";
import { chatActive } from "atoms/chat/chatAtom";
import ChatContent from "./ChatContent";
import {
  chatListState,
  roomIdState,
  newDataState,
  chatDisplayName,
  receiverIdState,
} from "atoms/chat/chatAtom";
import { sendMsg, subscribeRoom, unSubscribeRoom } from "hooks/chat/socket";
import useWindowSize from "hooks/useWindowSize";
import useGetChatList from "hooks/chat/useGetChatList";
import { useForm } from "react-hook-form";

const ChatComp = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [chatList, setChatList] = useRecoilState(chatListState);
  const [roomIdNum, setRoomId] = useRecoilState(roomIdState);
  const [chatRoomName, setChatRoomName] = useRecoilState(chatDisplayName);
  const [receiverId, setReceiverId] = useRecoilState(receiverIdState);

  const [newData, setNewData] = useRecoilState(newDataState);

  const { refetch: chatListFetch } = useGetChatList(roomIdNum);

  let windowSize = useWindowSize();

  const { handleSubmit, register, setValue } = useForm();

  const handleSocketData = (data: any) => {
    setNewData(data);
  };

  const handleClickCancel = () => {
    setActive(false);
    unSubscribeRoom();
    setRoomId(0);
    setReceiverId(0);
  };

  const onSubmit = (data: any) => {
    let inputValue = data.value;
    sendMsg(roomIdNum, inputValue, receiverId);
    setValue("value", "");
  };

  useEffect(() => {
    if (windowSize < 764) {
      if (active) {
        unSubscribeRoom();
        chatListFetch();
      }

      subscribeRoom({ roomIdNum, handleSocketData });
    }
  }, []);

  useEffect(() => {
    if (newData) {
      setChatList([newData, ...chatList]);
    }
  }, [newData]);

  return (
    <>
      <div className="tablet:w-2/3 w-full h-full overflow-auto flex flex-col justify-start items-center">
        {/* header */}
        <div className="w-[95%] h-[50px] flex flex-row justify-center items-center border-b border-borderColor/30">
          <div className="w-fit h-fit font-SCDream4 text-textColor tablet:text-md text-sm">
            {chatRoomName}
          </div>
          <div
            className="absolute right-6 w-fit h-fit cursor-pointer"
            onClick={handleClickCancel}
          >
            <BackIcon />
          </div>
        </div>
        {/* content */}
        <ChatContent chatList={chatList} />
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-row justify-center items-center w-[95%] h-[10%] border-t border-borderColor/30 "
        >
          <input
            type="text"
            autoComplete="off"
            placeholder="메세지를 입력하세요"
            className="w-full h-full rounded-xl outline-none bg-bgColor font-SCDream3 text-textColor text-sm p-3"
            {...register("value", { required: "true" })}
          />
          <button className="w-16 h-fit p-2 bg-pointColor rounded-md font-SCDream3 text-xs text-white flex flex-col justify-center items-center">
            전송
          </button>
        </form>
      </div>
    </>
  );
};

export default ChatComp;

import ChatListContent from "./ChatListContent";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { chatActive } from "atoms/chat/chatAtom";

const ChatListComp = () => {
  const [name, setName] = useState<string | null>("");
  const [active, setActive] = useRecoilState(chatActive);

  const handleClickChat = () => {
    setActive(true);
  };

  useEffect(() => {
    setName(localStorage.getItem("name"));
  }, []);

  return (
    <>
      <div className="tablet:w-1/3 w-full h-full overflow-auto flex flex-col justify-start items-center bg-borderColor/5">
        <div className="w-[95%] min-h-[50px] flex flex-row justify-center items-center border-b border-borderColor">
          <div className="w-fit h-fit font-SCDream4 text-textColor tablet:text-md text-sm">
            {name}
          </div>
        </div>
        {/* map */}
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
        <ChatListContent onClick={handleClickChat} />
      </div>
    </>
  );
};

export default ChatListComp;

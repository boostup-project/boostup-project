import ChatListComp from "./ChatListComp";
import ChatComp from "./ChatComp";
import { useRecoilState } from "recoil";
import { chatActive } from "atoms/chat/chatAtom";

const MobileChatComp = () => {
  const [active, setActive] = useRecoilState(chatActive);

  return (
    <>
      <div className="w-full h-full flex flex-col justify-start items-start">
        {active ? <ChatComp /> : <ChatListComp />}
      </div>
    </>
  );
};

export default MobileChatComp;

import ChatListComp from "./ChatListComp";
import ChatComp from "./ChatComp";
import Image from "next/image";
import Logo from "../../public/images/logo.png";
import { useRecoilState } from "recoil";
import { chatActive } from "atoms/chat/chatAtom";

const DesktopChatComp = () => {
  const [active, setActive] = useRecoilState(chatActive);
  return (
    <>
      <div className="w-full h-full flex flex-row justify-center items-start">
        <ChatListComp />
        {active ? (
          <ChatComp />
        ) : (
          <div className="w-2/3 h-full overflow-auto flex flex-col justify-center items-center">
            <Image
              src={Logo}
              width={100}
              height={100}
              alt="logo"
              className="opacity-70"
            />
          </div>
        )}
      </div>
    </>
  );
};

export default DesktopChatComp;

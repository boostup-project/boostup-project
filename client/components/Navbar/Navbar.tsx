import {
  IconChat,
  IconCode,
  IconHome,
  IconProfile,
  IconPlus,
} from "assets/icon/";
import { filterModal, isWrite, powerWriteModal } from "atoms/main/mainAtom";
import WriteModal from "components/createModal/WriteModal";
import Link from "next/link";
import { useRecoilState, useRecoilValue } from "recoil";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useEffect } from "react";
import usePostWrite from "hooks/usePostWrite";

const navContents = [
  {
    type: "Link",
    image: <IconHome />,
    link: "/",
  },
  {
    type: "Link",
    image: <IconProfile />,
    link: "/",
  },
  {
    type: "Write",
    image: <IconPlus />,
  },
  {
    type: "Link",
    image: <IconChat />,
    link: "/",
  },
  {
    type: "Filter",
    image: <IconCode />,
  },
];

const Navbar = () => {
  const [powerWrite, setPowerIsWrite] = useRecoilState(powerWriteModal);
  const [modal, setModal] = useRecoilState(filterModal);
  const [isWritten, setIsWritten] = useRecoilState(isWrite);
  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    setPowerIsWrite(prev => !prev);
  };

  /** 연휴 서버 켜져있을 때 테스트할 것
   * Modal이 제출되고 ModalBackDrop이 사라지고 메인 화면에 제출 완료라는 알람이 떴으면 좋겠음
   * 따라서, 메인과 연관되어 있는 네브바에서 props drilling을 통해 mutate함수를 curriculum까지 내려주고 isSuccess를 받게 되면 현 문서에서 alert를 출력하는 것을 구현 예정
   * **/
  const { mutate, isSuccess, isError } = usePostWrite();

  // 하단 Navbar의 필터 버튼 클릭시 10~16줄 코드를 넣어주시면 됩니다!
  const handleModalClick = () => {
    if (modal === 0) {
      setModal(48);
    } else {
      setModal(0);
    }
  };

  return (
    <>
      <ToastContainer />
      {powerWrite && <WriteModal mutate={mutate} />}
      <div className="bg-white w-full h-16 flex justify-center items-center sticky bottom-0  desktop:hidden">
        {navContents.map((content, i) => (
          <div key={i} className="w-1/5 flex justify-center items-center">
            {content.type === "Link" ? (
              <div className="w-7">
                <Link href={content.link!}>{content.image}</Link>
              </div>
            ) : content.type === "Write" ? (
              <div
                className="w-12 h-12 rounded-3xl bg-pointColor flex justify-center items-center"
                onClick={e => toWrite(e)}
              >
                <div className="w-5">{content.image}</div>
              </div>
            ) : (
              <div className="w-7" onClick={handleModalClick}>
                <div>{content.image}</div>
              </div>
            )}
          </div>
        ))}
      </div>
    </>
  );
};
export default Navbar;

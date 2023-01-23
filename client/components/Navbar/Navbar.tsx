import {
  IconChat,
  IconCode,
  IconHome,
  IconProfile,
  IconPlus,
} from "assets/icon/";
import { useEffect, useRef } from "react";
import { filterModal, isWrite, powerWriteModal } from "atoms/main/mainAtom";
import { useRecoilState, useRecoilValue } from "recoil";
import { toast, ToastContainer } from "react-toastify";
import WriteModal from "components/createModal/WriteModal";
import Link from "next/link";
import "react-toastify/dist/ReactToastify.css";

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
  const [isPowerWrite, setIsPowerWrite] = useRecoilState(powerWriteModal);
  const [modal, setModal] = useRecoilState(filterModal);
  const isWritten = useRecoilValue(isWrite);
  const mounted = useRef(false);
  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    setIsPowerWrite(prev => !prev);
  };

  useEffect(() => {
    if (mounted.current) {
      toast.success("글이 성공적으로 작성되었습니다", {
        autoClose: 1500,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
  }, [isWritten]);
  useEffect(() => {
    mounted.current = true;
    return () => {
      mounted.current = false;
    };
  }, []);

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
      {isPowerWrite && <WriteModal />}
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

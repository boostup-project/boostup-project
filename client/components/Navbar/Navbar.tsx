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
import { logUser } from "atoms/auth/authAtom";
import Swal from "sweetalert2";
import { POSITION } from "react-toastify/dist/utils";
import { useRouter } from "next/router";

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
  const router = useRouter();
  const isLog = useRecoilValue(logUser);
  const [isPowerWrite, setIsPowerWrite] = useRecoilState(powerWriteModal);
  const [modal, setModal] = useRecoilState(filterModal);
  const isWritten = useRecoilValue(isWrite);
  const mounted = useRef(false);
  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    if (isLog) {
      setIsPowerWrite(prev => !prev);
    }
    if (!isLog) {
      toast.info("과외 작성은 로그인 후 이용해주세요", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
      router.push("/login");
    }
  };

  useEffect(() => {
    if (mounted.current) {
      toast.success("과외가 성공적으로 등록되었습니다", {
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

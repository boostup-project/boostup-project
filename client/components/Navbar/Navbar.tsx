import {
  IconChat,
  IconCode,
  IconHome,
  IconProfile,
  IconPlus,
} from "assets/icon/";
import { isWriteModal } from "atoms/main/mainAtom";
import WriteModal from "components/createModal/WriteModal";
import Link from "next/link";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";

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
  const [isWrite, setIsWrite] = useRecoilState(isWriteModal);
  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    setIsWrite(prev => !prev);
  };

  return (
    <>
      {isWrite && <WriteModal />}
      <div className="bg-white w-full h-16 flex justify-center items-center sticky bottom-0  desktop:hidden">
        {navContents.map((content, i) => (
          <div key={i} className="w-1/5 flex justify-center items-center">
            {content.type === "Link" ? (
              <div className="w-7">
                <Link href={content.link!}>{content.image}</Link>
              </div>
            ) : content.type === "Write" ? (
              <div className="w-12 h-12 rounded-3xl bg-pointColor flex justify-center items-center">
                <div onClick={e => toWrite(e)} className="w-5">
                  {content.image}
                </div>
              </div>
            ) : (
              <div className="w-7">
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

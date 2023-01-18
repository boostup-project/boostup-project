import { useState } from "react";
import { useRecoilState } from "recoil";
import { logUser } from "atoms";
import { SubmitHandler } from "react-hook-form";
import { textColor } from "assets/color/color";
import { IconExit, IconMagnify } from "assets/icon/";
import { SearchPop } from "./SearchPop";
import Image from "next/image";
import Logo from "../../public/images/logo.png";
import Link from "next/link";
import { isWriteModal } from "atoms/main/mainAtom";
import WriteModal from "components/createModal/WriteModal";

interface SearchData {
  name: string;
  address: number;
  career: string;
  language: number;
  startCost: string;
  endCost: string;
}

const Header = () => {
  const [log, setLog] = useRecoilState(logUser);
  const [seek, setSeek] = useState(false);
  const [isWrite, setIsWrite] = useRecoilState(isWriteModal);

  const onSubmit: SubmitHandler<SearchData> = data => {
    console.log(data);
  };

  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    setIsWrite(prev => !prev);
  };

  return (
    <>
      <header className="bg-bgColor font-SCDream5 w-full pt-4 fixed top-0 z-[1] h-[63px] tablet:h-[69px] desktop:w-3/4 desktop:min-w-[1000px] desktop:h-[87px] desktop:mt-0">
        <nav className="w-full h-full flex tablet:justify-center tablet:items-center desktop:justify-between">
          <div className="w-1/2 flex justify-start items-center ml-4 tablet:h-[40px] desktop:hidden">
            <div className="w-[18px] tablet:w-[34.5px]">
              <IconExit />
            </div>
          </div>
          <div className="w-1/6 h-full flex justify-center items-center desktop:justify-start desktop:items-end">
            <div className="w-[57.91px] h-[30px] tablet:w-[142px] tablet:h-[69px] desktop:w-[142px] desktop:h-[72px]">
              <Image src={Logo} alt="logo" />
            </div>
          </div>
          <div className="w-1/2 flex justify-end items-center mr-4 tablet:h-[40px] desktop:hidden">
            <div
              className="w-[18px] tablet:w-[34.5px]"
              onClick={e => setSeek(!seek)}
            >
              <IconMagnify fill={textColor} />
            </div>
          </div>
          <div className="relative hidden desktop:flex desktop:items-end desktop:w-6/12 h-full">
            <div
              className={`w-full h-[50px] flex justify-center items-center border border-borderColor outline-pointColor font-SCDream2 text-sm text-textColor bg-white 
             ${seek ? `rounded-tr-xl rounded-tl-xl` : `rounded-xl`}`}
              onClick={e => setSeek(!seek)}
            >
              <div>조건에 맞는 과외선생님을 찾아보세요</div>
            </div>
            {seek && <SearchPop onSubmit={onSubmit} absolute={true} />}
          </div>
          <div className="w-1/5 h-full hidden desktop:flex desktop:flex-row-reverse desktop:items-end">
            <div className="w-full pt-2 desktop:flex desktop:h-[50px] desktop:justify-end text-sm">
              {log ? (
                <>
                  <div className="min-w-fit mr-8 py-1">마이페이지</div>
                  <div
                    className="min-w-fit mr-8 py-1"
                    onClick={e => setLog(!log)}
                  >
                    로그아웃
                  </div>
                  <div
                    onClick={toWrite}
                    className="min-w-fit w-1/4 h-fit p-1 rounded-md bg-pointColor text-white text-center"
                  >
                    과외등록
                  </div>
                </>
              ) : (
                <>
                  <div className="mr-8" onClick={e => setLog(!log)}>
                    <Link href="/login">로그인</Link>
                  </div>
                  <div>
                    <Link href="/signup">회원가입</Link>
                  </div>
                </>
              )}
            </div>
          </div>
        </nav>
      </header>
      {seek && (
        <div className="absolute top-40 desktop:hidden">
          <SearchPop onSubmit={onSubmit} absolute={false} />
        </div>
      )}
      {isWrite && <WriteModal />}
    </>
  );
};

export default Header;

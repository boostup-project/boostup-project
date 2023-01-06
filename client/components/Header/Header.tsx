import { useState } from "react";
import { useRecoilState } from "recoil";
import { logUser } from "atoms";
import { SubmitHandler } from "react-hook-form";
import { textColor } from "assets/color/color";
import Image from "next/image";
import Logo from "../../public/images/logo.png";
import Exit from "assets/icon/Exit";
import Magnify from "assets/icon/Magnify";
import { SearchPop } from "./SearchPop";
import Link from "next/link";

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

  const onSubmit: SubmitHandler<SearchData> = data => {
    console.log(data);
  };

  console.log("seek", seek);

  return (
    <>
      <header className="font-SCDream5 w-full mt-4 h-[63px] tablet:h-[69px] desktop:w-[1256px] desktop:h-[87px] desktop:mt-0">
        <nav className="w-full h-full flex tablet:justify-center tablet:items-center desktop:justify-between">
          <div className="w-1/2 flex justify-start items-center ml-4 tablet:h-[40px] desktop:hidden">
            <div className="w-[18px] tablet:w-[34.5px]">
              <Exit fill={textColor} />
            </div>
          </div>
          <div className="w-1/6 h-full flex justify-center items-center desktop:justify-none desktop:items-end">
            <Image
              className="w-[57.91px] h-[30px] tablet:w-[142px] tablet:h-[69px] desktop:w-[142px] desktop:h-[69px]"
              src={Logo}
              alt="logo"
            />
          </div>
          <div className="w-1/2 flex justify-end items-center mr-4 tablet:h-[40px] desktop:hidden">
            <div
              className="w-[18px] tablet:w-[34.5px]"
              onClick={e => setSeek(!seek)}
            >
              <Magnify fill={textColor} />
            </div>
          </div>
          <div className="relative hidden desktop:flex desktop:items-end desktop:w-6/12 h-full">
            <div
              className={`w-full h-14 flex justify-center items-center border border-borderColor outline-pointColor font-SCDream2 text-sm text-textColor bg-white 
             ${seek ? `rounded-tr-md rounded-tl-md` : `rounded-md`}`}
              onClick={e => setSeek(!seek)}
            >
              <div>조건에 맞는 과외선생님을 찾아보세요</div>
            </div>
            {seek && <SearchPop onSubmit={onSubmit} absolute={true} />}
          </div>
          <div className="w-1/6 hidden desktop:flex desktop:items-center desktop:justify-around">
            {log ? (
              <>
                <div className="mr-8">마이페이지</div>
                <div onClick={e => setLog(!log)}>로그아웃</div>
              </>
            ) : (
              <>
                <div className="mr-8" onClick={e => setLog(!log)}>
                  <Link href="/login">로그인</Link>
                </div>
                <div>
                <Link href="/signup">회원가입</Link></div>
              </>
            )}
          </div>
        </nav>
      </header>
      {seek && (
        <div className="absolute top-40 desktop:hidden">
          <SearchPop onSubmit={onSubmit} absolute={false} />
        </div>
      )}
    </>
  );
};

export default Header;

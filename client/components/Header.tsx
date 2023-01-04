import { useState } from "react";
import Image from "next/image";
import Logo from "../public/images/logo.png";
import InputBox from "./reuse/InputBox";

const Header = () => {
  const [searchData, setSearchData] = useState("");
  return (
    <header className="font-SCDream5 flex desktop:max-w-[1256px] ">
      <div className="w-[57.91px] h-[30px] desktop:w-[142px] desktop:h-[69px]">
        <Image src={Logo} alt="logo" />
      </div>
      <div className="hidden desktop:block desktop:w-[708px] ">
        <InputBox
          placeholder="조건에 맞는 과외쌤을 찾아보세요"
          value={searchData}
          onChange={e => setSearchData(e.target.value)}
        ></InputBox>
      </div>
      <div className="flex">
        <div>로그인</div>
        <div>회원가입</div>
      </div>
    </header>
  );
};

export default Header;

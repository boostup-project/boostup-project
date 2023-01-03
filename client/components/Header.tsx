import { useState } from "react";
import Image from "next/image";
import Logo from "../public/images/logo.png";

const Header = () => {
  const [searchData, setSearchData] = useState();
  return (
    <header className="font-SCDream5 flex desktop:bg-slate-600 bg-pointColor ">
      <div className="w-[57.91px] h-[30px] desktop:w-[142px] desktop:h-[69px]">
        <Image src={Logo} alt="logo" className="" />
      </div>
      <div className="w-screen ">searchbar</div>
      <div className="w-screen  ">right-side</div>
    </header>
  );
};

export default Header;

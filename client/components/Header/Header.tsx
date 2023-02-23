import { useState } from "react";
import { useRecoilState, useSetRecoilState } from "recoil";
import { SubmitHandler } from "react-hook-form";
import { textColor } from "assets/color/color";
import { IconExit, IconLogIn, IconMagnify } from "assets/icon/";
import { SearchPop } from "./SearchPop";
import { mainCardInfo, powerWriteModal } from "atoms/main/mainAtom";
import Image from "next/image";
import Logo from "../../public/images/logo.png";
import Link from "next/link";
import { logUser } from "atoms/auth/authAtom";
import "react-toastify/dist/ReactToastify.css";
import { useEffect } from "react";
import { useRouter } from "next/router";
import usePostSearch from "hooks/main/usePostSearch";

import { useQueryClient } from "@tanstack/react-query";
import useDeleteLogout from "hooks/auth/useDeleteLogout";
import Swal from "sweetalert2";

interface SelectData {
  key: number;
  label: string;
  value: number;
}

interface SearchData {
  name: string;
  address: SelectData;
  career: string;
  language: SelectData;
  startCost: string;
  endCost: string;
}

const Header = () => {
  const queryClient = useQueryClient();
  const router = useRouter();
  const [log, setLog] = useRecoilState<boolean>(logUser);
  const [wordOne, setWordOne] = useState<string>("");
  const [wordTwo, setWordTwo] = useState<string>("");
  const [writeBtn, setWriteBtn] = useState<string>("");
  const [mobLogExitIcon, setMobLogExitIcon] = useState<any>("");
  const [seek, setSeek] = useState(false);
  const [isPowerWrite, setIsPowerWrite] = useRecoilState(powerWriteModal);
  const setMainCardInfo = useSetRecoilState(mainCardInfo);

  const { mutate, isSuccess, data } = usePostSearch();
  const { mutate: mutateLogout } = useDeleteLogout();

  useEffect(() => {
    if (log) {
      setWordOne("내정보");
      setWordTwo("로그아웃");
      setWriteBtn("과외 등록");
      setMobLogExitIcon(<IconExit />);
    }
    if (!log) {
      setWordOne("로그인");
      setWordTwo("회원가입");
      setWriteBtn("");
      setMobLogExitIcon(<IconLogIn />);
    }
  }, [log]);

  const onSubmit: SubmitHandler<SearchData> = data => {
    const { address, career, endCost, language, name, startCost } = data;
    const parseAdd = address?.value;
    const parseLang = language?.value;
    const toSendInput = {
      name: name ? name : "",
      address: parseAdd ? parseAdd : "",
      career: career ? Number(career) : "",
      language: language ? parseLang : "",
      startCost: startCost ? Number(startCost) : "",
      endCost: endCost ? Number(endCost) : "",
    };
    console.log(toSendInput);
    mutate(toSendInput);
  };

  useEffect(() => {
    if (isSuccess && data) {
      console.log(data);
      setSeek(prev => !prev);
      setMainCardInfo(data.data.data);
    }
  }, [isSuccess]);

  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    if (localStorage.getItem("lessonExistence") === "true") {
      Swal.fire({
        text: "이미 과외를 등록하셨습니다.",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    } else {
      setIsPowerWrite(prev => !prev);
    }
  };

  const wordOneEvent = () => {
    if (!log) {
      router.push("/login");
    }
    if (log) {
      router.push(`/mypage/${localStorage.name}`);
    }
  };
  const wordTwoEvent = () => {
    if (log) {
      logInNOutEvent();
    }
    if (!log) {
      router.push("/signup");
    }
  };

  const logInNOutEvent = () => {
    if (log) {
      mutateLogout();
    }
    if (!log) {
      router.push("/login");
    }
  };

  useEffect(() => {
    if (seek) {
      setSeek(prev => !prev);
    }
  }, [router.asPath]);
  return (
    <header className="bg-bgColor font-SCDream5 w-full sticky top-0 z-[1] h-fit flex flex-col justify-center items-center shadow">
      <div className="relative pt-2 max-w-[1000px] w-full pb-2 flex flex-col justify-center items-center desktop:w-3/4 desktop:min-w-[1000px] desktop:h-[87px] desktop:mt-0">
        <nav className="w-full h-full flex tablet:justify-center tablet:items-center desktop:justify-between">
          <div className="w-1/2 flex justify-start items-center ml-4 tablet:h-[40px] desktop:hidden">
            <div
              className="w-5 tablet:w-[25px] cursor-pointer"
              onClick={logInNOutEvent}
            >
              {mobLogExitIcon}
            </div>
          </div>
          <div className="tablet:w-1/5 h-fit flex justify-center items-center desktop:justify-start desktop:items-end">
            <div className="flex justify-center items-center tablet:w-[100px] w-[80px] tablet:h-[69px] h-[50px]">
              <Link href="/">
                <Image src={Logo} alt="logo" />
              </Link>
            </div>
          </div>
          <div className="w-1/2 flex justify-end items-center mr-4 tablet:h-[40px] desktop:hidden">
            <div
              className="w-5 tablet:w-[25px] cursor-pointer"
              onClick={e => setSeek(!seek)}
            >
              <IconMagnify fill={textColor} />
            </div>
          </div>
          <div className="hidden desktop:flex desktop:items-end desktop:min-w-[630px] desktop:w-[630px] desktop:h-full desktop:relative desktop:visible">
            <div
              className={`w-full h-[50px] flex justify-center items-center border border-borderColor outline-pointColor font-SCDream2 text-sm text-textColor bg-white 
            ${seek ? `rounded-tr-xl rounded-tl-xl` : `rounded-xl`}`}
              onClick={e => setSeek(!seek)}
            >
              <div>조건에 맞는 과외선생님을 찾아보세요</div>
            </div>
          </div>
          <div className="w-1/5 h-full hidden desktop:flex desktop:flex-row-reverse desktop:items-end">
            <div className="w-full pt-2 desktop:flex desktop:h-[50px] desktop:justify-end text-sm">
              <div
                className="min-w-fit w-1/3 py-1 cursor-pointer text-center hover:underline"
                onClick={wordOneEvent}
              >
                {wordOne}
              </div>
              <div
                className="min-w-fit w-1/3 py-1 cursor-pointer text-center hover:underline mr-2"
                onClick={wordTwoEvent}
              >
                {wordTwo}
              </div>
              {writeBtn.length > 1 && (
                <div
                  onClick={toWrite}
                  className="min-w-fit h-fit p-1 rounded-md bg-pointColor text-white text-center cursor-pointer hover:underline"
                >
                  {writeBtn}
                </div>
              )}
            </div>
          </div>
        </nav>
      </div>
      {seek && (
        <div className="w-full flex justify-center items-center">
          <div className="absolute top-20 tablet:w-[500px] desktop:w-[630px] desktop:top-[78px]">
            <SearchPop onSubmit={onSubmit} />
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;

import { useState } from "react";
import { useRecoilState } from "recoil";
import { logUser } from "atoms";
import { SubmitHandler, useForm } from "react-hook-form";
import { langDict, addDict } from "./reuse/dict";
import Image from "next/image";
import Logo from "../public/images/logo.png";
import AuthBtn from "./reuse/AuthBtn";

const formArr = ["닉네임", "경력", "과외가능지역", "개발언어", "가격"];

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
  const [searchOpen, setsearchOpen] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const onSubmit = (data: any) => {
    console.log(data);
  };

  const langArr = Object.keys(langDict);
  const addArr = Object.keys(addDict);

  console.log(searchOpen);

  return (
    <header className="font-SCDream5 desktop:w-[1256px]">
      <nav className="w-full flex desktop:justify-around">
        <div className="w-[57.91px] h-[30px] desktop:w-[142px] desktop:h-[69px]">
          <Image src={Logo} alt="logo" />
        </div>
        <div className="relative hidden desktop:flex desktop:items-end desktop:w-6/12">
          <div
            className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor bg-white text-center"
            onClick={e => setsearchOpen(!searchOpen)}
          >
            <span>조건에 맞는 과외선생님을 찾아보세요</span>
          </div>
          {searchOpen && (
            <div className="absolute bg-white desktop:top-[70px] w-full h-fit">
              <form
                className="flex flex-col justify-center items-center"
                onSubmit={handleSubmit(onSubmit)}
              >
                <label className="w-full flex flex-col items-center mt-4">
                  <div className="text-textColor text-center">닉네임</div>
                  <input
                    className="desktop:w-8/12 w-5/6 h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                    type="text"
                    placeholder="원하는 과외쌤의 닉네임을 입력학세요"
                    {...register("name")}
                  />
                </label>
                <label className="w-full flex flex-col items-center mt-4">
                  <div className="text-textColor text-center">경력</div>
                  <input
                    className="desktop:w-8/12 w-5/6 h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                    type="text"
                    placeholder="원하는 과외쌤의 경력을 숫자로 입력하세요"
                    {...register("carrer")}
                  />
                </label>
                <label className="w-full mt-4">
                  <div className="text-textColor text-center">과외가능지역</div>
                  <div className="w-full flex">
                    <div className="w-3/6 text-center flex flex-col items-center">
                      <span>시,도</span>
                      <div className="w-2/6 desktop:w-[60%] h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor bg-white text-center mt-4">
                        <span>서울특별시</span>
                      </div>
                    </div>
                    <div className="w-3/6 text-center flex flex-col items-center">
                      <span>구</span>
                      <input
                        className="w-2/6 desktop:w-[60%] h-fit p-2 border border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                        type="text"
                        placeholder="자치구"
                        {...register("address")}
                      />
                    </div>
                  </div>
                </label>
                <label className="w-full flex flex-col items-center mt-4">
                  <div className="text-textColor text-center">개발언어</div>
                  <input
                    className="desktop:w-8/12 w-5/6 h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                    type="text"
                    placeholder="원하는 과외쌤의 경력을 숫자로 입력하세요"
                    {...register("language")}
                  />
                </label>
                <label className="w-full mt-4">
                  <div className="text-textColor text-center">가격</div>
                  <div className="w-full flex">
                    <div className="w-3/6 text-center flex flex-col items-center">
                      <span>최소금액</span>
                      <input
                        className="w-2/6 desktop:w-[60%] h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                        type="text"
                        placeholder="최소금액"
                        {...register("startCost")}
                      />
                    </div>
                    <div className="w-3/6 text-center flex flex-col items-center">
                      <span>최대금액</span>
                      <input
                        className="w-2/6 desktop:w-[60%] h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                        type="text"
                        placeholder="최대금액"
                        {...register("endCost")}
                      />
                    </div>
                  </div>
                </label>
                <AuthBtn className="mt-4">검색</AuthBtn>
              </form>
            </div>
          )}
        </div>

        <div className="hidden desktop:flex desktop:items-center desktop:justify-around">
          {log ? (
            <>
              <div className="mr-8">마이페이지</div>
              <div onClick={e => setLog(!log)}>로그아웃</div>
            </>
          ) : (
            <>
              <div className="mr-8" onClick={e => setLog(!log)}>
                로그인
              </div>
              <div>회원가입</div>
            </>
          )}
        </div>
      </nav>
    </header>
  );
};

export default Header;

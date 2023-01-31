import { addDict, langDict } from "components/reuse/dict";
import { useEffect } from "react";
import { useState } from "react";
import { useForm } from "react-hook-form";

interface stringToString {
  [index: string]: string;
}

const teacherInfo: stringToString = {
  name: "이름",
  language: "가능언어",
  address: "가능지역",
  company: "경력 / 학력",
};
const costInfo: stringToString = {
  cost: "회차별 금액",
  quantity: "횟수",
  totalCost: "총 금액",
};

const teacherInfoKeys = Object.keys(teacherInfo);
const costInfoKeys = Object.keys(costInfo);

const payMethod = ["카카오페이", "토스 - 계좌", "토스 - 카드", "토스 - 휴대폰"];

const shop = () => {
  /** 유저 정보 hydration error 방지를 위하여 state로 처리 **/
  const [userName, setUserName] = useState<string | null>("");
  const [userEmail, setUserEmail] = useState<string | null>("");
  useEffect(() => {
    const nameFrom = localStorage.getItem("name");
    const emailFrom = localStorage.getItem("email");
    setUserName(nameFrom);
    setUserEmail(emailFrom);
  }, []);

  /** hook-form 삽입 **/
  const { handleSubmit, register } = useForm();

  const onSubmit = (e: any) => {
    console.log(e);
  };

  /** 숫자값에 맞는 사전의 key값을 조회  **/
  const toRenderAdd = dummy.address
    .map(el => Object.keys(addDict).find(key => addDict[key] === el))
    .join(", ");
  const toRenderLang = dummy.language
    .map(el => Object.keys(langDict).find(key => langDict[key] === el))
    .join(", ");

  /** return 값 안에서 실질적으로 표현할 데이터 **/
  const teacherInfoData: stringToString = {
    name: dummy.name,
    language: toRenderLang,
    address: toRenderAdd,
    company: dummy.company,
  };
  const costInfoData: stringToString = {
    cost: Number(dummy.cost).toLocaleString("ko-KR") + " 원",
    quantity: dummy.quantity,
    totalCost: Number(dummy.totalCost).toLocaleString("ko-KR") + " 원",
  };

  return (
    <div className="flex flex-col bg-bgColor items-center mt-28 w-full h-screen text-base tablet:text-2xl desktop:mt-12 desktop:w-3/4 desktop:min-w-[1000px]">
      <form
        className="w-full h-full flex flex-col justify-center items-center"
        onSubmit={handleSubmit(onSubmit)}
      >
        <div className="font-SCDream5 text-2xl text-center tablet:text-3xl desktop:mb-12">
          결제
        </div>
        <div className="w-11/12 font-SCDream5 desktop:w-full desktop:flex desktop:justify-between">
          <div className="desktop:w-7/12">
            <div className="mb-1 pl-5">과외정보</div>
            <div className="mb-6 p-5 border rounded-xl border-borderColor bg-white desktop:h-[500px]">
              <div className="desktop:h-1/2">
                <div>선생님 정보</div>
                <div className="flex h-full my-4 tablet:justify-start desktop:h-fit">
                  <div className="w-fit">
                    <img
                      className="rounded-xl w-[140px]"
                      src={dummy.profileImage as string}
                    />
                  </div>
                  <div className="w-1/2 flex flex-col justify-evenly text-sm ml-7 tablet:text-lg tablet:ml-16">
                    <div className="w-full bold">{dummy.title}</div>
                    {teacherInfoKeys.map((key, i) => (
                      <div
                        key={i}
                        className="w-full flex text-xs tablet:text-sm"
                      >
                        <div className="bold w-[85px]">{teacherInfo[key]}</div>
                        <div className="w-[80px] ml-2 tablet:w-fit">
                          {teacherInfoData[key]}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
              <div className="desktop:h-1/2">
                <div>결제 상세내역</div>
                <div className="text-sm flex flex-col h-full justify-between my-4 tablet:text-sm desktop:h-2/5">
                  {costInfoKeys.map((key, i) => (
                    <div key={i} className="w-full flex">
                      <div className="w-24">{costInfo[key]}</div>
                      <div className="w-24 text-center">
                        {costInfoData[key]}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
          <div className="desktop:w-4/12 desktop:h-[532px] desktop:flex desktop:flex-col desktop:justify-between">
            <div className="desktop:h-2/6">
              <div className="mb-1">내 정보</div>
              <div className="text-sm mb-6 p-5 border rounded-xl border-borderColor bg-white desktop:h-full desktop:flex desktop:flex-col desktop:justify-evenly desktop:items-center">
                <div className="flex">
                  <div className="w-14 text-end mr-9">이름</div>
                  <div className="w-64 desktop:w-48">{userName}</div>
                </div>
                <div className="flex">
                  <div className="w-14 text-end mr-9">이메일</div>
                  <div className="w-64 desktop:w-48">{userEmail}</div>
                </div>
              </div>
            </div>
            <div className="desktop:h-1/2">
              <div className="mb-1">결제 수단</div>
              <div className="desktop:h-[87%]">
                <div className="flex text-xs mb-6 p-5 border rounded-xl border-borderColor bg-white tablet:text-sm desktop:h-full desktop:flex-col desktop:justify-evenly desktop:items-center">
                  {payMethod.map((el, i) => (
                    <label
                      key={i}
                      className="w-1/4 text-center desktop:text-start desktop:w-32 desktop:"
                    >
                      <input
                        type="radio"
                        value={i}
                        {...register("pay", {
                          required: true,
                        })}
                      />
                      {el}
                    </label>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="w-11/12 font-SCDream5 desktop:full">
          <div className="w-full text-center mt-10 tablet:text-base">
            <label className="w-full justify-center items-center">
              <input type="checkbox" required={true} /> 모든 결제 정보에 대해서
              확인하였습니다
            </label>
          </div>
          <div className="w-full text-center mt-7 tablet:text-base">
            <button
              className="rounded-xl bg-pointColor text-white px-4 py-2"
              onSubmit={onSubmit}
            >
              결제하기
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default shop;

var dummy = {
  name: "JS 잘해요",
  title: "인터텍티브 개발 끝장내기",
  language: [1, 2, 3],
  address: [1, 2, 3],
  company: "네이버",
  profileImage:
    "data:image/jpeg;base64,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",
  cost: "10000",
  totalCost: "20000",
  quantity: "2",
};

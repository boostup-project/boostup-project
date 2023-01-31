import useGetPaymentInfo from "hooks/shop/useGetPaymentInfo";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { useState } from "react";
import { useForm } from "react-hook-form";

interface FetchedData {
  address: string[];
  company: string;
  cost: number;
  languages: string[];
  name: string;
  profileImage: string;
  quantity: number;
  title: string;
  totalCost: number;
}

interface StringToString {
  [index: string]: string;
}

interface OnSubmit {
  pay: string;
}

const teacherInfo: StringToString = {
  name: "이름",
  language: "가능언어",
  address: "가능지역",
  company: "경력 / 학력",
};
const costInfo: StringToString = {
  cost: "회차별 금액",
  quantity: "횟수",
  totalCost: "총 금액",
};

const teacherInfoKeys = Object.keys(teacherInfo);
const costInfoKeys = Object.keys(costInfo);

const payMethod = ["카카오페이", "토스 - 계좌", "토스 - 카드", "토스 - 휴대폰"];

const shop = () => {
  const router = useRouter();
  const suggestId = Number(router.query.id);

  /** React-query로 데이터 가져오기 */
  const { refetch, data } = useGetPaymentInfo(suggestId);

  /** 유저 정보 hydration error 방지를 위하여 state로 처리 **/
  const [userName, setUserName] = useState<string | null>("");
  const [userEmail, setUserEmail] = useState<string | null>("");
  const [teacherInfoData, setTeacherInfoData] = useState<StringToString>();
  const [costInfoData, setCostInfoData] = useState<StringToString>();
  const [title, setTitle] = useState<string>("");
  const [teacherImg, setTeacherImg] = useState<string>("");

  /** hook-form 삽입 **/
  const { handleSubmit, register } = useForm<OnSubmit>();

  /** suggestId값이 있다면 로컬스토리지 값을 저장하고 refetch하기 */
  useEffect(() => {
    if (suggestId) {
      const nameFrom = localStorage.getItem("name");
      const emailFrom = localStorage.getItem("email");
      setUserName(nameFrom);
      setUserEmail(emailFrom);
      refetch();
    }
  }, [suggestId]);

  useEffect(() => {
    if (data) {
      const {
        address,
        company,
        cost,
        languages,
        name,
        profileImage,
        quantity,
        title,
        totalCost,
      } = data?.data as FetchedData;

      const toRenderAdd = address.join(", ");
      const toRenderLang = languages.join(", ");

      setTeacherInfoData({
        name,
        language: toRenderLang,
        address: toRenderAdd,
        company,
      });
      setCostInfoData({
        cost: cost.toLocaleString("ko-KR") + " 원",
        quantity: String(quantity),
        totalCost: totalCost.toLocaleString("ko-KR") + " 원",
      });
      setTeacherImg(profileImage);
      setTitle(title);
    }
  }, [data]);

  const onSubmit = (e: OnSubmit) => {
    console.log(e);
  };

  if (!data) {
    <div className="mt-28 desktop:mt-12">Loading</div>;
  } else {
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
                        src={teacherImg as string}
                      />
                    </div>
                    <div className="w-1/2 flex flex-col justify-evenly text-sm ml-7 tablet:text-lg tablet:ml-16">
                      <div className="w-full bold">{title}</div>
                      {teacherInfoKeys.map((key, i) => (
                        <div
                          key={i}
                          className="w-full flex text-xs tablet:text-sm"
                        >
                          <div className="bold w-[85px]">
                            {teacherInfo[key]}
                          </div>
                          <div className="w-[80px] ml-2 tablet:w-fit">
                            {typeof teacherInfoData === "undefined" ? (
                              <div>loading</div>
                            ) : (
                              teacherInfoData[key]
                            )}
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
                          {typeof costInfoData === "undefined" ? (
                            <div>loading</div>
                          ) : (
                            costInfoData[key]
                          )}
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
                <input type="checkbox" required={true} /> 모든 결제 정보에
                대해서 확인하였습니다
              </label>
            </div>
            <div className="w-full text-center mt-7 tablet:text-base">
              <button className="rounded-xl bg-pointColor text-white px-4 py-2">
                결제하기
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
};

export default shop;

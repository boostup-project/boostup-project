import { Assemble } from "apis/shop/getKakaoNTossPay";
import { useRouter } from "next/router";
import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import useGetKakaoNTossPay from "hooks/shop/useGetKakaoNTossPay";
import useGetPaymentCheck from "hooks/shop/useGetPaymentCheck";
import useGetPaymentInfo from "hooks/shop/useGetPaymentInfo";
import { toast } from "react-toastify";
import Kakao from "../../../public/images/Kakao.png";
import Toss from "../../../public/images/Toss.png";
import useWindowSize from "hooks/useWindowSize";

import Image from "next/image";

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

export interface StringToString {
  [index: string]: string;
}

interface OnSubmit {
  pay: string;
}

export const teacherInfo: StringToString = {
  name: "이름",
  languages: "가능언어",
  address: "가능지역",
  company: "경력 / 학력",
};
export const costInfo: StringToString = {
  cost: "회차별 금액",
  quantity: "횟수",
  totalCost: "총 금액",
};

export const teacherInfoKeys = Object.keys(teacherInfo);
export const costInfoKeys = Object.keys(costInfo);

const payMethod = [
  { name: "카카오페이", icon: Kakao, mobile: "카카오" },
  { name: "토스-카드", icon: Toss, mobile: "카드" },
  { name: "토스-휴대폰", icon: Toss, mobile: "휴대폰" },
  { name: "토스-계좌", icon: Toss, mobile: "계좌" },
];

const shop = () => {
  const router = useRouter();
  const suggestId = Number(router.query.id);
  const screenWidth = useWindowSize();

  /** 유저 정보 hydration error 방지를 위하여 state로 처리 **/
  const [userName, setUserName] = useState<string | null>("");
  const [userEmail, setUserEmail] = useState<string | null>("");
  const [teacherInfoData, setTeacherInfoData] = useState<StringToString>();
  const [costInfoData, setCostInfoData] = useState<StringToString>();
  const [title, setTitle] = useState<string>("");
  const [teacherImg, setTeacherImg] = useState<string>("");
  const [assemble, setAssemble] = useState<Assemble>();
  const [isPayClicked, setIsPayClicked] = useState(false);
  const mounted = useRef(false);

  /** React-query로 데이터 가져오기 */
  const { refetch, data } = useGetPaymentInfo(suggestId);
  const {
    refetch: getPayURL,
    isSuccess,
    data: dataPayURL,
  } = useGetKakaoNTossPay(assemble!);
  const {
    refetch: getPayCheck,
    isSuccess: payStatus,
    data: dataPayCheck,
  } = useGetPaymentCheck(suggestId);

  /** hook-form 삽입 **/
  const { handleSubmit, register } = useForm<OnSubmit>();

  /** 결제클릭시 url받아오기 */
  useEffect(() => {
    if (mounted.current) {
      getPayURL();
    }
  }, [assemble]);

  useEffect(() => {
    const width = 800;
    const height = 800;
    const left = window.screenX + (window.outerWidth - width) / 2;
    const top = window.screenY + (window.outerHeight - height) / 2;
    if (isSuccess) {
      console.log(dataPayURL.data.data);
      window.open(
        dataPayURL.data.data,
        "payPop",
        `width=${width},height=${height},left=${left},top=${top}`,
      );
    }
  }, [isSuccess]);

  /** 결제여부 확인 */
  useEffect(() => {
    if (payStatus) {
      const isPay = dataPayCheck.data.paymentCheck;
      if (isPay) {
        toast.success("결제가 확인되었습니다! 결제 내역을 불러옵니다", {
          autoClose: 3000,
          position: toast.POSITION.TOP_RIGHT,
        });
        setTimeout(() => {
          router.push(`/shop/${suggestId}/receipt`);
        }, 1500);
      } else if (!isPay) {
        toast.error(
          "결제가 정상적으로 처리되지 않았습니다. 다시 결제해주세요",
          {
            autoClose: 3000,
            position: toast.POSITION.TOP_RIGHT,
          },
        );
      }
    }
  }, [payStatus]);

  /** 초기 렌더링 방지 */
  useEffect(() => {
    mounted.current = true;
    return () => {
      mounted.current = false;
    };
  }, []);

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
        languages: toRenderLang,
        address: toRenderAdd,
        company,
      });
      setCostInfoData({
        cost: cost.toLocaleString("ko-KR") + " 원/회",
        quantity: String(quantity),
        totalCost: totalCost.toLocaleString("ko-KR") + " 원",
      });
      setTeacherImg(profileImage);
      setTitle(title);
    }
  }, [data]);

  const onSubmit = (e: OnSubmit) => {
    setAssemble({
      suggestId: suggestId,
      paymentId: e.pay,
    });
    setIsPayClicked(prev => !prev);
  };

  const paymentCheck = () => {
    getPayCheck();
  };

  if (!data) {
    <div className="mt-28 desktop:mt-12">Loading</div>;
  } else {
    return (
      <div className="flex flex-col bg-bgColor items-center w-full h-full mt-10 text-base tablet:text-2xl desktop:w-3/4 desktop:min-w-[1000px]">
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
                    <div className="w-2/3 flex flex-col justify-evenly text-sm ml-7 tablet:text-xl tablet:ml-16">
                      <div className="w-fit bold">{title}</div>
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
                        <div
                          className={`w-24 ${
                            key === "totalCost" && `font-SCDream7 font-bold`
                          }`}
                        >
                          {costInfo[key]}
                        </div>
                        <div
                          className={`w-24 text-center ${
                            key === "totalCost" && `font-SCDream7 font-bold`
                          }`}
                        >
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
                  <div className="flex justify-center text-xs mb-6 px-3 py-5 border rounded-xl border-borderColor bg-white tablet:text-sm desktop:h-full desktop:flex-col desktop:justify-evenly desktop:items-center">
                    {payMethod.map((el, i) => (
                      <label
                        key={i}
                        className="w-1/4 text-center flex justify-center text-xs items-center desktop:justify-start desktop:w-32"
                      >
                        <input
                          type="radio"
                          value={i}
                          disabled={isPayClicked ? true : false}
                          {...register("pay", {
                            required: true,
                          })}
                        />
                        <span>&nbsp;</span>
                        {screenWidth <= 412 ? el.mobile : el.name}
                        <span>&nbsp;</span>
                        <Image
                          src={el.icon}
                          alt="kakao"
                          width={screenWidth < 412 ? 15 : 30}
                          height={screenWidth < 412 ? 15 : 30}
                        />
                      </label>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </div>
          {isPayClicked && (
            <div className="w-full font-SCDream5 text-center mt-7 tablet:text-base">
              <button
                className="rounded-xl bg-pointColor text-white px-4 py-2"
                onClick={paymentCheck}
              >
                결제 확인
              </button>
            </div>
          )}
          {!isPayClicked && (
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
          )}
        </form>
      </div>
    );
  }
};

export default shop;

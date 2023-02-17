import useGetReceipt from "hooks/shop/useGetReceipt";
import { useRouter } from "next/router";
import { useState, useEffect } from "react";
import {
  StringToString,
  teacherInfo,
  costInfo,
  teacherInfoKeys,
  costInfoKeys,
} from "./index";
import Kakao from "../../../public/images/Kakao.png";
import Toss from "../../../public/images/Toss.png";
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
  paymentMethod: string;
}

interface StringToImage {
  [index: string]: any;
}

const payMethod: StringToImage = {
  카카오페이: Kakao,
  토스페이: Toss,
};

const receipt = () => {
  const router = useRouter();
  const queryId = Number(router.query.id);

  const [userName, setUserName] = useState<string | null>("");
  const [userEmail, setUserEmail] = useState<string | null>("");
  const [title, setTitle] = useState<string>("");
  const [teacherImg, setTeacherImg] = useState<string>("");
  const [teacherInfoData, setTeacherInfoData] = useState<StringToString>();
  const [costInfoData, setCostInfoData] = useState<StringToString>();
  const [paymentMethod, setPaymentMethod] = useState("");

  const { refetch, data } = useGetReceipt(queryId);
  useEffect(() => {
    if (queryId) {
      refetch();
    }
  }, [queryId]);
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
        paymentMethod,
      } = data?.data as FetchedData;

      const toRenderAdd = address.join(", ");
      const toRenderLang = languages.join(", ");
      console.log(toRenderLang);
      console.log(languages);

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
      setPaymentMethod(paymentMethod);
      const nameFrom = localStorage.getItem("name");
      const emailFrom = localStorage.getItem("email");
      setUserName(nameFrom);
      setUserEmail(emailFrom);
    }
  }, [data]);

  return (
    <div className="flex flex-col bg-bgColor items-center w-full h-screen text-base tablet:text-2xl desktop:w-3/4 desktop:min-w-[1000px]">
      <div className="w-full h-full flex flex-col justify-center items-center">
        <div className="font-SCDream5 text-2xl text-center tablet:text-3xl desktop:mb-12">
          결제 영수증
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
                      className="object-cover rounded-xl w-[140px] h-[140px]"
                      src={teacherImg as string}
                    />
                  </div>
                  <div className="w-1/2 flex flex-col justify-evenly text-sm ml-7 tablet:text-lg tablet:ml-16">
                    <div className="w-fit bold">{title}</div>
                    {teacherInfoKeys.map((key, i) => (
                      <div
                        key={i}
                        className="w-full flex text-xs tablet:text-sm"
                      >
                        <div className="bold w-[85px]">{teacherInfo[key]}</div>
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
                <div className="flex text-xs mb-6 p-5 border rounded-xl border-borderColor bg-white tablet:text-sm desktop:h-full desktop:justify-center desktop:items-center">
                  <div>{paymentMethod}</div>
                  <span>&nbsp;</span>
                  <Image
                    src={payMethod[paymentMethod.split(" ")[0]]}
                    alt="icon"
                    width={30}
                    height={30}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default receipt;

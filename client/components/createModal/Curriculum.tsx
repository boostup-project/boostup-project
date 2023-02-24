import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import SmallBtn from "components/reuse/btn/SmallBtn";
import dynamic from "next/dynamic";
import useWindowSize from "hooks/useWindowSize";
import usePostWrite from "hooks/main/usePostWrite";
import imageCompression from "browser-image-compression";
import { bold, italic } from "@uiw/react-md-editor/lib/commands/";
import { Dispatch, SetStateAction } from "react";
import { SetterOrUpdater, useSetRecoilState } from "recoil";
import { Info } from "./WriteModal";
import { langDict } from "components/reuse/dict";
import { useEffect } from "react";
import { isWrite } from "atoms/main/mainAtom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
  ssr: false,
});

interface Props {
  basicInfo: Info;
  extraInfo: Info;
  curInfo: string;
  setCurInfo: Dispatch<SetStateAction<string>>;
  setStep: SetterOrUpdater<number>;
  setIsPowerWrite: SetterOrUpdater<boolean>;
}

interface BasicSubmit {
  [index: string]: any;
}

const compressImage = async (image: any) => {
  try {
    const options = {
      maxSizeMb: 2,
      maxWidthOrHeight: 300,
    };
    return await imageCompression(image, options);
  } catch (e) {
    console.log(e);
  }
};

const Curriculum = ({
  basicInfo,
  extraInfo,
  curInfo,
  setCurInfo,
  setStep,
  setIsPowerWrite,
}: Props) => {
  const setIsWritten = useSetRecoilState(isWrite);
  const { mutate, isSuccess, isError } = usePostWrite();
  const screenWidth = useWindowSize();
  const toBack = (e: React.MouseEvent<HTMLButtonElement>) => {
    setStep(prev => prev - 1);
  };
  const handleChangeValue = (e: any) => {
    setCurInfo(e);
  };

  useEffect(() => {
    if (isSuccess) {
      setIsPowerWrite(prev => !prev);
      setIsWritten(prev => !prev);
      setStep(prev => prev - 2);
    }
  }),
    [isSuccess, isError];

  const onClick = async () => {
    /** 1. FormData선언 및 할당 */
    const formData = new FormData();

    /** 2. 데이터 분해 */
    const {
      address,
      career,
      company,
      cost,
      languages,
      profileImg,
      title,
    }: BasicSubmit = basicInfo;
    const {
      detailCompany,
      detailCost,
      detailLocation,
      detailImage,
      introduction,
      personality,
    }: BasicSubmit = extraInfo;

    /** 3. 주소와 언어를 숫자로 변경 */
    const parseAddress = address.map((el: any) => el.value);
    const parseLang = languages.map((el: any) => langDict[el]);

    /** 4. 먼저 문자 데이터 추합 및 할당 */
    const json = JSON.stringify({
      title,
      languages: parseLang,
      company,
      career,
      address: parseAddress,
      cost,
      introduction,
      detailCompany,
      detailLocation,
      personality,
      detailCost,
      curriculum: curInfo,
    });
    const blob = new Blob([json], { type: "application/json" });
    formData.append("data", blob);

    /** 5. 프로필 이미지 압축 및 할당 */
    const proImage = profileImg[0];
    const compressImg = await compressImage(proImage);
    let compressFile;

    if (compressImg) {
      compressFile = new File([compressImg], compressImg?.name);
      formData.append("profileImage", compressFile);
    }

    /** 6. 참고 사진 압축 및 할당 */
    if (detailImage.length > 0) {
      const careerImgsBefore = detailImage.map((image: FileList) => image[0]);
      const some = await Promise.all(
        careerImgsBefore.map(async (img: File, idx: number) => {
          const compressImg = await compressImage(img);
          return new File([compressImg!], compressImg?.name!);
        }),
      );
      some.map(el => formData.append("careerImage", el));
    }
    mutate(formData);
  };

  return (
    <>
      <ToastContainer />
      <div className="flex flex-col justify-center items-center w-full h-fit">
        <div className="w-fit h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[13.5px] text-pointColor">
          과외 진행방식에 대한 자세한 정보를 입력하세요
        </div>
        <div className="flex tablet:flex-row flex-col w-fit h-fit justify-center items-center">
          <div className="w-fit h-fit font-SCDream4 text-[12px] text-textColor mt-2">
            회당 시간, 수업구성, 개발환경 및 요구하는 수준 등을
          </div>
          <div className="w-fit h-fit font-SCDream4 text-[12px] text-textColor mt-2">
            적어주면 좋습니다.
          </div>
        </div>
      </div>
      <div className="w-full h-fit flex flex-row justify-start items-end font-SCDream5 text-md text-textColor mt-10">
        <div>진행방식</div>
        <div className="text-pointColor">*</div>
        <div className="h-full flex justify-end items-end text-[12px] text-pointColor">
          현재 {curInfo.length} / 최대 1000글자
        </div>
      </div>
      {screenWidth > 764 ? (
        <div data-color-mode="light" className="w-full">
          <MDEditor
            height={400}
            value={curInfo}
            onChange={handleChangeValue}
            preview="live"
            commands={[bold, italic]}
            className="flex flex-col w-full h-full mt-5"
          />
        </div>
      ) : (
        <div data-color-mode="light" className="w-full">
          <MDEditor
            height={400}
            value={curInfo}
            onChange={handleChangeValue}
            preview="edit"
            commands={[bold, italic]}
            className="flex flex-col w-full h-full mt-5"
          />
        </div>
      )}
      <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
        <SmallBtn type="button" onClick={toBack}>
          이전
        </SmallBtn>
        <SmallBtn type="submit" onClick={onClick} css="ml-5">
          등 록
        </SmallBtn>
      </div>
    </>
  );
};

export default Curriculum;

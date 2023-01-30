import { bold, italic } from "@uiw/react-md-editor/lib/commands/";
import { usePostCurModi } from "hooks/detail/usePostCurModi";
import useWindowSize from "hooks/useWindowSize";
import dynamic from "next/dynamic";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { Dispatch, SetStateAction } from "react";
import SmallBtn from "../reuse/btn/SmallBtn";
import ModalBackDrop from "../reuse/container/ModalBackDrop";
import CreateModalContainer from "../reuse/CreateModalContainer";
const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
  ssr: false,
});

interface Props {
  textData: string;
  setTextData: Dispatch<SetStateAction<string>>;
  modalOpen: () => void;
}

const DetailCurModi = ({ textData, setTextData, modalOpen }: Props) => {
  const { mutate, isSuccess } = usePostCurModi();
  const router = useRouter();
  const id = Number(router.query.id);
  const screenWidth = useWindowSize();
  const handleChangeValue = (e: any) => {
    setTextData(e);
  };
  const onSubmit = () => {
    const assemble = {
      id,
      textData,
    };
    // console.log(assemble);
    mutate(assemble);
  };
  useEffect(() => {
    if (isSuccess) {
      modalOpen();
    }
  }, [isSuccess]);
  return (
    <ModalBackDrop onClick={modalOpen}>
      <CreateModalContainer>
        <div className="flex flex-col justify-center items-center w-full h-fit">
          <div className="w-fit h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[13.5px] text-pointColor">
            수정할 내용을 입력해주세요
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

        <div className="w-full h-fit flex flex-row justify-start items-center font-SCDream5 text-md text-textColor mt-10">
          진행방식
        </div>
        {screenWidth > 764 ? (
          <div data-color-mode="light" className="w-full">
            <MDEditor
              height={400}
              value={textData}
              onChange={e => handleChangeValue(e)}
              preview="live"
              commands={[bold, italic]}
              className="flex flex-col w-full h-full mt-5"
            />
          </div>
        ) : (
          <div data-color-mode="light" className="w-full">
            <MDEditor
              height={400}
              value={textData}
              onChange={e => handleChangeValue(e)}
              preview="edit"
              commands={[bold, italic]}
              className="flex flex-col w-full h-full mt-5"
            />
          </div>
        )}
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn onClick={onSubmit}>수정</SmallBtn>
        </div>
      </CreateModalContainer>
    </ModalBackDrop>
  );
};

export default DetailCurModi;

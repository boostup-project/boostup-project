import { Dispatch, SetStateAction } from "react";

interface Props {
  children: React.ReactNode;
  bold: boolean;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
}

const DetailTabBtn = ({ children, bold, onClick }: Props) => {
  return (
    <>
      <button
        type="button"
        className={
          bold
            ? "w-48 tablet:h-14 h-12 bg-pointColor text-white font-SCDream7 flex flex-col justify-center items-center tablet:text-lg text-sm border-t-[1px] border-r-[1px] border-l-[1px] border-borderColor rounded-t-2xl mr-3"
            : "w-48 tablet:h-14 h-12 bg-white text-textColor font-SCDream4 flex flex-col justify-center items-center tablet:text-lg text-sm border-t-[1px] border-r-[1px] border-l-[1px] border-borderColor rounded-t-2xl mr-3"
        }
        onClick={onClick}
      >
        {children}
      </button>
    </>
  );
};

export default DetailTabBtn;

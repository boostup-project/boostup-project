interface Props {
  children: React.ReactNode;
  bold: boolean;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
}

const MypageTabBtn = ({ children, bold, onClick }: Props) => {
  return (
    <button
      type="button"
      className={
        bold

          ? "w-48 mx-14 tablet:mx-10 mx-5 tablet:h-14 h-10 bg-pointColor text-white font-SCDream7 flex flex-col justify-center items-center tablet:text-lg text-sm border-t-[1px] border-r-[1px] border-l-[1px] border-borderColor rounded-t-2xl"
          : "w-48 mx-14 tablet:mx-10 mx-5 tablet:h-14 h-10 bg-white text-textColor font-SCDream4 flex flex-col justify-center items-center tablet:text-lg text-sm border-t-[1px] border-r-[1px] border-l-[1px] border-borderColor rounded-t-2xl"

      }
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default MypageTabBtn;

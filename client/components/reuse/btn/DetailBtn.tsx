interface Props {
  children: React.ReactNode;
  bold: boolean;
  remove: boolean;
  onClick: () => void;
}
const DetailBtn = ({ children, bold, remove, onClick }: Props) => {
  return (
    <button
      type="button"
      className={
        bold
          ? "w-full h-14 flex flex-col justify-center items-center font-SCDream4 bg-pointColor border border-borderColor text-lg text-white mx-3 rounded-xl mb-3"
          : remove
          ? "w-full h-14 flex flex-col justify-center items-center font-SCDream4 bg-negativeMessage border border-borderColor text-lg text-white mx-3 rounded-xl mb-3"
          : "w-full h-14 flex flex-col justify-center items-center font-SCDream4 bg-white border border-borderColor text-lg text-textColor mx-3 rounded-xl mb-3"
      }
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default DetailBtn;

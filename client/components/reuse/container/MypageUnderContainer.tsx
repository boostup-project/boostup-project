interface Props {
  children: React.ReactNode;
}

const MypageUnderContainer = ({ children }: Props) => {
  return (
    <div className="flex w-full desktop:min-w-[1000px] min-w-[95%] h-fit flex-row justify-start items-center bg-white border-t-2">
      {children}
    </div>
  );
};

export default MypageUnderContainer;

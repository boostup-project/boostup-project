interface Props {
  children: React.ReactNode;
}

const MypageContentContainer = ({ children }: Props) => {
  return (
    <div className="flex w-3/4 desktop:min-w-[1000px] min-w-[95%] h-fit flex-row justify-start items-center bg-white border-2 border-t-borderColor">
      {children}
    </div>
  );
};

export default MypageContentContainer;

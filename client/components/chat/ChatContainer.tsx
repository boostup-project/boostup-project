interface Props {
  children: React.ReactNode;
}

const ChatContainer = ({ children }: Props) => {
  return (
    <>
      <div className="tablet:absolute tablet:top-40 tablet:min-w-[764px] tablet:w-2/3 w-[95%] tablet:h-2/3 h-3/4 flex flex-col justify-center items-center border-2 border-borderColor rounded-xl desktop:max-w-[1000px]">
        {children}
      </div>
    </>
  );
};

export default ChatContainer;

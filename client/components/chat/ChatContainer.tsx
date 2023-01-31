interface Props {
  children: React.ReactNode;
}

const ChatContainer = ({ children }: Props) => {
  return (
    <>
      <div className="w-[764px] h-[740px] flex flex-col justify-center items-center border border-borderColor">
        {children}
      </div>
    </>
  );
};

export default ChatContainer;

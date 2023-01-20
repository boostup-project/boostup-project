interface Props {
  children: React.ReactNode;
}

const DetailContentContainer = ({ children }: Props) => {
  return (
    <div className="flex desktop:min-w-[80%] min-w-full min-h-[500px] h-fit rounded-tr-xl rounded-b-xl flex-row justify-start items-center bg-white border-2 border-borderColor">
      {children}
    </div>
  );
};

export default DetailContentContainer;

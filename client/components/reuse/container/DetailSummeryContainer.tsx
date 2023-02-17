interface Props {
  children: React.ReactNode;
}

const DetailSummeryContainer = ({ children }: Props) => {
  return (
    <div className="flex w-3/4 desktop:min-w-[1000px] min-w-[95%] h-fit rounded-xl flex-col justify-between items-center bg-white border-2 border-borderColor">
      {children}
    </div>
  );
};

export default DetailSummeryContainer;

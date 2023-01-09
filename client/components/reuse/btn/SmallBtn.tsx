interface Props {
  onClick?: (e: any) => void;
  children: React.ReactNode;
}

const SmallBtn = ({ onClick, children }: Props) => {
  return (
    <>
      <button
        className={`font-SCDream4 w-1/6 py-2 bg-pointColor rounded-md text-white text-sm`}
        onClick={onClick}
      >
        {children}
      </button>
    </>
  );
};

export default SmallBtn;

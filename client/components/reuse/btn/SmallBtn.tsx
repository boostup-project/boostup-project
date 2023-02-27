interface Props {
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  children: React.ReactNode;
  css?: string;
  type?: "button" | "submit" | "reset" | undefined;
}

const SmallBtn = ({ onClick, children, css, type }: Props) => {
  return (
    <>
      <button
        type={type && type}
        className={`font-SCDream4 w-1/6 py-2 bg-pointColor rounded-md text-white text-sm ${css}`}
        onClick={onClick}
      >
        {children}
      </button>
    </>
  );
};

export default SmallBtn;

interface Props {
  onClick?: () => void;
  children?: React.ReactNode;
  className: string;
}

const AuthBtn = ({ onClick, children, className }: Props) => {
  return (
    <>
      <button
        className={`font-SCDream4 w-1/4 py-2 bg-pointColor rounded-md text-white text-sm ${className}`}
        onClick={onClick}
      >
        {children}
      </button>
    </>
  );
};

export default AuthBtn;

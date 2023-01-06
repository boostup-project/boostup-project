interface Props {
  onClick: (e: any) => void;
  children: string;
  disabled: boolean;
}

const NewAuthBtn = ({ onClick, children, disabled }: Props) => {
  return (
    <>
      {!disabled ? (
        <input
          type="button"
          disabled={disabled}
          className="font-SCDream4 w-1/4 py-2 bg-pointColor rounded-md text-white text-sm cursor-pointer"
          value={children}
          onClick={onClick}
        />
      ) : (
        <input
          type="button"
          disabled={disabled}
          className="font-SCDream4 w-1/4 py-2 bg-disabledBtnColor rounded-md text-white text-sm"
          value={children}
          onClick={onClick}
        />
      )}
    </>
  );
};

export default NewAuthBtn;

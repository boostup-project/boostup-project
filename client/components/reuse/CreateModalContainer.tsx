interface Props {
  children: React.ReactNode;
}

const CreateModalContainer = ({ children }: Props) => {
  return (
    <>
      <div className="absolute flex flex-col p-7 justify-start items-center top-28 min-h-[500px] rounded-xl desktop:w-1/2 tablet:w-3/4 w-[90%] h-fit bg-white border border-borderColor">
        {children}
      </div>
    </>
  );
};

export default CreateModalContainer;

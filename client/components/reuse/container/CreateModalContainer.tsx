interface Props {
  children: React.ReactNode;
}

const CreateModalContainer = ({ children }: Props) => {
  return (
    <>
      <div className="fixed overflow-auto flex flex-col py-7 px-28 justify-start items-center top-28 max-h-[80%] rounded-xl desktop:w-1/2 tablet:w-3/4 w-[90%] h-fit bg-white border border-borderColor">
        {children}
      </div>
    </>
  );
};

export default CreateModalContainer;

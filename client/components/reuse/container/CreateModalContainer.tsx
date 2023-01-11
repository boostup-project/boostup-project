interface Props {
  children: React.ReactNode;
}

const CreateModalContainer = ({ children }: Props) => {
  return (
    <>
      <div className="fixed overflow-auto flex flex-col py-7 tablet:px-20 px-5 justify-start items-center top-28 max-h-[80%] rounded-xl desktop:w-1/2 desktop:min-w-[800px] tablet:w-3/4 w-[90%] h-fit bg-white border border-borderColor">
        {children}
      </div>
    </>
  );
};

export default CreateModalContainer;

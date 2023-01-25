interface Props {
  children: React.ReactNode;
}

const CreateModalContainer = ({ children }: Props) => {
  return (
    <>
      <div
        className="absolute flex flex-col p-7 justify-start items-center top-16 bottom-10 min-h-[500px] max-h-[800px]  desktop:w-1/2 tablet:w-3/4 w-[90%] h-screen overflow-y-auto bg-white border border-borderColor rounded-xl"
        onClick={e => e.stopPropagation()}
      >
        {children}
      </div>
    </>
  );
};

export default CreateModalContainer;

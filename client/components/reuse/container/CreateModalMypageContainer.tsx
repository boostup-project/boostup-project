interface Props {
  children: React.ReactNode;
}

const CreateModalMypageContainer = ({ children }: Props) => {
  return (
    <>
      <div
        className="fixed overflow-auto flex flex-col py-7 tablet:px-20 px-5 justify-start items-center top-28 max-h-[80%] rounded-xl desktop:w-1/3 tablet:w-2/5 tablet:min-w-[500px] w-[90%] h-fit bg-white border border-borderColor z-50"
        onClick={e => e.stopPropagation()}
      >
        {children}
      </div>
    </>
  );
};

export default CreateModalMypageContainer;

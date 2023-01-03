interface Props {
  children: React.ReactNode;
}

const AuthContainer = ({ children }: Props) => {
  return (
    <>
      <div className="flex flex-col justify-center items-center desktop:w-1/3 tablet:w-1/2 w-full min-h-[70%] h-fit bg-white p-10">
        {children}
      </div>
    </>
  );
};

export default AuthContainer;

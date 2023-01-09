interface Props {
  children: React.ReactNode;
}

const AuthContainer = ({ children }: Props) => {
  return (
    <>
      <div className="flex flex-col justify-center items-center desktop:w-1/3 tablet:w-1/2 w-5/6 min-h-[300px] h-fit bg-white p-10 rounded-lg">
        {children}
      </div>
    </>
  );
};

export default AuthContainer;

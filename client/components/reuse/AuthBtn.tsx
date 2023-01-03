interface Props {
    onClick : () => void;
    children : React.ReactNode;
}

const AuthBtn = ({onClick, children}:Props) => {
    return (
        <>
            <button className="mt-4 font-SCDream4 w-1/4 py-2 bg-pointColor rounded-md text-white text-sm" onClick={onClick}>
                {children}
            </button>
        </>
    )
}

export default AuthBtn;
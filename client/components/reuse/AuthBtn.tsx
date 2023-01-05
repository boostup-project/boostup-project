interface Props {
    onClick : (e:any) => void;
    children : React.ReactNode;
}

const AuthBtn = ({onClick, children}:Props) => {
    return (
        <>
            <button className="font-SCDream4 w-1/4 py-2 bg-pointColor rounded-md text-white text-sm" onClick={onClick}>
                {children}
            </button>
        </>
    )
}

export default AuthBtn;
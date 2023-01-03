interface Props {
    placeholder : string,
    value : string,
    onChange : (e:any) => void;
}

const InputBox = ({placeholder, value, onChange}:Props) => {
    return (
        <>
            <input type="text" placeholder={placeholder} value={value} onChange={onChange} className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor"></input>
        </>
    )
}

export default InputBox;
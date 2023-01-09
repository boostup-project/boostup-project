import { useForm } from "react-hook-form";

interface Props {
  placeholder: string;
  value: string;
  onChange: (e: any) => void;
  formId : string;
}

const FormInputBox = ({ placeholder, value, onChange, formId }: Props) => {
  const { register } = useForm();

  return (
    <>
      <input
        type="text"
        placeholder={placeholder}
        value={value}
        className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 text-sm text-textColor"
        {...register(formId, {
            required: "Required",
            onChange: onChange,
            pattern: {
                value: /^[a-z0-9]{5,12}$/,
                message: "올바르지 않은 닉네임형식입니다."
            }
        })}
      />
    </>
  );
};

export default FormInputBox;

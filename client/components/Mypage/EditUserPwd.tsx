import AuthBtn from "components/reuse/btn/AuthBtn";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import usePostPwdChange from "hooks/mypage/usePostPwdChange";
import usePostPwdCheck from "hooks/mypage/usePostPwdCheck";
import { useEffect } from "react";
import { ChangeEvent, useState } from "react";
import { useForm } from "react-hook-form";

interface Props {
  editPWd: () => void;
}

interface PwdEditData {
  [index: string]: string;
}

const EditUserPwd = ({ editPWd }: Props) => {
  const [currentPwd, setCurrentPwd] = useState("");
  const [isEditable, setIsEditable] = useState(false);
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<PwdEditData>({ mode: "onBlur" });

  /** 비밀번호 확인 로직 */
  const currentPwdReciever = (e: ChangeEvent<HTMLInputElement>) => {
    setCurrentPwd(e.target.value);
  };
  const { mutate: mutatePwdCheck, isSuccess: successPwdCheck } =
    usePostPwdCheck();
  const pwdCheck = () => {
    console.log(currentPwd);
    mutatePwdCheck(currentPwd);
  };
  useEffect(() => {
    if (successPwdCheck) {
      setIsEditable(prev => !prev);
    }
  }, [successPwdCheck]);

  /** 비밀번호 변경 로직 */
  const { mutate: mutatePwdChange, isSuccess: sucessPwdChange } =
    usePostPwdChange();
  const editSubmit = (e: PwdEditData) => {
    mutatePwdChange(e.password);
    editPWd();
  };

  return (
    <ModalBackDrop onClick={editPWd}>
      <CreateModalContainer>
        <div className="w-full font-SCDream7 text-borderColor desktop:w-4/5">
          비밀번호 변경하기
        </div>
        <form
          className="w-full font-SCDream5 text-sm desktop:w-4/5"
          onSubmit={handleSubmit(editSubmit)}
        >
          <div>
            <div className="mt-4">현재 비밀번호</div>
            <div className="w-full flex mt-1 tablet:w-full">
              <label className="w-9/12">
                <input
                  type="password"
                  placeholder="닉네임 중복검사를 해주세요"
                  className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-l-xl font-SCDream4 text-xs text-textColor tablet:text-sm"
                  {...register("currentPwd")}
                  onChange={e => currentPwdReciever(e)}
                />
              </label>
              <div
                className="w-3/12 flex justify-center items-center text-xs border border-l-0 bg-borderColor border-borderColor rounded-r-xl text-bgColor cursor-pointer break-keep text-center"
                onClick={pwdCheck}
              >
                비밀번호 확인
              </div>
            </div>
          </div>
          {isEditable && (
            <div>
              <div className="mt-4">새로운 비밀번호</div>
              <div className="w-full flex mt-1 tablet:w-full">
                <label className="w-full">
                  <input
                    disabled={isEditable ? false : true}
                    type="password"
                    placeholder="새로운 비밀번호를 입력해주세요"
                    className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm"
                    {...register("password", {
                      required: "필수로 입력해야되는 값입니다.",
                      pattern: {
                        value:
                          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/gm,
                        message:
                          "최소 8 자, 하나 이상의 대문자, 하나 이상의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함합니다",
                      },
                    })}
                  />
                </label>
              </div>
              <p className="w-full text-xs text-negativeMessage mt-1 break-keep">
                {errors?.password?.message?.toString()}
              </p>
            </div>
          )}
          {isEditable && (
            <div>
              <div className="mt-4">비밀번호 확인</div>
              <div className="w-full flex mt-1 tablet:w-full">
                <label className="w-full">
                  <input
                    disabled={isEditable ? false : true}
                    type="password"
                    placeholder="비밀번호를 한번 더 입력해주세요"
                    className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm"
                    {...register("passwordCheck", {
                      required: "필수로 입력해야되는 값입니다.",
                      validate: (val: string) => {
                        if (watch("password") !== val) {
                          return "비밀번호가 일치하지 않습니다.";
                        }
                      },
                    })}
                  />
                </label>
              </div>
              <p className="w-full text-xs text-negativeMessage mt-1 break-keep">
                {errors?.passwordCheck?.message?.toString()}
              </p>
            </div>
          )}
          <div className="mt-7 flex justify-center w-full tablet:w-full">
            <div
              className="flex justify-center items-center w-1/4 py-2 bg-borderColor rounded-xl text-negativeMessage text-sm mr-5 cursor-pointer"
              onClick={editPWd}
            >
              취소하기
            </div>
            {isEditable && <AuthBtn>변경하기</AuthBtn>}
          </div>
        </form>
      </CreateModalContainer>
    </ModalBackDrop>
  );
};

export default EditUserPwd;

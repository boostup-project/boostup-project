import CreateModalContainer from "../reuse/container/CreateModalContainer";
import { useForm } from "react-hook-form";
import SmallBtn from "../reuse/btn/SmallBtn";
import { langDict, dayDict } from "../reuse/dict";

import usePostApply from "hooks/detail/usePostApply";
import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import { PropsWithChildren } from "react";
import { useRouter } from "next/router";
interface Application {
  days: string;
  languages: string;
  requests: string;
  id: number;
}
interface ModalDefaultType {
  onClickToggleModal: () => void;
}

const ApplyModal = ({
  onClickToggleModal,
}: PropsWithChildren<ModalDefaultType>) => {
  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Application>({ mode: "onBlur" });

  const router = useRouter();
  const lessonId = Number(router.query.id);
  const { mutate, isSuccess, isError } = usePostApply();

  const langArr = Object.keys(langDict);
  const dayArr = Object.keys(dayDict);

  const onSubmit = (e: Application) => {
    const applyData = {
      days: e.days.toString(),
      languages: e.languages.toString(),
      requests: e.requests,
      id: lessonId,
    };

    mutate(applyData);
  };

  useEffect(() => {
    if (isSuccess) {
      Swal.fire({
        text: "신청이 완료되었습니다",
        icon: "success",
        confirmButtonColor: "#3085d6",
      });
      onClickToggleModal();
    }

    if (isError) {
      Swal.fire({
        text: "다시 신청해주세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  }, [isSuccess, isError]);

  return (
    <>
      <div className="fixed z-10 top-0 left-0 bottom-0 right-0 bg-modalBgColor grid place-items-center">
        <CreateModalContainer>
          <form
            className="flex flex-col items-center w-full text-sm z-20"
            onSubmit={handleSubmit(onSubmit)}
          >
            <div className="flex w-full desktop:w-4/6 h-fit font-SCDream7 text-lg text-textColor mt-4 mb-2">
              신청하기
            </div>
            <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
              <div>희망요일</div>
              <div className="text-pointColor">*</div>{" "}
              <div className="text-pointColor text-xs">
                3개까지 선택가능합니다
              </div>
            </div>
            <div className="w-11/12 list h-fit flex flex-row items-start desktop:w-4/6">
              {dayArr.map((el: string, idx: number) => {
                if (idx < 4) {
                  return (
                    <div key={idx} className="w-1/4 h-fit text-xs">
                      <label className="checkboxLabel">
                        <input
                          type="checkbox"
                          value={el}
                          {...register("days", { required: "true" })}
                        />
                        {el}
                      </label>
                    </div>
                  );
                }
              })}
            </div>
            <div className="w-11/12 list h-fit flex flex-row items-start mt-3 desktop:w-4/6">
              {dayArr.map((el: string, idx: number) => {
                if (idx > 3) {
                  return (
                    <div key={idx} className="w-1/4 h-fit text-xs">
                      <label className="checkboxLabel">
                        <input
                          type="checkbox"
                          value={el}
                          {...register("days", { required: "true" })}
                        />
                        {el}
                      </label>
                    </div>
                  );
                }
              })}
            </div>
            <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
              {errors?.days && <span>필수 정보입니다</span>}
            </p>
            <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
              <div>희망언어</div>
              <div className="text-pointColor">*</div>{" "}
              <div className="text-pointColor text-xs">
                3개까지 선택가능합니다
              </div>
            </div>
            <div className="w-11/12 list h-fit flex flex-row items-start desktop:w-4/6">
              {langArr.map((el: string, idx: number) => {
                if (idx < 4) {
                  return (
                    <div key={idx} className="w-1/4 h-fit text-xs">
                      <label className="checkboxLabel">
                        <input
                          type="checkbox"
                          value={el}
                          {...register("languages", { required: "true" })}
                        />
                        {el}
                      </label>
                    </div>
                  );
                }
              })}
            </div>
            <div className="w-11/12 list h-fit flex flex-row items-start mt-3 desktop:w-4/6">
              {langArr.map((el: string, idx: number) => {
                if (idx > 3) {
                  return (
                    <div key={idx} className="w-1/4 h-fit text-xs">
                      <label className="checkboxLabel">
                        <input
                          type="checkbox"
                          value={el}
                          {...register("languages", { required: "true" })}
                        />
                        {el}
                      </label>
                    </div>
                  );
                }
              })}
            </div>
            <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
              {errors?.languages && <span>필수 정보입니다</span>}
            </p>
            <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
              요청사항
            </div>
            <input
              type="text"
              placeholder="요청사항을 입력하세요"
              className="w-11/12 desktop:w-4/6 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm "
              {...register("requests")}
            />
            <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
              <SmallBtn css="mr-4" onClick={onClickToggleModal}>
                취 소
              </SmallBtn>
              <SmallBtn>신 청</SmallBtn>
            </div>
          </form>
        </CreateModalContainer>
      </div>
    </>
  );
};

export default ApplyModal;

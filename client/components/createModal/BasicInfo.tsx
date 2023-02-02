import Select from "react-select";
import SmallBtn from "components/reuse/btn/SmallBtn";
import { useState, Dispatch, SetStateAction } from "react";
import { useForm, Controller } from "react-hook-form";
import { langDict, addDict } from "../reuse/dict";
import { IconImg } from "assets/icon";
import { modalImgTxt } from "assets/color/color";
import { SetterOrUpdater } from "recoil";
import { Info } from "./WriteModal";
import { useEffect } from "react";
import { useRouter } from "next/router";
import { useRecoilState } from "recoil";
import { editMode } from "atoms/detail/detailAtom";

interface BasicInfo {
  [index: string]: string | string[];
}

interface Props {
  basicInfo: Info;
  setBasicInfo: Dispatch<SetStateAction<Info>>;
  toWrite: () => void;
  setStep: SetterOrUpdater<number>;
}

const BasicInfo = ({ basicInfo, setBasicInfo, toWrite, setStep }: Props) => {
  const [previewImg, setPreviewImg] = useState<string>(
    basicInfo ? (basicInfo.profileImage as string) : "",
  );

  const [mode, setMode] = useRecoilState(editMode);

  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<BasicInfo>({ mode: "onBlur" });

  const langArr = Object.keys(langDict);
  const addArr = Object.keys(addDict);

  const options: any = [
    ...addArr.map((el, idx) => ({
      key: idx,
      value: addDict[el],
      label: el,
    })),
  ];

  const defaultAddress: any = [Object.assign([], basicInfo.address as any)];
  const router = useRouter();
  /** 미리보기 이미지 생성**/
  const insertImg = (e: any) => {
    let reader = new FileReader();

    if (e.target.files[0]) {
      reader.readAsDataURL(e.target.files[0]);
    }
    reader.onloadend = () => {
      const previewImgUrl = reader.result as string;

      if (previewImgUrl) {
        setPreviewImg(previewImgUrl);
      }
    };
  };
  const deleteImg = () => {
    setPreviewImg("");
  };

  const handleClickCancel = () => {
    toWrite();
    setMode(false);
  };

  const onSubmit = (basicData: BasicInfo) => {
    if (mode) {
      const lessonId = Number(router.query.id);
      console.log(lessonId);
      const json = JSON.stringify({
        languages: basicData.language,
        title: basicData.title,
        company: basicData.company,
        career: basicData.career,
        cost: basicData.cost,
        address: basicData.address,
      });
      const blob = new Blob([json], { type: "application/json" });
      const formData = new FormData();

      formData.append("data", blob);
    } else {
      setBasicInfo(basicData);
      setStep(prev => prev + 1);
    }
  };

  // 페이지 랜더링이 데이터 Fetch 보다 먼저 진행됨에 따른 딜레이 해결

  return (
    <>
      <div className="w-full h-fit font-SCDream5 text-xs text-center text-pointColor mt-2 tablet:text-sm">
        메인페이지에 보일 과외에 대한 간략한 정보를 입력해주세요
      </div>
      <div className="w-full h-fit font-SCDream5 text-xs text-center text-pointColor mb-5 tablet:text-sm">
        더 자세한 내용은 다음 단계에서 작성해주세요
      </div>
      <form
        className="placeholder:text-center w-full flex flex-col items-center text-sm tablet:text-base"
        onSubmit={handleSubmit(onSubmit)}
      >
        <div className="flex w-11/12 desktop:w-4/6 h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
          <div>프로필 사진</div>
          <div className="text-pointColor">*</div>
        </div>
        <div className="w-11/12 flex items-center rounded-xl justify-center h-fit border mb-3 desktop:w-4/6">
          <label className="flex flex-col w-full h-32 border-borderColor hover:bg-gray-100 hover:border-gray-300">
            <div className="relative flex flex-col h-fit  items-center justify-center pt-7">
              {previewImg ? (
                <>
                  <img
                    id="preview"
                    className="absolute inset-0 w-full h-32 object-contain"
                    src={previewImg}
                  />
                  <button
                    className="absolute top-1 right-3  text-red-500 text-lg"
                    onClick={deleteImg}
                  >
                    삭제
                  </button>
                </>
              ) : (
                <>
                  <IconImg width="69px" heigth="62px" fill={modalImgTxt} />
                  <p className="pt-1 text-sm h-fit tracking-wider text-modalImgTxt group-hover:text-gray-600">
                    클릭하여 사진을 첨부하세요
                  </p>
                </>
              )}
            </div>
            {Object.keys(basicInfo).length ? (
              <input
                type="file"
                className="opacity-0 rounded-xl"
                accept="image/jpeg,.txt"
                {...register("profileImg")}
                onChange={e => insertImg(e)}
              />
            ) : (
              <input
                type="file"
                className="opacity-0 rounded-xl"
                accept="image/jpeg,.txt"
                {...register("profileImg", {
                  required: "필수 정보입니다",
                })}
                onChange={e => insertImg(e)}
              />
            )}
          </label>
        </div>
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.profileImg?.message}
        </p>
        <div className="w-11/12 flex flex-row justify-start items-start h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          타이틀 <div className="text-pointColor">*</div>
        </div>
        <input
          type="text"
          placeholder="타이틀을 입력하세요"
          className="w-11/12 desktop:w-4/6 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm "
          defaultValue={basicInfo?.title as string}
          {...register("title", {
            required: "필수 정보입니다.",
            maxLength: {
              value: 12,
              message: "타이틀을 12자 이하로 입력하여 주시길 바랍니다.",
            },
          })}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.title?.message}
        </p>
        <div className="flex flex-row justify-start items-start w-11/12  h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          <div>가능언어</div>
          <div className="text-pointColor">*</div>{" "}
          <div className="text-pointColor text-xs">3개까지 선택가능합니다</div>
        </div>
        <div className="w-11/12 list h-fit flex flex-row items-start desktop:w-4/6">
          {langArr.map((el: string, idx: number) => {
            if (idx < 4) {
              return (
                <div key={idx} className="w-1/4 h-fit text-xs">
                  <label className="checkboxLabel">
                    {Object.keys(basicInfo).length ? (
                      <input
                        type="checkbox"
                        value={el}
                        {...register("languages", { required: "true" })}
                        defaultChecked={
                          [...(basicInfo.languages as any)].includes(el)
                            ? true
                            : false
                        }
                      />
                    ) : (
                      <input
                        type="checkbox"
                        value={el}
                        {...register("languages", { required: "true" })}
                      />
                    )}
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
                    {Object.keys(basicInfo).length ? (
                      <input
                        type="checkbox"
                        value={el}
                        {...register("languages", { required: "true" })}
                        defaultChecked={
                          [...(basicInfo.languages as any)].includes(el)
                            ? true
                            : false
                        }
                      />
                    ) : (
                      <input
                        type="checkbox"
                        value={el}
                        {...register("languages", { required: "true" })}
                      />
                    )}

                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.language && <span>필수 정보입니다</span>}
        </p>
        <div className="flex flex-row justify-start items-start w-11/12 h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          재직 회사/학교 <div className="text-pointColor">*</div>
        </div>
        <input
          type="text"
          placeholder="현재 회사 또는 학교를 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("company", { required: "필수 정보입니다." })}
          defaultValue={basicInfo.company as string}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.company?.message}
        </p>
        <div className="flex flex-row justify-start items-start w-11/12 h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          경력 <div className="text-pointColor">*</div>
        </div>
        <input
          type="number"
          placeholder="경력을 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("career", {
            required: "필수 정보입니다.",
          })}
          defaultValue={basicInfo.career as string}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.career?.message}
        </p>
        <label className="w-11/12 flex flex-col items-start mt-5 desktop:w-4/6">
          <div className="flex flex-row justify-start items-stretch w-full h-fit font-SCDream5 text-sm text-textColor my-2">
            <div>과외가능지역</div>
            <div className="text-pointColor">*</div>
            <div className="text-pointColor text-xs">
              3개까지 선택가능합니다
            </div>
          </div>
          <div className="w-full flex justify-between items-stretch">
            <div className="w-4/12 text-center flex flex-col even:items-end">
              <div>시,도</div>
              <div className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor bg-white text-center mt-5">
                <span>서울특별시</span>
              </div>
            </div>
            <div className="w-8/12 text-center flex flex-col items-stretch ml-4">
              <div>구</div>
              <div className="w-full h-fit rounded-xl mt-5">
                <Controller
                  name="address"
                  control={control}
                  rules={{
                    required: "필수 정보입니다.",
                    maxLength: {
                      value: 3,
                      message: "3개까지만 넣어주세요",
                    },
                  }}
                  // defaultValue={defaultAddress[0].map(
                  //   (el: string, idx: number) => {
                  //     return {
                  //       key: idx,
                  //       value: addDict[el],
                  //       label: el,
                  //     };
                  //   },
                  // )}
                  render={({ field }) => (
                    <Select
                      {...field}
                      placeholder={<div>구 선택</div>}
                      styles={{
                        control: baseStyles => ({
                          ...baseStyles,
                          borderRadius: "12px",
                          fontSize: "14px",
                          fontFamily: "SCDream2",
                          borderColor: "#A8A7A7",
                          borderWidth: "1px",
                        }),
                        menu: base => ({
                          ...base,
                          fontFamily: "SCDream2",
                          fontSize: "14px",
                        }),
                      }}
                      isMulti
                      options={options}
                    />
                  )}
                />
              </div>
            </div>
          </div>
        </label>
        <p className="text-right w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.address?.message}
        </p>
        <div className="w-11/12 h-fit flex flex-row justify-start items-start font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          수업료<div className="text-pointColor">*</div>
        </div>
        <input
          type="number"
          placeholder="회 당 수업료를 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("cost", { required: "필수 정보입니다." })}
          defaultValue={basicInfo.cost as string}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.cost?.message}
        </p>
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn onClick={handleClickCancel}>취 소</SmallBtn>
          <SmallBtn css="ml-5">다 음</SmallBtn>
        </div>
      </form>
    </>
  );
};

export default BasicInfo;

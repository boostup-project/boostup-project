import Select from "react-select";
import SmallBtn from "components/reuse/btn/SmallBtn";
import { useState, Dispatch, SetStateAction } from "react";
import { useForm, Controller } from "react-hook-form";
import { langDict, addDict } from "../reuse/dict";
import { IconImg } from "assets/icon";
import { modalImgTxt } from "assets/color/color";
import { SetterOrUpdater } from "recoil";
import { Info } from "./WriteModal";
import { useRouter } from "next/router";
import { useRecoilState } from "recoil";
import { editMode } from "atoms/detail/detailAtom";
import usePostBasicModi from "hooks/detail/usePostBasicModi";
import imageCompression from "browser-image-compression";
import { useEffect } from "react";
import { AnyARecord } from "dns";
import { ErrorMessage } from "@hookform/error-message";

export interface BasicInfo {
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
  const [btnWord, setBtnWord] = useState<string>();

  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
    watch,
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

  const router = useRouter();
  useEffect(() => {
    if (mode) {
      setBtnWord("수정");
    } else {
      setBtnWord("다음");
    }
  }, []);

  /** 미리보기 이미지 생성**/
  const insertImg = async (e: any) => {
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

  // 이미지 압축 함수
  const compressImage = async (image: any) => {
    try {
      const options = {
        maxSizeMb: 2,
        maxWidthOrHeight: 300,
      };
      return await imageCompression(image, options);
    } catch (e) {
      console.log(e);
    }
  };

  const { mutate: basicModiMutate } = usePostBasicModi();

  const onSubmit = async (basicData: BasicInfo) => {
    if (mode) {
      const lessonId = Number(router.query.id);
      const { address, languages, profileImg } = basicData;
      const parseAddress = [...address].map((el: any) => el.value);
      const parseLang = [...languages].map((el: any) => langDict[el]);
      const proImage = profileImg[0];
      const formData = new FormData();

      let editState = "";

      if (proImage) {
        editState = "true";

        const compressImg = await compressImage(proImage);
        let compressFile;

        if (compressImg) {
          compressFile = new File([compressImg], compressImg?.name);
          formData.append("profileImage", compressFile);
        }
      } else {
        editState = "false";
      }

      const json = JSON.stringify({
        languages: parseLang,
        title: basicData.title,
        company: basicData.company,
        career: basicData.career,
        cost: basicData.cost,
        addresses: parseAddress,
        // editState : true(수정) false(수정X)
        editState: editState,
      });
      const blob = new Blob([json], { type: "application/json" });

      formData.append("data", blob);
      // 사진 수정 안했을 시는 false / 사진 수정 했을 시에는 새로운 File 전달

      basicModiMutate({ formData, lessonId });
    } else {
      setBasicInfo(basicData);
      setStep(prev => prev + 1);
    }
  };

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
          <div className="text-[11px] flex items-center text-pointColor ml-1">
            얼굴이 보이는 사진을 올려주세요.
          </div>
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
              value: 25,
              message: "타이틀을 25자 이하로 입력하여 주시길 바랍니다.",
            },
          })}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.title?.message}
        </p>
        <div className="flex flex-row justify-start items-center w-11/12  h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          <div>가능언어</div>
          <div className="text-pointColor">*</div>{" "}
          <div className="text-pointColor text-xs ml-1">
            3개까지 선택가능합니다
          </div>
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
                        {...register("languages", {
                          required: "필수 정보입니다",
                          validate: (val: string | string[]) => {
                            if (watch("languages").length > 3) {
                              return "3개가지 선택할 수 있습니다";
                            }
                          },
                        })}
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
                        {...register("languages", {
                          required: "필수 정보입니다",
                          validate: (val: string | string[]) => {
                            if (watch("languages").length > 3) {
                              return "3개가지 선택할 수 있습니다";
                            }
                          },
                        })}
                      />
                    )}
                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        {/* <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.language && <span>필수 정보입니다</span>}
        </p> */}
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
                        {...register("languages", {
                          required: "필수 정보입니다",
                          validate: (val: string | string[]) => {
                            if (watch("languages").length > 3) {
                              return "3개가지 선택할 수 있습니다";
                            }
                          },
                        })}
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
                        {...register("languages", {
                          required: "필수 정보입니다",
                          validate: () => {
                            if (watch("languages").length > 3) {
                              return "3개까지 선택할 수 있습니다";
                            }
                          },
                        })}
                      />
                    )}

                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <ErrorMessage
          errors={errors}
          name="languages"
          render={({ message }) => {
            return (
              <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
                {message}
              </p>
            );
          }}
        />
        {/* <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.languages && <span>필수 정보입니다</span>}
        </p> */}
        <div className="flex flex-row justify-start items-start w-11/12 h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2 desktop:w-4/6">
          재직 회사/학교 <div className="text-pointColor">*</div>
        </div>
        <input
          type="text"
          placeholder="현재 회사 또는 학교를 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("company", {
            required: "필수 정보입니다.",
            maxLength: {
              value: 18,
              message: "재직 회사/학교를 18자 이하로 입력하여 주시길 바랍니다.",
            },
          })}
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
          min="0"
          placeholder="경력을 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("career", {
            required: "필수 정보입니다.",
            valueAsNumber: true,
          })}
          defaultValue={basicInfo.career as string}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.career?.message}
        </p>
        <label className="w-11/12 flex flex-col items-start mt-5 desktop:w-4/6">
          <div className="flex flex-row justify-start items-center w-full h-fit font-SCDream5 text-sm text-textColor my-2">
            <div>과외가능지역</div>
            <div className="text-pointColor">*</div>
            <div className="text-pointColor text-xs ml-1">
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
                  }}
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
                      isOptionDisabled={() =>
                        watch("address") && watch("address").length > 2
                          ? true
                          : false
                      }
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
          min="0"
          placeholder="회 당 수업료를 입력하세요"
          className="w-11/12 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm desktop:w-4/6"
          {...register("cost", {
            required: "필수 정보입니다.",
            valueAsNumber: true,
          })}
          defaultValue={basicInfo.cost as string}
        />
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.cost?.message}
        </p>
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn type="button" onClick={handleClickCancel}>
            취 소
          </SmallBtn>
          <SmallBtn type="submit" css="ml-5">
            {btnWord}
          </SmallBtn>
        </div>
      </form>
    </>
  );
};

export default BasicInfo;

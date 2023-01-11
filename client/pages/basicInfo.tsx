import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import { useForm,Controller } from "react-hook-form";
import { useState,  useCallback} from "react";
import { ErrorMessage } from "@hookform/error-message";
import { langDict,addDict } from "../components/reuse/dict"
import BasicStep from "components/reuse/step/BasicStep";
import CurrStep from "components/reuse/step/CurrStep";
import ExtraStep from "components/reuse/step/ExtraStep";
import Select from "react-select";

const basicInfo = () => {
  const [previewImg, setPreviewImg] = useState<string>('');
  const [checkedList, setCheckedList] = useState<Array<string>>([]);
  const [title, setTitle] = useState<string>('')
  const [currentWork, setCurrentWork] = useState<string>('')
  const [carrer, setCareer] = useState<string>('')
  
  const [tuition, setTuition] = useState('')
  
  const langArr = Object.keys(langDict);
  const addArr = Object.keys(addDict);

    const { control, register, handleSubmit, formState: {errors}} = useForm({mode : "onBlur"});
    
    const onCheckedItem = (checked: boolean, item: string) =>(event:any)=> {
            if (checked) {
                setCheckedList([...checkedList, item]);
            } else if (!checked) {
                setCheckedList(checkedList.filter((el) => el !== item));
                console.log(checkedList)
            } }
        
    const processImg = (file:any) => {
          return new Promise((res, rej) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = (e:any) => res(e.target.result);
            reader.onerror = (err) => rej(err);
          });
      };
        
      const insertImg = (e:any) => {
          let reader = new FileReader()

            if(e.target.files[0]) {
              reader.readAsDataURL(e.target.files[0])
            }          
            reader.onloadend = () => {
              const previewImgUrl = reader.result;
              
           if(previewImgUrl) {
                setPreviewImg(previewImgUrl);
            }          
            }
          }

       const handleTitle = (e:any) =>{
            setTitle(e.target.value)
        }

        const handleCurrentWork =(e:any) =>{
          setCurrentWork(e.target.value);
        }
        const handleCarrer =(e:any) =>{
          setCareer(e.target.value);
        }
        const handleTuition =(e:any) =>{
          setTuition(e.target.value);
        }
       const onSubmit = (e: any) => {
                  console.log("Success Submit!");
                };
          
          return (
            <>
        <CreateModalContainer>
            <div className="flex flex-row w-full h-fit justify-center items-center font-SCDream3 ">
            <BasicStep></BasicStep><ExtraStep></ExtraStep><CurrStep></CurrStep></div>
            <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mt-2">
            메인페이지에 보일 과외에 대한 간략한 정보를 입력해주세요
            </div>
            <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mb-5">
            더 자세한 내용은 다음 단계에서 작성해주세요
            </div>
            <form onSubmit={handleSubmit(onSubmit)}>
        
            <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
            프로필 사진 <div className="text-pointColor">*</div></div>
            <div className="flex items-center justify-center w-full h-fit border mb-3">
                        <label
                            className="flex flex-col w-full h-32 border-borderColor rounded-xl hover:bg-gray-100 hover:border-gray-300">
                            <div className="relative flex flex-col h-fit  items-center justify-center rounded-xl  pt-7">
                                <img id="preview" className="absolute inset-0 w-full h-32 object-contain" src = {previewImg.length ? previewImg : null}/>
                                <svg xmlns="http://www.w3.org/2000/svg" className="w-12 h-12 text-gray-400 group-hover:text-gray-600" viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd" />
                                </svg>
                                <p className="rounded-xl pt-1 text-sm h-fit tracking-wider text-gray-400 group-hover:text-gray-600">
                                    Select a photo</p>
                            </div>
                            <input type="file" className="opacity-0 rounded-xl" accept="image/jpeg,.txt" onChange={(e) => insertImg(e)}/>
                        </label>
                    </div>

            <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
            타이틀 <div className="text-pointColor">*</div></div>
            <input
                type="text"
                value = {title}
                placeholder="타이틀을 입력하세요"
                className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
                {...register("email", {
                    required: "필수로 입력해야되는 값입니다.",
                    onChange: handleTitle,
                    pattern: {
                        value:/\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi,
                        message: "타이틀을 입력하세요",
                    },
                })}
                />
                <ErrorMessage errors={errors} name="title"
                render={({message})=>{
                    return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                        {message}
                    </div>}}/>

          <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
          가능언어 <div className="text-pointColor">*</div> <div className="text-pointColor text-xs">3개까지 선택가능합니다</div></div>
                  
            <>
          <div className='w-full list h-fit flex flex-row items-start '>
                {langArr.map((el: string, idx: number) => {
                  if(idx<4){return (
                    <div className="w-full h-fit flex flex-col">
                    <label className='checkboxLabel' key={idx}>
                      <input
                        type='checkbox'
                        id={el}
                        onChange={(e) => {
                          onCheckedItem(e.target.checked, e.target.id);
                        }}
                      />
                        {el}
                    </label>
                        </div>
                  );} })}
              </div>

          <div className='w-[400px] list h-fit flex flex-row items-start mt-3'>
                {langArr.map((el: string, idx: number) => {
                  if(idx>3){return (
                    <div className="w-full h-fit flex flex-col">
                    <label className='checkboxLabel' key={idx}>
                      <input
                        type='checkbox'
                        id={el}
                        onChange={(e) => {
                          onCheckedItem(e.target.checked, e.target.id);
                          console.log(checkedList)
                        }}
                      />
                        {el}
                    </label>
                        </div>
                  );} })}
              </div>
            </>
        
            <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
            재직 회사/학교 <div className="text-pointColor">*</div></div>
            <input
                type="text"
                value = {currentWork}
                placeholder="현재 회사 또는 학교를 입력하세요"
                className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
                {...register("currentWork", {
                    required: "필수로 입력해야되는 값입니다.",
                    onChange: handleCurrentWork,
                    pattern: {
                        value:/\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi,
                        message: "재직 회사/학교를 입력하세요",
                    },
                })}
                />
                <ErrorMessage errors={errors} name="currentWork"
                render={({message})=>{
                    return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                        {message}
                    </div>}}/>

           <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
           경력 <div className="text-pointColor">*</div></div>
            <input
                type="text"
                value = {carrer}
                placeholder="경력을 입력하세요"
                className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
                {...register("carrer", {
                    required: "필수로 입력해야되는 값입니다.",
                    onChange: handleCarrer,
                    pattern: {
                        value:/\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi,
                        message: "경력을 입력하세요",
                    },
                })}
                />
                <ErrorMessage errors={errors} name="carrer"
                render={({message})=>{
                    return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                        {message}
                    </div>}}/>
      
        <label className="w-full flex flex-col items-start mt-5">
          <div className="flex flex-row justify-start items-stretch w-full h-fit font-SCDream5 text-sm text-textColor my-2">
          과외가능지역 <div className="text-pointColor">*</div></div>
                  
          <div className="desktop:w-8/12 w-5/6 flex justify-between items-stretch">
            <div className="w-5/12 text-center flex flex-col even:items-end">
              <div>시,도</div>
              <div className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor bg-white text-center mt-5">
                <span>서울특별시</span>
              </div>
            </div>
            <div className="w-5/12 text-center flex flex-col items-stretch">
              <div>구</div>
              <div className="w-full h-fit items-stretch rounded-xl mt-5">
                <Controller
                  name="language"
                  render={({ field }) => (
                    <Select
                      {...field}
                      placeholder={<div>구 선택</div>}
                      styles={{
                        control: (baseStyles, state) => ({
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
                      options={[
                        ...addArr.map((el, idx) => ({
                          value: addDict[el],
                          label: el,
                          key: idx,
                        })),
                      ]}
                    />
                  )}
                  control={control}
                  defaultValue=""
                />
              </div>
            </div>
          </div>
        </label>

        <div className="flex flex-row justify-start items-start w-full h-fit font-SCDream5 text-sm text-textColor mt-4 mb-2">
        수업료<div className="text-pointColor">*</div></div>
          <input
              type="text"
              value = {tuition}
              placeholder="회 당 수업료를 입력하세요"
              className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
              {...register("tuition", {
                 required: "필수로 입력해야되는 값입니다.",
                 onChange: handleTuition,
                 pattern: {
                 value:/\^\d.*/gi,
                 message: "수업료를 입력하세요",
                  },
              })}
               />
                <ErrorMessage errors={errors} name="tuition"
                render={({message})=>{
                   return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                       {message}
                   </div>}}/>

            </form>
        </CreateModalContainer>
    </>
    )}

export default basicInfo;
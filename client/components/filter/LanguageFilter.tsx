import { detailLangDict } from "../reuse/dict";
import Image from "next/image";

const LanguageFilter = () => {
  return (
    <>
      <div className="desktop:flex hidden w-3/4 min-w-[1000px] min-h-[100px] desktop-h-[100px] h-fit mt-7 rounded-xl flex-row justify-between items-center">
        {detailLangDict.map(lang => {
          return (
            <div
              key={lang.id}
              className="desktop:w-[100px] desktop:h-[100px] w-[50px] h-[50px] desktop:my-0 my-3"
            >
              <Image
                width={100}
                height={100}
                src={lang.img}
                alt={lang.name}
                className="border border-borderColor rounded-full w-full h-full cursor-pointer bg-white"
              />
            </div>
          );
        })}
      </div>
    </>
  );
};

export default LanguageFilter;

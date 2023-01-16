import { langDict } from "../reuse/dict";
import Image from "next/image";

const LanguageFilter = () => {
  const langDict = [
    {
      id: 1,
      name: "javaScript",
      img: "/images/javascript.png",
    },
    {
      id: 2,
      name: "python",
      img: "/images/python.png",
    },
    {
      id: 3,
      name: "go",
      img: "/images/go.png",
    },
    {
      id: 4,
      name: "java",
      img: "/images/java.png",
    },
    {
      id: 5,
      name: "kotlin",
      img: "/images/kotlin.png",
    },
    {
      id: 6,
      name: "php",
      img: "/images/php.png",
    },
    {
      id: 7,
      name: "C#",
      img: "/images/C.png",
    },
    {
      id: 8,
      name: "swift",
      img: "/images/swift.png",
    },
  ];

  return (
    <>
      <div className="w-full desktop:w-3/4 desktop:min-w-[1000px] h-[100px]  mt-7 rounded-xl flex desktop:flex-row flex-col justify-between items-center">
        {langDict.map(lang => {
          return (
            <div className="w-[100px] h-[100px]">
              <Image
                width={100}
                height={100}
                key={lang.id}
                src={lang.img}
                alt={lang.name}
                className="border border-borderColor rounded-full w-full h-full cursor-pointer"
              />
            </div>
          );
        })}
      </div>
    </>
  );
};

export default LanguageFilter;

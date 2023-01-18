import { filterModal } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";
import { detailLangDict } from "components/reuse/dict";
import Image from "next/image";

const MoblieLanguageFilter = () => {
  const [modal, setModal] = useRecoilState(filterModal);

  // 하단 Navbar의 필터 버튼 클릭시 10~16줄 코드를 넣어주시면 됩니다!
  const handleModalClick = () => {
    if (modal === 0) {
      setModal(48);
    } else {
      setModal(0);
    }
  };

  return (
    <>
      {/* 테스트용으로 확인이 끝난뒤에 삭제 예정입니다 */}
      <button
        className="w-32 h-9 flex flex-col justify-center items-center cursor-pointer fixed top-48 bg-pointColor z-10 rounded-xl text-white"
        onClick={handleModalClick}
      >
        테스트버튼
      </button>
      <div
        className={
          modal === 0
            ? `right-0 ease-in-out translate-x-0 duration-500 flex flex-col justify-between items-center fixed overflow-auto scrollbar-hide w-[120px] h-[75%]  top-24 bg-white/90 border border-borderColor rounded-2xl `
            : `right-0 ease-in-out translate-x-48 duration-500 flex flex-col justify-between items-center fixed overflow-auto scrollbar-hide w-[120px] h-[75%]  top-24 bg-white/90 border border-borderColor rounded-2xl `
        }
      >
        {detailLangDict.map((el, idx) => {
          return (
            <>
              <div
                key={el.id}
                className="desktop:w-[100px] desktop:h-[100px] w-[50px] h-[50px] desktop:my-0 my-3"
              >
                <Image
                  width={100}
                  height={100}
                  src={el.img}
                  alt={el.name}
                  className="border border-borderColor rounded-full w-full h-full cursor-pointer bg-white"
                />
              </div>
            </>
          );
        })}
      </div>
    </>
  );
};

export default MoblieLanguageFilter;

import { IconPaper, IconPlace, IconRibbon, IconWon } from "assets/icon/";
import { Maincard } from "components/reuse/maincard";
import Link from "next/link";

const Cards = (maincard: Maincard) => {
  return (
    <div className="w-full h-full">
      <button>
        <div className="relative">
          <img
            className="rounded-t-lg h-full"
            src="https://www.medidata.com/wp-content/uploads/2021/11/Medidata_RaveCoder_web_keyfeatures_A1-1.jpg"
          />
          <span className="absolute top-[12px] right-[12px]">
            <IconPaper />
          </span>
        </div>
        <div className="px-1">
          <div className="flex justify-between">
            <div className="block whitespace-wrap overflow-hidden text-lg font-bold text-start mr-[2px] my-[2px] text-ellipsis">
              {maincard.name}Name
            </div>
            <div className="w-fit text-base">Title{maincard.title}</div>
          </div>
        </div>
      </button>
    </div>
  );
};
export default Cards;

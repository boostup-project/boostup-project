import Card from "components/Card/card";
import ApplyModal from "components/Detail/ApplyModal";
import DetailButtons from "components/Detail/DetailButtons";
const Cards = () => {
  return (
    <div className="mt-32 flex flex-row flex-wrap w-full desktop:w-9/12 desktop:min-w-[1000px] tablet:w-full h-fit">
      {/* <Card /> */}
      {/* <ApplyModal></ApplyModal> */}
      <DetailButtons></DetailButtons>
    </div>
  );
};
export default Cards;

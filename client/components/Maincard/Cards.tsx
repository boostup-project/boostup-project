import Card from "components/Maincard/Card";

const Cards = ({ data }: any) => {
  return (
    <div className="mt-6 flex flex-row justify-center flex-wrap w-full desktop:w-9/12 desktop:min-w-[1000px] tablet:w-full min-h-[300px] h-fit">
      <Card data={data} />
    </div>
  );
};
export default Cards;

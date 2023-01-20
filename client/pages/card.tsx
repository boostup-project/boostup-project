import Card from "components/Card/card";

//Mobile = 375 * 667
const Cards = () => {
  return (
    <div className="mt-3 flex flex-row flex-wrap w-full desktop:w-9/12 desktop:min-w-[1000px] tablet:w-full h-fit">
      <Card></Card>
      <Card></Card>
      <Card></Card>
      <Card></Card>
      <Card></Card>
      <Card></Card>
      <Card></Card>
      <Card></Card>
    </div>
  );
};
export default Cards;

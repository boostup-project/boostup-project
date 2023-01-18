import Card from "components/Card/card";
//https://8a91-182-226-233-7.jp.ngrok.io
//Mobile = 375 * 667
const card = () => {
  return (
    <div className="mt-24 flex flex-row justify-center flex-wrap desktop:w-10/12 tablet:w-full h-fit">
      <Card></Card>
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
export default card;

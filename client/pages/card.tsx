import Card from "components/Card/card";
import { useQuery } from "react-query";
import getMainCard from "apis/card/getMainCard";
import { useState } from "react";

const Cards = () => {
  const [cards, setCards] = useState([]);
  const { isLoading, isError, data, error } = useQuery(
    "cards",
    () => getMainCard(),
    {
      enabled: true,
      onSuccess: data => {
        // 성공시 호출
        console.log(data.data.data);
        setCards(data.data.data);
      },
      onError: error => {
        console.log(error);
      },
      retry: 2,
    },
  );

  return (
    <div className="mt-3 flex flex-row flex-wrap w-full desktop:w-9/12 desktop:min-w-[1000px] tablet:w-full h-fit">
      <Card />
      <Card />
      <Card />
      <Card />
      <Card />
      <Card />
      <Card />
    </div>
  );
};
export default Cards;

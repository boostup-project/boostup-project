import { useQuery } from "@tanstack/react-query";
import getMainCard from "apis/card/getMainCard";

export async function getStaticProps() {
  const posts = await getMainCard();
  return { props: { posts } };
}

const useGetMainCard = (props: any) => {
  return useQuery(["cards"], () => getMainCard(), {
    initialData: props.posts,
    enabled: true,
    onSuccess: (data: any) => {
      console.log(data);
    },
    onError: error => {},
    retry: 1,
  });
};

export default useGetMainCard;

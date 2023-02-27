import { useQuery } from "@tanstack/react-query";
import getMainCard from "apis/card/getMainCard";

export async function getStaticProps() {
  const posts = await getMainCard();
  console.log(posts);
  return { props: { posts } };
}

const useGetMainCard = (props: any) => {
  return useQuery(["cards"], () => getMainCard(), {
    initialData: props.posts,
    enabled: true,
    onSuccess: (data: any) => {},
    onError: error => {},
    retry: 1,
  });
};

export default useGetMainCard;

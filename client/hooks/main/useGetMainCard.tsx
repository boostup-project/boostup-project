import { useQuery } from "react-query";
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
      // 성공시 호출
      console.log("success");
    },
    onError: error => {
      console.log(error);
    },
    retry: 1,
  });
};

export default useGetMainCard;

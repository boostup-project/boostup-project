// import { useQuery } from "react-query";
// import getMainCard from "apis/card/getMainCard";

// const useGetMainCard = () => {
//   return useQuery(["cards"], () => getMainCard(), {
//     enabled: true,
//     onSuccess: (data: any) => {
//       // 성공시 호출
//       console.log("success");
//     },
//     onError: error => {
//       console.log(error);
//     },
//     retry: 1,
//   });
// };

// export default useGetMainCard;

import getMainCard from "apis/card/getMainCard";
import { dehydrate, QueryClient, useQuery } from "react-query";

export async function getStaticProps() {
  const queryClient = new QueryClient();

  await queryClient.prefetchQuery("cards", () => getMainCard());

  return {
    props: {
      dehydratedState: dehydrate(queryClient),
    },
  };
}
function useGetMainCard() {
  const { data } = useQuery("cards", () => getMainCard());
}

import getReceipt from "apis/shop/getReceipt";
import { useQuery } from "react-query";

const useGetReceipt = (suggestId: number) => {
  return useQuery(["get/Receipt"], () => getReceipt(suggestId), {
    enabled: false,
    cacheTime: 5 * 10000,
    staleTime: 5 * 10000,
    onSuccess: res => {
      console.log("receipt loaded");
    },
    onError: res => {
      console.log("receipt loaded failed");
    },
  });
};
export default useGetReceipt;

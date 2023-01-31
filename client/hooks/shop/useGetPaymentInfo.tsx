import getPaymentInfo from "apis/shop/getPaymentInfo";
import { useQuery } from "react-query";

const useGetPaymentInfo = (suggestId: number) => {
  return useQuery(["get/PaymentInfo"], () => getPaymentInfo(suggestId), {
    enabled: false,
    cacheTime: 5 * 10000,
    staleTime: 5 * 10000,
    onSuccess: res => {
      console.log("payment info loaded success");
    },
    onError: res => {
      console.log("payment info loaded failed");
    },
  });
};

export default useGetPaymentInfo;

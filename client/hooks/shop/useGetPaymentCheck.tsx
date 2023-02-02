import getPaymentCheck from "apis/shop/getPaymentCheck";
import { useQuery } from "@tanstack/react-query";

const useGetPaymentCheck = (suggestId: number) => {
  return useQuery(["get/PaymentCheck"], () => getPaymentCheck(suggestId), {
    enabled: false,
    cacheTime: 5 * 10000,
    staleTime: 5 * 10000,
    onSuccess: res => {
      console.log("payment status get!");
    },
    onError: res => {
      console.log("payment status cannot get");
    },
  });
};

export default useGetPaymentCheck;

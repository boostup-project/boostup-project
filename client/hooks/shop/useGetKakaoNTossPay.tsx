import { useQuery } from "@tanstack/react-query";
import getKakaoNTossPay from "apis/shop/getKakaoNTossPay";

import { toast } from "react-toastify";

interface Assemble {
  suggestId: number;
  paymentId: string;
}

const useGetKakaoNTossPay = (assemble: Assemble) => {
  return useQuery(["get/KakaoNTossPay"], () => getKakaoNTossPay(assemble), {
    enabled: false,
    cacheTime: 5 * 10000,
    staleTime: 5 * 10000,
    onSuccess: res => {
      console.log("pay url loaded");
    },
    onError: (res: any) => {
      console.log("pay url failed");
      toast.error(res.response.data.message, {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};
export default useGetKakaoNTossPay;

import getKakaoNTossPay from "apis/shop/getKakaoNTossPay";
import { useQuery } from "react-query";
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
    onError: res => {
      console.log("pay url failed", res);
      toast.error("결제정보가 제대로 전달되지 않았습니다. 다시 눌러주세요", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    },
  });
};
export default useGetKakaoNTossPay;

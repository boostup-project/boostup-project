import instance from "apis/module";

export interface Assemble {
  suggestId: number;
  paymentId: string;
}

const getKakaoNTossPay = async (aseemble: Assemble) => {
  const { suggestId, paymentId } = aseemble;
  const kakaoUrl = `/suggest/${suggestId}/kakao/payment`;
  const tossUrl = `/suggest/${suggestId}/toss/payment/${paymentId}`;

  if (paymentId === "0") {
    return await instance.get(kakaoUrl, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  } else {
    return await instance.get(tossUrl, {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  }
};
export default getKakaoNTossPay;

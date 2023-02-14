import instance from "apis/module";

const getPaymentInfo = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/payment/info`;
  return await instance.get(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getPaymentInfo;

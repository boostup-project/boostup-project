import instance from "apis/module";

const getPaymentCheck = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/payment/check`;

  return await instance.get(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getPaymentCheck;

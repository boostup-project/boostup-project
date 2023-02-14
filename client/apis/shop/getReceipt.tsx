import instance from "apis/module";

const getReceipt = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/receipt`;
  return instance.get(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getReceipt;

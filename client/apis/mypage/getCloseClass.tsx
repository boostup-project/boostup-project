import instance from "apis/module";

const getCloseClass = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/done`;
  return await instance.get(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getCloseClass;

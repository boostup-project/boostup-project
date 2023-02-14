import instance from "apis/module";

const deleteApply = async (suggestId: number) => {
  return await instance.delete(`/suggest/${suggestId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteApply;

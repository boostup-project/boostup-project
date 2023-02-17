//과외 종료 삭제(학생용)
import instance from "apis/module";

const deleteFinishedClass = async (suggestId: number) => {
  return await instance.delete(`/suggest/${suggestId}/student`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteFinishedClass;

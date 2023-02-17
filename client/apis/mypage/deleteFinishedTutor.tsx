//과외 종료 삭제(강사용)
import instance from "apis/module";

const deleteFinishedTutor = async (suggestId: number) => {
  return await instance.delete(`/suggest/${suggestId}/tutor`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteFinishedTutor;

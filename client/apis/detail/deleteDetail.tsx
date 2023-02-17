import instance from "apis/module";

const deleteDetail = async (lessonId: number) => {
  return await instance.delete(`/lesson/${lessonId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteDetail;

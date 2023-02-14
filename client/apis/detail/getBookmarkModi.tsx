import instance from "apis/module";

const getBookmarkModi = async (lessonId: Number) => {
  return await instance.get(`/bookmark/lesson/${lessonId}/modification`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default getBookmarkModi;

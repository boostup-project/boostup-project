import instance from "apis/module";

const getBookmark = async (lessonId: number) => {
  return await instance.get(`/bookmark/lesson/${lessonId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default getBookmark;

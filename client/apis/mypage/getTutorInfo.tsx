import instance from "apis/module";

const getTutorInfo = async (lessonId: number, tabId: number) => {
  const url = `/suggest/lesson/${lessonId}/tutor/tab/${tabId}`;
  return await instance.get(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getTutorInfo;

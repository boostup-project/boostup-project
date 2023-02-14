import instance from "apis/module";

const getBasicInfo = async (lessonId: number) => {
  return await instance.get(`/lesson/${lessonId}`, {
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `${
        localStorage.getItem("token")
          ? `Bearer ${localStorage.getItem("token")}`
          : null
      }`,
    },
  });
};

export default getBasicInfo;

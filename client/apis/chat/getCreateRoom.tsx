import instance from "apis/module";

const getCreateRoom = (lessonId: number) => {
  return instance.get(`chat/room/create/lesson/${lessonId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getCreateRoom;

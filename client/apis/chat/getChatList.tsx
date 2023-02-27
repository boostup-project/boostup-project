import instance from "apis/module";

const getChatList = (roomId: number) => {
  return instance.get(`/chat/room/${roomId}/messages`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getChatList;

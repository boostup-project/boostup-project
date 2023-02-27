import instance from "apis/module";

const getChatRoomList = () => {
  return instance.get("/chat/room", {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getChatRoomList;

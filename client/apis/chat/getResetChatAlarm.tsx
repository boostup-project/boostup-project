import instance from "apis/module";

const getResetChatAlarm = (roomId: number) => {
  return instance.get(`/alarm/room/${roomId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getResetChatAlarm;

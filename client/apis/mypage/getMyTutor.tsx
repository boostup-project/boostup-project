import instance from "apis/module";

const getMyTutor = async () => {
  const url = `/lesson/tutor`;
  return await instance.get(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getMyTutor;

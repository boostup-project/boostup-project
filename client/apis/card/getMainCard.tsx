import instance from "apis/module";

const getMainCard = async () => {
  const author = localStorage.getItem("token");

  if (author) {
    return await instance.get(`/lesson`, {
      headers: {
        "content-Type": `application/json`,
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  } else {
    return await instance.get(`/lesson`, {
      headers: {
        "content-Type": `application/json`,
      },
    });
  }
};
export default getMainCard;

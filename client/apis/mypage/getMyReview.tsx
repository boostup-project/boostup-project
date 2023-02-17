import instance from "apis/module";

const getMyReview = async () => {
  const url = `/review`;
  return await instance.get(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getMyReview;

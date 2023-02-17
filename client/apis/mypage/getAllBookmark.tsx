import instance from "apis/module";

const getAllBookmark = async () => {
  return await instance.get(`/bookmark`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default getAllBookmark;

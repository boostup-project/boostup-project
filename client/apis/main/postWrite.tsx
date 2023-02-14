import instance from "apis/module";

const postWrite = async (object: FormData) => {
  const url = "/lesson/registration";

  return await instance.post(url, object, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postWrite;

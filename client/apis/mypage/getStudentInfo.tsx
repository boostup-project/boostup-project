import instance from "apis/module";

const getStudentInfo = async () => {
  const url = `/suggest/student`;
  return await instance.get(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getStudentInfo;

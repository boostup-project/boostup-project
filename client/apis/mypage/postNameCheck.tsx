import instance from "apis/module";

const postNameCheck = async (name: string) => {
  const url = "/member/name/overlap/check";
  return await instance.post(url, name, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postNameCheck;

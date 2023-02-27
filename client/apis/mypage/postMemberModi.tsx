import instance from "apis/module";

const postMemberModi = async (object: FormData) => {
  const url = "/member/modification";
  return await instance.post(url, object, {
    headers: {
      "content-Type": `multipart/form-data`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postMemberModi;

import instance from "apis/module";

const postPwdChange = async (changePassword: string) => {
  const url = "/member/password/resetting/my-page";
  return await instance.post(url, changePassword, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postPwdChange;

import instance from "apis/module";

const postPwdCheck = async (password: string) => {
  const url = "/member/password/check";
  return await instance.post(url, password, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postPwdCheck;

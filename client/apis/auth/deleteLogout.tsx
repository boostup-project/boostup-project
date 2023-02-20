import instance from "apis/module";

export const deleteLogout = async () => {
  const url = "/member/logout";

  return await instance.delete(url, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default deleteLogout;

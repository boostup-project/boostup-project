import instance from "apis/module";

interface Accept {
  quantity: number;
  suggestId: number;
}

const postAccept = async ({ quantity, suggestId }: Accept) => {
  const body = {
    quantity: quantity,
  };
  return await instance.post(
    `/suggest/${suggestId}/accept
  `,
    body,
    {
      headers: {
        "content-Type": `application/json`,
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    },
  );
};
export default postAccept;

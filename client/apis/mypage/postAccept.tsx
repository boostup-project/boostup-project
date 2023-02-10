import axios from "axios";

interface Accept {
  quantity: number;
  suggestId: number;
}

const postAccept = async ({ quantity, suggestId }: Accept) => {
  const body = {
    quantity: quantity,
  };
  return await axios.post(
    `/suggest/${suggestId}/accept
  `,
    body,
    {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "content-Type": `application/json`,

        Authorization: `Bearer ${localStorage.getItem("token")}`,
        // RefreshToken: localStorage.getItem("refresh_token"),
      },
    },
  );
};
export default postAccept;

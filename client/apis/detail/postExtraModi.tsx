import axios from "axios";

interface Assemble {
  object: FormData;
  id: number;
}

const postExtraModi = async (assemble: Assemble) => {
  const { object, id } = assemble;
  const url = `/lesson/${id}/detailInfo/modification`;
  // const object = "hi";
  return await axios.post(url, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      RefreshToken: localStorage.getItem("refresh_token"),
    },
  });
};

export default postExtraModi;

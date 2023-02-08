import axios from "axios";

interface Assemble {
  object: FormData;
  id: number;
}

const postExtraModi = async (assemble: Assemble) => {
  const { object, id } = assemble;
  const url = `/lesson/${id}/detailInfo/modification`;
  return await axios.post(url, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postExtraModi;

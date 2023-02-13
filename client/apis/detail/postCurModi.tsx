import axios from "axios";

interface Assemble {
  textData: string;
  id: number;
}

const postCurModi = async (assemble: Assemble) => {
  const { id, textData } = assemble;
  const object = {
    curriculum: textData,
  };
  const url = `/lesson/${id}/curriculum/modification`;
  return await axios.patch(url, textData, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postCurModi;

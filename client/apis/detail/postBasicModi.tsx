import axios from "axios";

interface Props {
  formData: FormData;
  lessonId: number;
}

const postBasicModi = ({ formData, lessonId }: Props) => {
  console.log(formData);
  return axios.post(`lesson/${lessonId}/modification`, formData, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postBasicModi;

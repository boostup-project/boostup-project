import instance from "apis/module";

interface Props {
  formData: FormData;
  lessonId: number;
}

const postBasicModi = ({ formData, lessonId }: Props) => {
  console.log(formData);
  return instance.post(`lesson/${lessonId}/modification`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postBasicModi;

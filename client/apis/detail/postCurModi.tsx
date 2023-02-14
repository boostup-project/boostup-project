import instance from "apis/module";

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
  return await instance.patch(url, textData, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postCurModi;

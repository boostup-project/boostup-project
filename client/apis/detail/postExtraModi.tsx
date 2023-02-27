import instance from "apis/module";

interface Assemble {
  object: FormData;
  id: number;
}

const postExtraModi = async (assemble: Assemble) => {
  const { object, id } = assemble;
  const url = `/lesson/${id}/detailInfo/modification`;
  return await instance.post(url, object, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postExtraModi;

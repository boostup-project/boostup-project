import instance from "apis/module";

interface Application {
  days: string;
  languages: string;
  requests?: string | null;
  id: number;
}

const postApply = async ({ days, languages, requests, id }: Application) => {
  const body = {
    days: days,
    languages: languages,
    requests: requests,
  };

  return await instance.post(`/suggest/lesson/${id}`, body, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default postApply;

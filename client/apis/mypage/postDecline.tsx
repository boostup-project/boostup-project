import instance from "apis/module";

interface Decline {
  reason: string;
  suggestId: number;
}

const postDecline = async ({ reason, suggestId }: Decline) => {
  const body = {
    reason: reason,
  };
  return await instance.post(
    `/suggest/${suggestId}/decline
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
export default postDecline;

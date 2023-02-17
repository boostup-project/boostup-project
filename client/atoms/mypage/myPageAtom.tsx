import { atom } from "recoil";

const isMemberEdited = atom({
  key: "isMemberEdited",
  default: false,
});

const reviewIdState = atom<number>({
  key: "reviewIdState",
  default: 0,
});
const totalCard = atom<number>({
  key: "totalCard",
  default: 0,
});

export { isMemberEdited, reviewIdState };

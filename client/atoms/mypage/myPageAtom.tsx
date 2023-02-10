import { atom } from "recoil";

const isMemberEdited = atom({
  key: "isMemberEdited",
  default: false,
});

export { isMemberEdited };

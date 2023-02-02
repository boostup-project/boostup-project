import { atom } from "recoil";

const chatActive = atom({
  key: "chatActive",
  default: false,
});

export { chatActive };

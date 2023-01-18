import { atom } from "recoil";

const inputStep = atom({
  key: "inputStep",
  default: 1,
});

const filterModal = atom({
  // Modal ON : "30"
  // Modal OFF :"32"
  key: "filterModal",
  default: 48,
});

export { inputStep, filterModal };

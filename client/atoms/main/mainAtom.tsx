import { atom } from "recoil";

const inputStep = atom({
  key: "inputStep",
  default: 1,
});

const isWriteModal = atom({
  key: "isWriteModal",
  default: false,
});

const baseSave = atom({
  key: "baseSave",
  default: {},
});

const addSave = atom({
  key: "baseSave",
  default: {},
});

const currSave = atom({
  key: "baseSave",
  default: "",
});

const filterModal = atom({
  // Modal ON : "30"
  // Modal OFF :"32"
  key: "filterModal",
  default: 48,
});

export { inputStep, baseSave, isWriteModal, addSave, currSave, filterModal };

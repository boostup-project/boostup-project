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

export { inputStep, baseSave, isWriteModal, addSave, currSave };

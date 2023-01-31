import { atom } from "recoil";

const powerApplyModal = atom({
  key: "powerApplyModal",
  default: false,
});

const powerBasicEditModal = atom({
  key: "powerBasicEditModal",
  default: false,
});

const editMode = atom({
  key: "editMode",
  default: false,
});

export { powerApplyModal, powerBasicEditModal, editMode };

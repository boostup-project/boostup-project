import { atom } from "recoil";

const powerApplyModal = atom({
  key: "powerApplyModal",
  default: false,
});

const powerBasicEditModal = atom({
  key: "powerBasicEditModal",
  default: false,
});

export { powerApplyModal, powerBasicEditModal };

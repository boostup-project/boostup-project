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

const refetchToggle = atom({
  key: "refetchToggle",
  default: false,
});

const refetchBookmark = atom({
  key: "refetchBookmark",
  default: false,
});

export {
  powerApplyModal,
  powerBasicEditModal,
  editMode,
  refetchToggle,
  refetchBookmark,
};

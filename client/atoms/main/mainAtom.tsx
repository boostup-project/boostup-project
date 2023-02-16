import { atom } from "recoil";

const inputStep = atom({
  key: "inputStep",
  default: 1,
});

const powerWriteModal = atom({
  key: "powerWriteModal",
  default: false,
});
const mainCardInfo = atom<any>({
  key: "mainCardInfo",
  default: undefined,
});

const isWrite = atom({
  key: "isWrite",
  default: false,
});

const filterModal = atom({
  // Modal ON : "30"
  // Modal OFF :"32"
  key: "filterModal",
  default: 48,
});

const powerEditReviewModal = atom({
  key: "powerEditReviewModal",
  default: false,
});

const reviewCommentState = atom({
  key: "reviewCommentState",
  default: "",
});
const totalCard = atom<number>({
  key: "totalCard",
  default: 0,
});

export {
  inputStep,
  powerWriteModal,
  filterModal,
  isWrite,
  mainCardInfo,
  powerEditReviewModal,
  reviewCommentState,
  totalCard,
};

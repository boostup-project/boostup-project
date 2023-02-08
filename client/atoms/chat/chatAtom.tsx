import { atom } from "recoil";

const chatActive = atom({
  key: "chatActive",
  default: false,
});

const chatListState = atom<any>({
  key: "chatListState",
  default: [],
});

const newDataState = atom<any>({
  key: "newDataState",
  default: [],
});

const connectionState = atom({
  key: "connectionState",
  default: false,
});

const roomIdState = atom({
  key: "roomIdState",
  default: 0,
});

export {
  chatActive,
  chatListState,
  connectionState,
  roomIdState,
  newDataState,
};

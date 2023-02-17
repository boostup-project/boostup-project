import { atom } from "recoil";

const chatActive = atom<boolean>({
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

const receiverIdState = atom({
  key: "receiverIdState",
  default: 0,
});

const chatDisplayName = atom({
  key: "chatDisplayName",
  default: "",
});

const chatRoomListState = atom<any>({
  key: "chatRoomListState",
  default: [],
});

const newChatRoomState = atom<any>({
  key: "newChatRoomState",
  default: [],
});

export {
  chatActive,
  chatListState,
  connectionState,
  roomIdState,
  newDataState,
  chatDisplayName,
  receiverIdState,
  chatRoomListState,
  newChatRoomState,
};

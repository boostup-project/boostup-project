import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

const findPwEmail = atom({
  key: "findPwEmail",
  default: "",
});

const signUpErrorMessage = atom<any>({
  key: "signUpErrorMessage",
  default: "",
});

const loginErrorMessage = atom<any>({
  key: "loginErrorMessage",
  default: "",
});

const resetPwStep = atom<Number>({
  key: "resetPwStep",
  default: 1,
});

const logUser = atom({
  key: "logUser",
  default: false,
  effects_UNSTABLE: [persistAtom],
});

export {
  findPwEmail,
  signUpErrorMessage,
  loginErrorMessage,
  resetPwStep,
  logUser,
};

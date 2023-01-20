import { atom } from "recoil";

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
export { findPwEmail, signUpErrorMessage, loginErrorMessage, resetPwStep };

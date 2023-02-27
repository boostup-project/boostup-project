import axios from "axios";

const instance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
});

instance.interceptors.response.use(config => {
  if (config.headers.authorization) {
    localStorage.setItem("token", config.headers.authorization);
    console.log("response 작동");
  }
  return config;
});

export default instance;

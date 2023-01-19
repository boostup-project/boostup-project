import axios, { AxiosResponse } from "axios";

const postWrite = async (object: FormData) => {
  const url = "/lesson/registration";
  const baseURL = "https://88f0-182-226-233-7.jp.ngrok.io";
  const Authorization =
    "Baerer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlkIjoxMSwiZW1haWwiOiJtYW1lMUBnbWFpbC5jb20iLCJzdWIiOiJtYW1lMUBnbWFpbC5jb20iLCJpYXQiOjE2NzQxNDA5MTIsImV4cCI6MTY3NDE0MjcxMn0.jp1uw8Tg-T3HC5FkjgfLY2q4tc0WscsrOWTUJfdvy84xFhgNOQGzehZDG5uPM590TjmInah9skGEiMYwEcYz2A";
  const RefreshToken =
    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYW1lMUBnbWFpbC5jb20iLCJpYXQiOjE2NzQxNDA5MTIsImV4cCI6MTY3NDIyNzMxMn0.m0wuHXhRtF0SepkQe17HNHc43T4Qj4BL-3dkqsBmYsIWM4rQGkJvunVL4PBcAT0wuPvL8ufWtwMEScAhAm7j1g";

  await axios
    .post(url, object, {
      baseURL,
      headers: {
        "ngrok-skip-browser-warning": "63490",
        "Content-Type": "multipart/form-data",
        Authorization,
        RefreshToken,
      },
    })
    .then(response => {
      console.log(response);
    })
    .catch(err => {
      console.log(err);
    });
};

export default postWrite;

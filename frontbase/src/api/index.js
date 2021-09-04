import axios from "axios";

function createInstance() {
  const instance = axios.create({
    // baseURL: "http://localhost:8080",
    baseURL: "http://i5c105.p.ssafy.io/api",

    headers: {
      "Content-Type": "application/json"
    },
  });
  return instance;
}

export { createInstance };

import axios from "axios";
import store from "@/store/index";

//let url = "http://localhost:8080";
let url= "http://i5c105.p.ssafy.io/api";
function createInstance() {
  const instance = axios.create({
    baseURL: url,
    
    headers: {
      "Content-Type": "application/json",
      "access-token":store.state.token,
    }
  });
  return instance;
}

export { createInstance, url};

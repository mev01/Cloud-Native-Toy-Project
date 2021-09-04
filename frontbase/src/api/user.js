import { createInstance } from "./teamindex.js";

const instance = createInstance();
// const config = {
//   headers: { "access-token": localStorage.getItem("access-token") }
// };

function login(member, success, fail) {
  instance.defaults.headers["access-token"] = window.localStorage.getItem(
    "access-token"
  );
  
  instance
    .post(`/member/login`, {
      email: member.email,
      password:member.password
    })
    .then(success)
    .catch(fail);
}

function join(member, success, fail) {
  const body = {
    userid: member.userid,
    userpwd: member.userpwd,
    username: member.username,
    email: member.email,
    address: member.address,
  };

  instance
    .post("/user/confirm/join", JSON.stringify(body))
    .then(success)
    .catch(fail);
}


async function findById(memberEmail, success, fail) {
  instance.defaults.headers["access-token"] = window.localStorage.getItem(
    "access-token"
  );
  await instance
    .get(`/member/info/${memberEmail}`)
    .then(success)
    .catch(fail);
}

export { login, findById };

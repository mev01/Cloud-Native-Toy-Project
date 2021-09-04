<template>
  <div>
    <v-container row justify-center align-center wrap class="mt-4 pt-3">
      <form>

          <v-text-field
            type="email"
            color="green"
            background-color="transparent"
            name="member.email"
            v-model="member.email"
            label="E-mail"
          ></v-text-field>

          <v-text-field
            :type="'password'"
            name="member.password"
            color="green"
            background-color="transparent"
            v-model="member.password"
            label="비밀번호"
          ></v-text-field>

          
      </form>
      <v-btn @click="confirm" type="submit" color="green" class="white--text">로그인</v-btn>
    </v-container> 
  </div>
</template>

<script>
import { login } from "@/api/user.js";
import { mapState } from "vuex";

let callbackFunction = (status) => {
    if (status) {
    /* (5) 필수적으로 받아야하는 프로필 정보가 있다면 callback처리 시점에 체크 */
    var email = naverLogin.user.getEmail();
    if( email == undefined || email == null) {
      alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
      /* (5-1) 사용자 정보 재동의를 위하여 다시 네아로 동의페이지로 이동함 */
      naverLogin.reprompt();
      return;
    }

    window.location.replace("http://" + window.location.hostname + ( (location.port==""||location.port==undefined)?"":":" + location.port) + "/sample/main.html");
  } else {
    console.log("callback 처리에 실패하였습니다.");
  }
}

export default {
  name: "login",
  components: {
    
  },
  data() {
    return {
      member: {
        email: "",
        password: ""
      },
      isLoginError: false,
      client_id: "916d7a1087ccb6494372f576d3911baf",
      redirect_uri: "http://localhost:8080",
    };
  },
  methods: {
    confirm() {
      localStorage.setItem("access-token", "");
      console.log(this.member.email);
      console.log(this.member.password);
      login(
        this.member,
        (response) => {
          if (response.data.message === "success") {
            let token = response.data["access-token"];
            this.$store.commit("setIsLogined", true);
            localStorage.setItem("access-token", token);
            console.log(token);
            this.$store.dispatch("GET_MEMBER_INFO", token);
            this.$router.push("/");
          } else {
            this.isLoginError = true;
          }
        },
        (error) => {
          console.error(error);
          alert("에러입니다.");
        }
      );
      
    },
    onSuccess(){
      this.$store.commit("setMemberInfo", true);
      this.$router.push("/");
    },
    onFailure(){
      this.$router.push("/");
    },
    callbackFunction
  },
  computed: {
    kakaoLoginLink() {
      
    },
    ...mapState(["memberInfo","isLogin"]),
  },


};
</script>

<style scoped>

#app img {
  width: 200px;
}

</style>

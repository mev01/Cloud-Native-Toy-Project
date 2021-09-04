<template>
  <v-layout column justify-center align-center class="mt-4 pt-2">
    <VueCompareImage
      class="hidden-md-and-down"
      hover
      :style="{ minWidth: '1300px' }"
      :sliderLineWidth="sliderLine"
      :handleSize="hSize"
      :leftImage="leftImage"
      :rightImage="rightImage"
      :sliderPositionPercentage="sliderPosition"
    />
    <VueCompareImage
      class="hidden-lg-and-up"
      hover
      :style="{ maxWidth: '400px' }"
      :sliderLineWidth="sliderLine"
      :handleSize="hSize"
      :leftImage="leftImage2"
      :rightImage="rightImage2"
      :sliderPositionPercentage="sliderPosition"
    />

    <br />

    <vue-typer class="headline" :repeat="0" text="혼자 뭐하니?"></vue-typer>
    <vue-typer
      :text="text1"
      :repeat="Infinity"
      :shuffle="false"
      initial-action="erasing"
      :pre-type-delay="70"
      :type-delay="70"
      :pre-erase-delay="1980"
      :erase-delay="150"
      erase-style="select-back"
      :erase-on-complete="false"
      caret-animation="smooth"
    ></vue-typer>
    <br />
    <v-flex xs12 sm6 md4 lg4 xl4 v-if="memberInfo === null">
      <v-card flat color="transparent" max-width="500">
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
            @keydown.enter="confirm"
          ></v-text-field>
        </form>

        <v-card-actions class=" justify-center">
          <v-btn
            @click="confirm"
            type="submit"
            color="green"
            class="white--text"
            >로그인</v-btn
          >
          <v-btn
            flat
            to="/signup"
            type="submit"
            color="green"
            class="white--text"
            >회원가입</v-btn
          >
        </v-card-actions>
        <v-card hover>
          <img
            src="//k.kakaocdn.net/14/dn/btqCn0WEmI3/nijroPfbpCa4at5EIsjyf0/o.jpg"
            width="250"
            height="50"
            hover
            @click="loginWithKakao"
          />
        </v-card>

        <GoogleLogin :params="params" :renderParams="renderParams" :onSuccess="onSuccess" :onFailure="onFailure"></GoogleLogin>

        <v-card hover>
          <img 
            height="50" 
            @click="naverLogin" 
            src='@/assets/images/naverid.png'
          />
        </v-card>
        

        <v-card-actions class="hidden-md-and-up justify-center">
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import { VueTyper } from "vue-typer";
import { login } from "@/api/user.js";
import { mapState } from "vuex";
import { mapGetters } from "vuex";
import VueCompareImage from "vue-compare-image";
import banner1 from "@/assets/images/banner1.jpg";
import banner2 from "@/assets/images/banner2.jpg";
import banner3 from "@/assets/images/banner3.jpg";
import banner4 from "@/assets/images/banner4.jpg";
import GoogleLogin from 'vue-google-login';
import naverid from "@/assets/images/naverid.png";
import { createInstance, url } from "@/api/teamindex.js";
export default {
  components: {
    "vue-typer": VueTyper,
    VueCompareImage,
    GoogleLogin,
    naverid,
  },
  computed: {
    ...mapGetters(["memberInfo"])
  },
  created(){
    this.naverimgsrc=naverid
  },
  
  data() {
    return {
      naverimgsrc:"",
      member: {
        email: "",
        password: ""
      },
      isLoginError: false,
      client_id: "916d7a1087ccb6494372f576d3911baf",
      redirect_uri: url,
      icons: [
        { href: "https://github.com/EldinZaimovic", icon: "fab fa-github" },
        {
          href: "https://stackoverflow.com/users/story/9660700",
          icon: "fab fa-stack-overflow"
        },
        { href: "https://twitter.com/EldinMatricar", icon: "fab fa-twitter" },
        {
          href: "https://www.linkedin.com/in/eldin-zaimovic",
          icon: "fab fa-linkedin-in"
        },
        {
          href: "https://www.xing.com/profile/Eldin_Zaimovic?sc_o=mxb_p",
          icon: "fab fa-xing"
        },
        {
          href: "https://www.facebook.com/EldinVasVoli",
          icon: "fab fa-facebook-f"
        },
        { href: "https://www.instagram.com/matricar", icon: "fab fa-instagram" }
      ],
      text1: ["혼자서 하는 쓸쓸한 운동", "다함께 즐기는 재미난 운동"],
      leftImage: banner1,
      rightImage: banner2,
      leftImage2: banner3,
      rightImage2: banner4,
      sliderLine: 0,
      hSize: 0,
      sliderPosition: 0.5,
      params: {
        client_id: "1011124741177-02ief3em5ve5bj13s93nvvq33dd7et9v.apps.googleusercontent.com",
        ux_mode: "popup",
        redirect_uri: "http://localhost:8081/",
      },
      renderParams: {
          width: 250,
          height: 50,
          longtitle: true
      }
    };
  },
  methods: {
    confirm() {
      localStorage.setItem("access-token", "");
      console.log(this.member.email);
      console.log(this.member.password);
      login(
        this.member,
        response => {
          if (response.data.message === "success") {
            let token = response.data["access-token"];
            this.$store.commit("setIsLogined", true);
            localStorage.setItem("access-token", token);
            console.log(token);
            this.$store.dispatch("GET_MEMBER_INFO", token);
            this.$router.push("/teamlist");
          } else {
            this.isLoginError = true;
          }
        },
        error => {
          console.error(error);
          alert("이메일이나 비밀번호를 잘못 입력하셨습니다.");
        }
      );
    },
    onSuccess(googleUser) {
      const instance = createInstance();
      instance
        .get("/member/google?email=" + googleUser.getBasicProfile().Et)
        .then(response => {
          console.log(response);
          if(response.data.data == null){
            this.$router.push("/kakaosignup?email=" + googleUser.getBasicProfile().Et);
          }
          else{
            this.member.email = response.data.data.email;
            this.member.password = response.data.data.password;

            this.confirm();
          }
        })
    },
    onFailure(){

    },
    
    loginWithKakao(){
      window.location.replace(
        `https://kauth.kakao.com/oauth/authorize?client_id=35246c4d76c9d177b219aeeb8d0f2579&redirect_uri=http://i5c105.p.ssafy.io/kakaosignup&response_type=code`
      );
    },
    naverLogin(){
      const instance = createInstance();
      instance.get("/member/naverlogin")
          .then(response => {
            console.log(response.data);
            window.location.href=response.data;
              
          }).catch(error =>{
            console.log(error);
      })
    }
  },
  computed: {
    kakaoLoginLink() {},
    ...mapState(["memberInfo", "isLogin"])
  }
};
</script>
<style>
@keyframes rocking {
  0%,
  100% {
    transform: rotateZ(-10deg);
  }

  50% {
    transform: rotateZ(10deg);
  }
}

.vue-typer {
  font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
}
.vue-typer .custom.char.typed {
  color: green;
}
.vue-typer .custom.char.selected {
  color: #e91e63;
}

.vue-typer .custom.caret {
  animation: rocking 1s ease-in-out 0s infinite;
}
.vue-typer .custom.caret.typing {
  background-color: green;
}
.vue-typer .custom.caret.selecting {
  display: inline-block;
  background-color: #e91e63;
}
</style>

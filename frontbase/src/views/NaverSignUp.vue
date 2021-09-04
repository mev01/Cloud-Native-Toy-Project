<template>
  <v-container grid-list-xl>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2"  v-if="checkingSocial">
      <v-flex xs12 sm12 md6 lg6 xl6>
        <h2 class="pb-4 mb-4">
          <span>NaverSign</span>
          <span class="blue--text">Up</span>
        </h2>

        <form>
          <v-text-field
            name="member.zonecode"
            color="green"
            background-color="transparent"
            v-model="member.zonecode"
            label="우편번호"
            @click="showApi"
          ></v-text-field>

          <v-text-field
            name="member.address"
            color="green"
            background-color="transparent"
            v-model="member.address"
            label="주소"
          ></v-text-field>

          <v-text-field
            name="member.addressDetail"
            color="green"
            background-color="transparent"
            v-model="member.addressDetail"
            label="상세주소"
          ></v-text-field>

          <v-text-field
            name="member.phone"
            color="green"
            background-color="transparent"
            v-model="member.phone"
            label="전화번호"
          ></v-text-field>

          <v-select
            v-model="sex"
            :items="sexList"
            label="성별"
            item-text="name"
            item-value="value"
            return-object
          ></v-select>

          <v-select
            v-model="mbti"
            :items="mbtiList"
            label="MBTI"
            item-text="name"
            item-value="value"
            return-object
          ></v-select>

        </form>
          <v-btn @click="submit" type="submit" color="green" class="white--text"
            >회원가입</v-btn>
            
          <v-btn @click="clear">clear</v-btn>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { validationMixin } from "vuelidate";
import { createInstance } from "@/api/teamindex.js";
import { login } from "@/api/user.js";
import axios from "axios";

import {
  required,
  maxLength,
  email,
  minLength,
} from "vuelidate/lib/validators";
export default {
  mixins: [validationMixin],
  validations: {
    name: { required, maxLength: maxLength(20) },
    email: { required, email },
    body: { required, minLength: minLength(20) },
  },
  data() {
    return {
      num:"",
      authenticFlag:false,
      passwordConfirm:"",
      sexList: [
        { name: "남자", value: "M" },
        { name: "여자", value: "W" },
      ],
      mbtiList: [
        { name: "istj", value: "istj" },
        { name: "isfj", value: "isfj" },
        { name: "infj", value: "infj" },
        { name: "intj", value: "intj" },
        { name: "istp", value: "istp" },
        { name: "isfp", value: "isfp" },
        { name: "infp", value: "infp" },
        { name: "intp", value: "intp" },
        { name: "estp", value: "estp" },
        { name: "esfp", value: "esfp" },
        { name: "entp", value: "entp" },
        { name: "estj", value: "estj" },
        { name: "esfj", value: "esfj" },
        { name: "enfj", value: "enfj" },
        { name: "entj", value: "entj" },
      ],
      name: "",
      email: "",
      body: "",
      sex: "",
      mbti: "",
      member: {
        name: "",
        email: "",
        phone: "",
        password: "",
        address: "",
        addressDetail: "",
        zonecode: "",
        sex: "",
        point: 0,
        mbti: "",
        createdate: "",
        authentication: "",
      },
      error: {
        passwordConfirm: false,
      },
      checkingSocial :false,
    };
  },
  created() {
      console.log(this.$route.query.code);
      this.getNaverMemberInfo();
  },
  watch: {
    passwordConfirm: function(v){
      this.checkForm();
    }
  },
  methods: {
  
    getNaverMemberInfo(){
        const instance = createInstance();
        instance
            .get("/member/navercallback?code=" + this.$route.query.code
            +"&state=" +this.$route.query.state)
            .then(response => {
              if(response.data.message=="nocreate"){
                let token = response.data["access-token"];
                this.$store.commit("setIsLogined", true);
                localStorage.setItem("access-token", token);

                this.$store.dispatch("GET_MEMBER_INFO", token);
                this.$router.push("/teamlist");
              }else if(response.data.message=="needcreate"){
                alert("추가정보를 입력하셔야합니다.");
                this.checkingSocial=true;
                this.member.email = response.data.email;
                this.member.name = response.data.name;
              }
            })
    },
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
            this.$store.dispatch("GET_MEMBER_INFO", token);
            this.$router.push("/teamlist");
          } else {
            this.isLoginError = true;
          }
        },
        error => {
          console.error(error);
          alert("에러입니다.");
        }
      );
    },
    submit() {
      this.member.sex = this.sex.value;
      this.member.mbti = this.mbti.value;
      this.member.password = "qwer1234";
      this.passwordConfirm = "qwer1234";
      const instance = createInstance();
      instance.post("/member/signup", JSON.stringify(this.member))
        .then(
          (response) => {
            console.log(response);
            if (response.data.message === "success") {
              alert("회원가입 완료 가입된 아이디로 로그인 해주세요.");
              this.$router.push("/");
            } else {
              alert("회원가입 실패");
            }
          }
        )
        .catch(() => {
          alert("에러발생!");
          this.$router.push("/");
        });
    },
    
    clear() {
      this.$v.$reset();
      this.member.address="";
      this.member.addressDetail="";
      this.member.zonecode="";
    },
    showApi() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          let fullRoadAddr = data.roadAddress;
          let extraRoadAddr = "";
          if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname;
          }
          if (data.buildingName !== "" && data.apartment === "Y") {
            extraRoadAddr +=
              extraRoadAddr !== ""
                ? ", " + data.buildingName
                : data.buildingName;
          }
          if (extraRoadAddr !== "") {
            extraRoadAddr = " (" + extraRoadAddr + ")";
          }
          if (fullRoadAddr !== "") {
            fullRoadAddr += extraRoadAddr;
          }
          this.member.zonecode = data.zonecode;
          this.member.address = fullRoadAddr;
        },
      }).open();
    },
    checkForm() {
      if (this.member.password !== this.passwordConfirm)
        this.error.passwordConfirm = "비밀번호가 다릅니다.";
      else this.error.passwordConfirm = false;


      let isSubmit = true;
      Object.values(this.error).map(v => {
        if (v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    check(){
      console.log(this.authenticFlag);
    }
  },
  computed: {
    nameErrors() {
      const errors = [];
      if (!this.$v.name.$dirty) return errors;
      !this.$v.name.maxLength &&
        errors.push("이름은 20글자 이내로 작성하셔야합니다.");
      !this.$v.name.required && errors.push("이름을 적어주세요.");
      return errors;
    },
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.email && errors.push("이메일 형식이 맞지 않습니다.");
      !this.$v.email.required && errors.push("이메일을 적어주세요");
      return errors;
    },
    
  },
};
</script>

<style lang="scss" scoped>
</style>

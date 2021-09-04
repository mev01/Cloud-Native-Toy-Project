<template>
  <v-container grid-list-xl>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <v-flex xs12 sm12 md6 lg6 xl6>
        <h2 class="pb-4 mb-4">
          <span>Sign</span>
          <span class="green--text">Up</span>
        </h2>
        
        <form>
          <v-text-field
            name="member.name"
            color="green"
            background-color="transparent"
            v-model="member.name"
            :error-messages="nameErrors"
            label="이름"
            
            required
          ></v-text-field>
          
          <v-layout row wrap justify-center align-center>
            <v-text-field id ="nowrap-overflow"
              type="email"
              color="green"
              background-color="transparent"
              name="member.email"
              v-model="member.email"
              :error-messages="emailErrors"
              label="E-mail"
            ></v-text-field>
          <v-btn @click="authentic()" color="green" class="white--text">인증하기</v-btn>
          <div class="timer" v-if="resetButton">
            <span class="minute">{{ minutes }}</span>
            <span>:</span>
            <span class="seconds">{{ seconds }}</span>
          </div>
          </v-layout>

          <v-layout row wrap>
            <v-text-field
              name="num"
              color="green"
              background-color="transparent"
              v-model="num"
              label="인증번호"
            ></v-text-field>
            <v-btn @click="certify()" elevation="0" color="green" class="white--text">확인하기</v-btn>
          </v-layout>

          <v-text-field
            name="member.phone"
            color="green"
            background-color="transparent"
            v-model="member.phone"
            label="전화번호"
          ></v-text-field>

          <v-text-field
            :type="'password'"
            name="member.password"
            color="green"
            background-color="transparent"
            v-model="member.password"
            label="비밀번호"
          ></v-text-field>

          <v-text-field
            :type="'password'"
            name="passwordConfirm"
            color="green"
            background-color="transparent"
            v-model="passwordConfirm"
            label="비밀번호확인"
          ></v-text-field>
          <div style="color:red" v-if="error.passwordConfirm">{{error.passwordConfirm}}</div>
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
import Countdown from 'vuejs-countdown';

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
  components: { 
    Countdown,
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
      resTimeData : '',

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
        { name: "enfp", value: "enfp" },
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
      timer: null,
      totalTime: (5 * 60),
      resetButton: false,
      title: "Countdown to rest time!",
      edit: false
    };
  },
  watch: {
    passwordConfirm: function(v){
      this.checkForm();
    }
  },
  methods: {
    loadf() {
      var file = document.getElementById("chooseFile");

      let preview = document.querySelector(".preview");
      preview.src = URL.createObjectURL(file.files[0]);

      // console.log(file.files[0]);

      preview.style.width = "60%";
      preview.style.height = "60%";
      preview.style.maxHeight = "500px";
    },
    submit() {
      if(!this.authenticFlag){
        alert("인증먼저해주세요");
        return;
      }
      this.member.sex = this.sex.value;
      this.member.mbti = this.mbti.value;
      const instance = createInstance();
      instance.post("/member/signup", JSON.stringify(this.member))
        .then(
          (response) => {
            console.log(response);
            if (response.data.message === "success") {
              alert("회원가입 완료");
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
      this.member.name = "";
      this.member.email = "";
      this.passwordConfirm="";
      this.member.password="";
      this.member.phone="";
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
    authentic(){
      this.startTimer();
      const instance = createInstance();
      instance.post("/email/send?"+"member_email="+this.member.email)
        .then(
          (response) => {
            console.log(response);
            if (response.data.message === "success") {
              alert("인증번호가 전송되었습니다.");
            } else {
              alert("인증번호 전송에 실패하였습니다.");
            }
          }
        )
        .catch(() =>{
          alert("실패하셨습니다.");
        });
    },
    certify(){
      const instance = createInstance();
      instance.get("/email/certified?"+"num="+this.num)
        .then(
          (response) => {
            console.log(response);
            if (response.data.message === "success") {
              this.authenticFlag=true;
              alert("인증완료 되었습니다");
            } else {
              alert("인증 실패하였습니다. 다시 인증해주세요.");
            }
          }
        )
        .catch(() =>{
          alert("실패하셨습니다.");
        });
    },
    check(){
      console.log(this.authenticFlag);
    },
    startTimer: function() {
      this.timer = setInterval(() => this.countdown(), 1000); //1000ms = 1 second
      this.resetButton = true;
    },
    stopTimer: function() {
      clearInterval(this.timer);
      this.timer = null;
      this.resetButton = true;
    },
    resetTimer: function() {
      this.totalTime = (5 * 60);
      clearInterval(this.timer);
      this.timer = null;
      this.resetButton = false;
    },
    editTimer: function() {
      this.edit = true;
    },
    padTime: function(time){
      return (time < 10 ? '0' : '') + time;
    },
    countdown: function() {
      this.totalTime--;
    },
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
    // bodyErrors() {
    //   const errors = [];
    //   if (!this.$v.body.$dirty) return errors;
    //   !this.$v.body.minLength &&
    //     errors.push("Text must be at least 20 characters long");
    //   !this.$v.body.required && errors.push("Text is required");
    //   return errors;
    // },
    minutes: function(){
      const minutes = Math.floor(this.totalTime / 60);
      return this.padTime(minutes);
    },
    seconds: function() {
      const seconds = this.totalTime - (this.minutes * 60);
      return this.padTime(seconds);
    },
  },
};
</script>

<style lang="scss" scoped>
.nowrap-overflow {
    flex-wrap: nowrap;
    overflow-x: auto;
}
.img_wrap {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.preview {
  display: block;
  margin: 20px 0;
}

</style>

<template>
  <v-container grid-list-xl>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <v-flex xs12 sm12 md6 lg6 xl6>
        <h2 class="pb-4 mb-4">
          <span><b>비밀번호</b></span>
          <span class="green--text"><b>변경</b></span>
        </h2>

        <form method="POST">
          <v-text-field
            :type="'password'"
            name="member.password"
            color="green"
            background-color="transparent"
            v-model="member.password"
            label="새로운 비밀번호"
          ></v-text-field>

          <v-text-field
            :type="'password'"
            name="passwordConfirm"
            color="green"
            background-color="transparent"
            v-model="passwordConfirm"
            label="새로운 비밀번호확인"
          ></v-text-field>
          <div style="color:red" v-if="error.passwordConfirm">{{error.passwordConfirm}}</div>

          <v-btn
            type="button"
            color="green" class="white--text"
            @click="modify"
          >비밀번호 변경</v-btn>
          <v-btn @click="clear">초기화</v-btn>
          <v-btn large flat to="/mypage" class="green--text">
          <v-icon>arrow_back</v-icon>뒤로가기
        </v-btn>
        </form>
        
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { createInstance } from "@/api/teamindex.js";
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["memberInfo"]),
  },
  

  created(){
    this.member.name = this.memberInfo.name;
    this.member.email = this.memberInfo.email;
    this.member.phone =this.memberInfo.phone;
    this.member.address = this.memberInfo.address;
    this.member.addressDetail = this.memberInfo.addressDetail;
    this.member.zonecode = this.memberInfo.zonecode;
    this.member.sex = this.memberInfo.sex;
    this.member.point = this.memberInfo.point;
    this.member.mbti = this.memberInfo.mbti;
    this.mbti = this.memberInfo.mbti;
    this.member.createDate =this.memberInfo.createdate;
    this.member.authenticated =this.memberInfo.authenticated;
    this.member.memberId =this.memberInfo.memberId;
  },
  data() {
    return {
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
      passwordConfirm:"",
      member: {
        memberId: 0,
        name: "",
        email: "",
        phone: "",
        password: "",
        address: "",
        addressDetail: null,
        zonecode: "",
        sex: "",
        point: 0,
        mbti: "",
        createDate: null,
        authenticated: "",
      },
      error: {
        passwordConfirm: false,
      },
    };
  },
  watch: {
    passwordConfirm: function(v){
      this.checkForm();
    }
  },
  methods: {
    clear() {
      this.mbti.name = this.memberInfo.mbti;
      this.mbti.value = this.memberInfo.mbti;
      this.member.address = this.memberInfo.address;
      this.member.addressDetail = this.memberInfo.addressDetail;
    },
    modify() {
      const instance = createInstance();
      const param={
        email:this.memberInfo.email,
        password:this.member.password
      }
      console.log(param);
      instance.post("/member/password", JSON.stringify(param))
      .then(
        (response) => {
          if (response.data.message === "success") {
            alert("비밀번호 변경이 완료되었습니다. 새로운 비밀번호로 로그인해주세요.");
            this.$store.commit("setMemberInfo", this.member);
            this.$store.dispatch("LOGOUT")
          } else {
            alert("비밀번호 변경 실패");
          }
        }
      )
      .catch();
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
  },

};
</script>

<style lang="scss" scoped>
</style>

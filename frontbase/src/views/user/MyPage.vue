<template>
  <div>
    <b-container class="mt-4" v-if="memberInfo">
      <b-row>
        <b-col></b-col>
        <b-col cols="8">
        <v-layout align-center data-aos="fade-right">
          <v-toolbar-title class="headline">
              <span><b>My</b></span>
              <span class="green--text"><b>&nbsp;page</b></span>
          </v-toolbar-title>
          <br><br><br><br>
        </v-layout>

            <p data-aos="fade-left"><b>
              내 정보 확인페이지입니다.
            </b></p>

          <hr class="my-4" />
          
          <v-text-field
            name="member.name"
            color="green"
            background-color="transparent"
            v-model="memberInfo.name"
            label="Name"
            readonly
            data-aos="fade-right"
          ></v-text-field>

          <v-text-field
            type="email"
            color="green"
            background-color="transparent"
            name="member.email"
            v-model="memberInfo.email"
            label="E-mail"
            readonly
            required
            data-aos="fade-left"
          ></v-text-field>

          <v-text-field
            type="point"
            color="green"
            background-color="transparent"
            name="member.email"
            v-model="memberInfo.point"
            label="Point"
            readonly
            required
            data-aos="fade-right"
          ></v-text-field>

          
          <v-text-field
            address="address"
            color="green"
            background-color="transparent"
            v-model="memberInfo.address"
            label="주소"
            readonly
            required
            data-aos="fade-left"
          ></v-text-field>

          <v-text-field
            name="member.addressDetail"
            color="green"
            background-color="transparent"
            v-model="memberInfo.addressDetail"
            label="상세주소"
            readonly
            data-aos="fade-right"
          ></v-text-field>

          <v-text-field
            type="mbti"
            color="green"
            background-color="transparent"
            name="member.mbti"
            v-model="memberInfo.mbti"
            label="MBTI"
            readonly
            required
            data-aos="fade-left"
          ></v-text-field>
            
          <hr class="my-4" />

          <v-layout row justify-center align-center wrap class="mt-4 pt-2" data-aos="fade-up">
            <v-btn color="blue" class="white--text" @click="memberModify">정보수정</v-btn>
            <v-btn color="green" class="white--text" @click="memberPasswordModify">비밀번호변경</v-btn>
            <v-btn color="red" class="white--text" @click.prevent="check()">회원탈퇴</v-btn>
          </v-layout>
          <br>
        </b-col>
        <b-col></b-col>
      </b-row>
      
    </b-container>
    
  </div>
</template>

<script>
import { mapState } from "vuex";
import JoinRequest from "@/views/user/JoinRequest";
export default {
  name: "mypage",
  components: {
    JoinRequest
  },
  computed: {
    ...mapState(["memberInfo","managingTeam"])
  },
  
  created: function(){
    this.$store.dispatch("GET_MANAGE_TEAM", this.memberInfo.memberId);
    this.$store.dispatch("getRequests", this.managingTeam.teamId);
    console.log(this.memberInfo.memberId);
    console.log(this.managingTeam.memberId);
  },
  methods: {
    modify() {
      alert("아직 안만듬");
    },
    delete(){
      alert("아직 안만듬");
    },
    check(){
      alert("관리자에게 삭제요청이 전송되었습니다.");
    },
    memberModify(){
      this.$router.push("/membermodify");
    },
    memberPasswordModify(){
      this.$router.push("/memberpasswordmodify");
    },
    loadf() {
      if (this.type === "update") {
        var oldpre = document.querySelector(".oldpre");
        oldpre.style.display = "none";
        var old = document.querySelector(".old");
        old.style.display = "none";
      }
      var file = document.getElementById("chooseFile");

      var preview = document.querySelector(".preview");
      preview.src = URL.createObjectURL(file.files[0]);

      console.log(file.files[0]);

      preview.style.width = "60%";
      preview.style.height = "60%";
      preview.style.maxHeight = "500px";
    },
  },
};
</script>

<style></style>

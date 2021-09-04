<template>
  <v-container grid-list-xl>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <v-flex xs12 sm12 md6 lg6 xl6>
        <v-layout align-center data-aos="fade-right">
          <v-toolbar-title class="headline">
              <span><b>회원정보</b></span>
              <span class="green--text"><b>&nbsp;변경</b></span>
          </v-toolbar-title>
          <br><br><br><br>
        </v-layout>
        <form method="POST">
          <v-layout align-center data-aos="fade-right">
              <v-toolbar-title class="headline">
                  <span><b>프로필 사진</b></span>
                  <span class="green--text"><b>&nbsp;변경</b></span>
              </v-toolbar-title>
            <br><br><br><br>
          </v-layout>

          <div class="img_wrap">
            <img src="" class="preview" />
              <v-btn color="green" class="white--text"><label for="chooseFile">
                Upload yor Image
              </label></v-btn>
              <input
                type="file"
                id="chooseFile"
                name="chooseFile"
                accept="image/*"
                @change="loadf"
              />
            </div>

          <v-text-field
            name="member.name"
            color="green"
            background-color="transparent"
            v-model="memberInfo.name"
            label="Name"
            readonly
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
          ></v-text-field>

          <v-text-field
            address="address"
            color="green"
            background-color="transparent"
            v-model="member.address"
            label="주소"
            required
          ></v-text-field>

          <v-text-field
            name="member.addressDetail"
            color="green"
            background-color="transparent"
            v-model="member.addressDetail"
            label="상세주소"
          ></v-text-field>

          <v-select
            v-model="mbti"
            :items="mbtiList"
            label="MBTI"
            item-text="name"
            item-value="value"
            return-object
          ></v-select>

          

          <div class="buttons">
            <v-btn
              type="button"
              color="green" class="white--text"
              @click="modify"
            >회원 변경</v-btn>
            <v-btn @click="clear">초기화</v-btn>
            <v-btn large flat to="/mypage" class="green--text">
              <v-icon>arrow_back</v-icon>뒤로가기
            </v-btn>
          </div>
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
    this.member.password = this.memberInfo.password;
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
    this.photo=this.memberInfo.photo.filePath;
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
      photo:null,
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
      const formData = new FormData();

      const instance = createInstance();
      formData.append("memberId", this.member.memberId);
      formData.append("name", this.member.name);
      formData.append("email", this.member.email);
      formData.append("phone", this.member.phone);
      formData.append("password", this.member.password);
      formData.append("address", this.member.address);
      formData.append("addressDetail", this.member.addressDetail);
      formData.append("zonecode", this.member.zonecode);
      formData.append("sex", this.member.sex);
      formData.append("point", this.member.point);
      formData.append("mbti", this.member.mbti);
      formData.append("authenticated", this.member.authenticated);
      const tmp = document.getElementById("chooseFile").files[0];
      console.log(tmp);
      if(tmp==null){
        alert("이미지가 선택되지 않았습니다. 이미지를 선택해주세요.");
        return;
      }else{
        formData.append(
          "image",
          document.getElementById("chooseFile").files[0]
        );
      }
      instance.put("/member", 
      formData,
      {
        Headers: {
          "Content-Type": "multipart/form-data"
        }
      })
      .then(
        (response) => {
          if (response.data.message === "success") {
            alert("회원정보가 변경되었습니다. 새로운 정보로 다시 로그인 해주세요.");
            this.$store.commit("setMemberInfo", this.member);
            this.$store.dispatch("LOGOUT")
            .then(() => {
              if (this.$route.path !== "/") this.$router.replace("/");
            })
            .catch(() => {
              console.log("로그아웃 에러입니다.");
            });
            this.$router.push("/");
          } else {
            alert("회원변경 실패");
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
    loadf() {
      var file = document.getElementById("chooseFile");
      var preview = document.querySelector(".preview");
      preview.src = URL.createObjectURL(file.files[0]);

      console.log(file.files[0]);

      preview.style.width = "60%";
      preview.style.height = "60%";
      preview.style.maxHeight = "500px";
    },
    check(){
      console.log(this.memberInfo);
    }
  },

};
</script>

<style scoped>
.buttons {
  position: relative;
  margin-left: 33%;
}
</style>

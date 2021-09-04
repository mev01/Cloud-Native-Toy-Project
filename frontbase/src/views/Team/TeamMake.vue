<template>
  <v-container grid-list-xl>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <v-flex xs12 sm12 md6 lg6 xl6>
        <v-layout align-center data-aos="fade-right">
          <v-toolbar-title class="headline">
              <span><b>팀</b></span>
              <span class="green--text"><b>&nbsp;생성</b></span>
          </v-toolbar-title>
          <br><br><br><br>
        </v-layout>

        <form>
          <div class="img_wrap">
            <v-btn color="green"
            class="white--text"><label for="chooseFile">
              팀 썸네일 이미지
            </label></v-btn>
            <input
              type="file"
              id="chooseFile"
              name="chooseFile"
              accept="image/*"
              @change="loadf"
            />
            <img src="" class="preview" />
          </div>

          <v-text-field
            name="team.name"
            color="green"
            background-color="transparent"
            v-model="team.name"
            label="팀이름"
          ></v-text-field>

          <v-btn
            @click="duplicateName"
            type="button"
            color="green"
            class="white--text duplicate"
          >
            중복검사
          </v-btn>

          <v-select
            v-model="team.sport"
            :items="sportList"
            label="종목"
            item-text="name"
            item-value="value"
            return-object
          ></v-select>

          <v-text-field
            name="team.introduction"
            color="green"
            background-color="transparent"
            v-model="team.introduction"
            label="팀소개"
          ></v-text-field>

          <br>
          <div class="buttons">
            <v-btn
              @click="submit"
              type="button"
              color="green"
              class="white--text"
              >생성하기</v-btn
            >
            <v-btn @click="clear">clear</v-btn>
          </div>
        </form>
      </v-flex>
    </v-layout>
  </v-container>
</template>


<script>
import { createInstance } from "@/api/teamindex.js";
import { mapGetters } from 'vuex';

export default {
  computed: {
    ...mapGetters(["memberInfo", "selectTeam", "SET_SELECT_TEAM"]),
  },
  data() {
    return {
      sportList: [
        { name: "러닝", value: 1 },
        { name: "헬스", value: 2 },
        { name: "수영", value: 3 },
        { name: "탁구", value: 4 },
      ],
      team: {
        name: "",
        introduction: "",
        leader: "",
        member: {
          memberId: Number()
        },
        sportDto: {
          sportId: Number()
        }
      }
    };
  },
  methods: {
    loadf() {
      var file = document.getElementById("chooseFile");

      let preview = document.querySelector(".preview");
      preview.src = URL.createObjectURL(file.files[0]);
      preview.style.width = "60%";
      preview.style.height = "60%";
      preview.style.maxHeight = "500px";
    },
    submit() {
      this.team.leader = this.memberInfo.name
      this.team.member.memberId = this.memberInfo.memberId
      this.team.sportDto.sportId = this.team.sport.value

      console.log(this.team);

      const formData = new FormData();
      formData.append("name", JSON.stringify(this.team.name));
      formData.append("introduction", JSON.stringify(this.team.introduction));
      formData.append("leader", JSON.stringify(this.team.leader));
      formData.append("memberId", JSON.stringify(this.team.member.memberId));
      formData.append("sportId", JSON.stringify(this.team.sportDto.sportId));
      if(document.getElementById("chooseFile").files[0] !=null){
        formData.append("multipartFile", document.getElementById("chooseFile").files[0]);
      }

      for (var key of formData.keys()) {
      console.log(key);
      }

      for (var value of formData.values()) {
      console.log(value);
      }
      
      const instance = createInstance();
      instance
        .post("/team", formData, {
          Headers: {
            "Content-Type": "multipart/form-data"
          }
        })
        .then(response => {
          if (response.data.data === "success") {
            alert("팀생성완료 완료");
            this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);
            this.$store.dispatch("GET_MANAGE_TEAM", this.memberInfo.memberId);
            this.$router.push("/feed");
          } else {
            alert("팀생성 실패");
          }
        })
        .catch(() => {
          alert("에러발생!");
        });
    },
    clear() {
      this.team.name = "";
      this.team.introduction = "";
    },
    duplicateName() {
      const instance = createInstance();
      instance.get('/team/"' + this.team.name + '"/exists').then(({ data }) => {
        if (data) {
          alert("이미 사용된 팀명입니다!");
        } else {
          alert("사용가능한 팀명입니다!");
        }
      });
    },
  },
};
</script>


<style scoped>
.duplicate {
  position: relative;
  margin-left: 85%;
}
.buttons {
  position: relative;
  margin-left: 33%;
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
#chooseFile {
  display: none;
}
.fileBtn {
  background: lavender;
  border-radius: 15px;
  padding: 0.5em 1em;
}
.fileBtn:hover {
  cursor: pointer;
}
</style>
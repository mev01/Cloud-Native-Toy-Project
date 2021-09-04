<template>
  <div class="wrap">
    <div class="img_wrap">
      <v-btn color="green" class="white--text">
        <label v-if="this.type === 'update'" for="chooseFile" class="fileBtn">
          MODIFY your IMAGE
        </label>
        <label v-else for="chooseFile" class="fileBtn">
          UPLOAD your IMAGE
        </label>
      </v-btn>
      <input
        type="file"
        id="chooseFile"
        name="chooseFile"
        accept="image/*"
        @change="loadf"
      />

      <div class="oldwrap">
        <div v-if="this.type === 'update'" class="old">이전이미지</div>
        <img
          v-if="this.type === 'update'"
          v-bind:src="this.oneFeed.image"
          class="oldpre"
        />
      </div>

      <img src="" class="preview" />
    </div>
    <div class="challenge_wrap">
      <v-btn color="green" class="white--text"
        ><label for="challenge" class="feedType">▼일상글|챌린지▼</label></v-btn
      >
      <select id="challenge" v-model="challenge" class="dailyFeed">
        <option><b>일상글</b></option>
        <option
          v-for="(challenge, idx) in feed_challenging"
          :key="idx"
          :value="challenge"
        >
          <b>{{ challenge.text.teamChallenge.title }}</b>
        </option>
      </select>
    </div>

    <div class="contents_wrap">
      <textarea v-model="contents" class="contents"></textarea>
    </div>

    <v-btn
      v-if="this.type === 'update'"
      color="green"
      class="white--text"
      @click="modify"
      >수정</v-btn
    >
    <v-btn v-else color="green" class="white--text" @click="write">등록</v-btn>

  
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { createInstance } from "@/api/teamindex.js";
import "../../components/css/feed/writeFeed.scss";

export default {
  props: {
    type: { type: String }
  },
  data: () => {
    return {
      contents: "",
      challenge: Number(),
      file: null,
      src: ""
    };
  },
  computed: {
    ...mapGetters([
      "memberInfo",
      "myTeamList",
      "team_challenges",
      "feed_challenging",
      "oneFeed",
      "feedid",
      "selectTeam"
    ])
  },

  created() {
    this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);

    if (this.type === "update") {
      this.contents = this.oneFeed.contents;
    }
  },

  methods: {
    write() {
      var daily = document.querySelector(".dailyFeed");
      const formData = new FormData();

      if (daily.options[daily.selectedIndex] == null) {
        alert("일상글, 챌린지 선택 필수");
      } else if (document.getElementById("chooseFile").files[0] == null) {
        alert("사진을 넣어주세요");
      } else {
        if (daily.options[daily.selectedIndex].value == "일상글") {
          formData.append("teamchallengeId", 0);
        } else {
          formData.append(
            "teamchallengeId",
            this.challenge.text.teamChallenge.teamChallengeId
          );
        }
        formData.append("memberId", this.memberInfo.memberId);
        formData.append("teamId", this.selectTeam.teamId);
        formData.append("teamName", this.selectTeam.name);
        formData.append("contents", this.contents);
        formData.append("writer", this.memberInfo.name);
        formData.append(
          "image",
          document.getElementById("chooseFile").files[0]
        );

        const instance = createInstance();
        instance
          .post("/feed", formData, {
            Headers: {
              "Content-Type": "multipart/form-data"
            }
          })
          .then(response => {
            if (response.data.data === "success") {
              this.$store.dispatch(
                "GET_TEAMCHALLENGEING_INFO",
                this.memberInfo.memberId
              );
              this.$store.dispatch("getMyFeeds", this.memberInfo.memberId);
              this.$store.dispatch("getMyTeamFeeds", this.selectTeam.teamId);

              alert("피드 작성 완료");
              this.$router.push("/teamfeed");
            } else {
              alert("피드 작성 실패");
            }
          })
          .catch((error) => {
            console.log(error);
            alert(error);
            alert("에러발생!!.");
          });
      }
    },
    modify() {
      var daily = document.querySelector(".dailyFeed");
      const formData = new FormData();
      if (daily.options[daily.selectedIndex] == null) {
        alert("일상글, 챌린지 선택 필수");
      } else {
        if (daily.options[daily.selectedIndex].value == "일상글") {
          formData.append("teamchallengeId", 0);
        } else {
          formData.append(
            "teamchallengeId",
            this.challenge.text.teamChallenge.teamChallengeId
          );
        }
        formData.append("feedId", this.feedid);
        formData.append("memberId", this.oneFeed.memberId);
        formData.append("teamId", this.oneFeed.teamId);
        formData.append("teamName", this.oneFeed.teamName);
        formData.append("contents", this.contents);
        formData.append("writer", this.oneFeed.writer);
        if (document.getElementById("chooseFile").files[0] != null) {
          formData.append(
            "image",
            document.getElementById("chooseFile").files[0]
          );
        }

        const instance = createInstance();
        instance
          .put("/feed", formData, {
            Headers: {
              "Content-Type": "multipart/form-data"
            }
          })
          .then(response => {
            if (response.data.data === "success") {
              this.$store.dispatch(
                "GET_TEAMCHALLENGEING_INFO",
                this.memberInfo.memberId
              );
              alert("피드 수정 완료");
              this.$router.push("/feed");
            } else {
              alert("피드 수정 실패");
            }
          })
          .catch(() => {
            alert("에러발생");
          });
      }
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
    check() {
      console.log(this.selectTeam);
    }
  }
};
</script>

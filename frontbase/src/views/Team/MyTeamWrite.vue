<template>
  <v-container grid-list-xl>
    <v-layout column justify-center align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
        <span><b>피드를 </b></span>
        <span><b>&nbsp;작성할 </b></span>
        <span class="green--text"><b>팀을&nbsp;선택해주세요</b></span>
      </v-toolbar-title>
    </v-layout>

    <v-layout
      justify-center
      row
      wrap
      class="mt-0 pt-2"
      data-aos="fade-down"
      v-if="myTeamList"
    >
      <PostPreviewWrite
        v-for="post in myTeamList"
        :key="post.text.teamId"
        :name="post.text.name"
        :introduction="post.text.introduction"
        :imgPath="post.text.photoDto.filePath"
        :teamId="post.text.teamId"
        :selectMyTeam="post.text"
        :leader="post.text.leader"
      />
    </v-layout>

    <v-layout justify-center align-center data-aos="fade-top" v-else>
      <h1>
        <b>가입된</b
        ><span class="green--text"><b>&nbsp;팀이 없습니다.</b></span>
      </h1>
    </v-layout>
  </v-container>
</template>

<script>
import { mapGetters } from "vuex";
import PostPreviewWrite from "@/components/PostPreviewWrite";

export default {
  components: {
    PostPreviewWrite
  },
  computed: {
    ...mapGetters(["myTeamList", "memberInfo"])
  },
  created() {
    this.$store.dispatch("GET_TEAMCHALLENGEING_INFO", this.memberInfo.memberId);
    this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);
    console.log(this.myTeamList);
  },
  data() {
    return {};
  },
  methods: {
    running() {
      this.selected = 0;
    },
    helth() {
      this.selected = 1;
    },
    swimming() {
      this.selected = 2;
    },
    tableTennis() {
      this.selected = 3;
    },
    setFilter: function(filter) {
      this.currentFilter = filter;
    },
    confirm() {
      alert(this.myTeamList[0].text.imgPath);
      console.log(this.myTeamList);
    },
    mvWrite() {
      this.$store.dispatch(
        "GET_TEAMCHALLENGEING_INFO",
        this.memberInfo.memberId
      );
      this.$router.push("/writefeed");
    }
  }
};
</script>

<style scoped></style>

<template>
  <v-container grid-list-xl>
   
      <v-layout align-center data-aos="fade-right">
        <v-toolbar-title class="headline">
            <span><b>달성</b></span>
            <span class="green--text"><b>&nbsp;기록</b></span>
        </v-toolbar-title>
        <br><br><br><br>
      </v-layout>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <v-flex v-for="(challenge,idx) in newChallenges" :key=idx xs6 sm3 md3 lg3 xl3>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-card v-on="on" hover flat color="transparent" :style="{'cursor':'context-menu'}">
              <v-card-title></v-card-title>
              <v-img
                :src="challenge.src"
                height="100"
                aspect-ratio="2.75"
                :alt="challenge.title"
                contain
              ></v-img>
              <v-card-title class="justify-center">
                <v-flex text-xs-center subheading font-weight-bold>{{ challenge.title }}</v-flex>
              </v-card-title>
            </v-card>
          </template>
          <span>{{ challenge.introduction }}</span>
        </v-tooltip>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
import attendance from "@/assets/images/attendance.png";
import comment from "@/assets/images/comment.png";
import feed from "@/assets/images/feed.png";
import team from "@/assets/images/team.png";
import point from "@/assets/images/point.png";
import security from "@/assets/images/security.png";
import attendance_b from "@/assets/images/attendance_b.png";
import comment_b from "@/assets/images/comment_b.png";
import feed_b from "@/assets/images/feed_b.png";
import team_b from "@/assets/images/team_b.png";
import point_b from "@/assets/images/point_b.png";
import security_b from "@/assets/images/security_b.png";

export default {
  created(){
  },
  computed: {
    ...mapGetters(["memberInfo", "entire_challenge"]),
    newChallenges() {
      if(this.entire_challenge.data.attendance < 30) {
        this.challenges[0].src = attendance_b
      } else {
        this.challenges[0].src = attendance
      }

      if(this.entire_challenge.data.teamCount < 5) {
        this.challenges[1].src = team_b
      } else {
        this.challenges[1].src = team
      }

      if(this.entire_challenge.data.feedCount < 10) {
        this.challenges[2].src = feed_b
      } else {
        this.challenges[2].src = feed
      }

      if(this.entire_challenge.data.commentCount < 10) {
        this.challenges[3].src = comment_b
      } else {
        this.challenges[3].src = comment
      }

      if(this.memberInfo.point < 100) {
        this.challenges[4].src = point_b
      } else {
        this.challenges[4].src = point
      }

      if(this.memberInfo.password.length < 10) {
        this.challenges[5].src = security_b
      } else {
        this.challenges[5].src = security
      }
      return this.challenges
    }
    
  },
  data() {
    return {
      challenges: [
        {
          src: "",
          title: "매일매일 개근왕",
          introduction: "30 일 출석 달성",
        },
        {
          src: "",
          title: "인싸중에 인싸",
          introduction: "5 개팀 가입 달성",
        },
        {
          src: "",
          title: "포스팅 워리어",
          introduction: "포스팅 10 개 달성",
        },
        {
          src: "",
          title: "댓글 워리어",
          introduction: "댓글 10 개 달성",
        },
        {
          src: "",
          title: "포인트 만수르",
          introduction: "100 포인트 달성",
        },
        {
          src: "",
          title: "궁극의 기억력",
          introduction: "비밀번호 10 자리 이상 충족",
        },
      ]
    };
  },
  methods(){
    
  }
};
</script>
<style >
.v-img--reveal {
  opacity: 0.3;
}
</style>
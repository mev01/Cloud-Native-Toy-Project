<template>
  <v-container grid-list-xl>

    <v-layout >
      <v-bottom-navigation
        class="mx-auto"
        shift
        x-large
      >
        <v-btn color="secondary" @click="moveMain">
          <i class="fas fa-address-card fa-2x"></i>
          <div>&nbsp;&nbsp;&nbsp;정보</div>
        </v-btn>

        <v-btn color="success" @click="moveTeamFeed">
          <i class="fas fa-clipboard fa-2x" ></i>
          <div>&nbsp;&nbsp;&nbsp;피드</div>
        </v-btn>

        <v-btn color="primary" @click="moveBoard">
          <i class="fas fa-check fa-2x" ></i>
          <div>&nbsp;&nbsp;&nbsp;공지사항</div>
        </v-btn>
        
        <v-btn color="warning" @click="moveChattingRoom">
          <i class="fas fa-comments fa-2x"></i>
          <div>&nbsp;&nbsp;&nbsp;채팅</div>
        </v-btn>

        <v-btn color="error"  @click="moveTeamChallenge">
          <i class="fas fa-trophy fa-2x"></i>
          <div>&nbsp;&nbsp;&nbsp;챌린지</div>
        </v-btn>
      </v-bottom-navigation>
    </v-layout>
    <br>
    <v-layout justify-center align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
          <span><b>챌린지</b></span>
          <span class="green--text"><b>&nbsp;등록하기</b></span>
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>
    <v-layout row justify-center align-center wrap class="mt-4 pt-2">
      <form>
          <v-text-field
            name="challenge.title"
            color="green"
            background-color="transparent"
            v-model="challenge.title"
            label="챌린지 제목:"
          ></v-text-field>

          <v-text-field
            type="challenge.contents"
            color="green"
            background-color="transparent"
            name="challenge.contents"
            v-model="challenge.contents"
            label="챌린지 내용"
          ></v-text-field>

          <date-picker v-model="challenge.date" range></date-picker>
      </form>
    </v-layout>
    <v-layout justify-center align-center data-aos="fade-top">
    <v-btn flat large dark color="black" target="_blank" @click="enroll()" :disabled="this.memberInfo.memberId !== this.selectTeam.memberId">
        등록하기
    </v-btn>
          </v-layout>

  </v-container>
</template>

<script>
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
import { mapState } from 'vuex';
import { createInstance } from "@/api/teamindex.js";


export default {
  components:{
    DatePicker 
  },
  computed:{
  
    ...mapState(["memberInfo","teamInfo","selectTeam","team_challenges"])
  },
  data() {
    return {
      challenge: {
        title: "",
        contents: "",
        date: [],
      },
    };
  },
  
  methods: {
      check(){
        alert(this.team_challenges);
      },
      enroll() {
        var today = new Date();   
        var start = new Date(this.challenge.date[0]);
        
        if(today>start){
          alert("오늘이나 이전날짜에는 등록할 수 없습니다.");
          return;
        }
        let start_date = JSON.stringify(this.challenge.date[0]).replaceAll('"', "");
        let end_date = JSON.stringify(this.challenge.date[1]).replaceAll('"', "");
        start_date = start_date.replaceAll('\\', "");
        end_date = end_date.replaceAll('\\',"");
        const instance = createInstance();
        
        const body = {
          "contents":this.challenge.contents,
          "endDate":end_date,
          "startDate":start_date,
          "teamChallengeId":0,
          "teamId": this.selectTeam.teamId,
          "title": this.challenge.title
        }; 
        instance.post("/team_challenge_enroll", JSON.stringify(body))
        .then(
          (response) => {
            if (response.data.message === "success") {
              alert("팀 챌린지 리스트 등록 완료");
              this.$store.dispatch("GET_TEAMCHALLENGE_INFO", this.memberInfo.memberId);
              this.$router.push("/teamchallenge");
            } else {
              alert("팀 챌린지 리스트 등록 실패");
            }
          }
        )
        .catch();

      },
      
      moveMain(){
        this.$router.push("/teammain");
      },
      moveTeamChallenge(){
        this.$router.push("/teamChallenge");
      },
      moveBoard(){
        this.$router.push("/board");
      },
      moveChattingRoom(){
        this.$router.push("/chattingroom");
      },
      moveTeamFeed(){
        this.$router.push("/teamFeed");
      }          
    },
      
};
</script>


<style>

</style>
<template>
  <v-container grid-list-xl>
    

    <v-layout >
      <v-bottom-navigation
        class="mx-auto"
        shift
        x-large
      >
      <team-header />
      </v-bottom-navigation>
    </v-layout>
    <br><br><br>
    <v-layout align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
          <span><b>현재 도전중인</b></span>
          <span class="green--text"><b>&nbsp;챌린지</b></span>
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>
    
    <br>
    <div v-if="team_challenging.length">
      <table id="book-list">
        <colgroup>
          <col style="width: 10%" />
          <col style="width: 20%" />
          <col style="width: 30%" />
          <col style="width: 10%" />
          <col style="width: 20%" />
        </colgroup>
        <thead>
          <tr>
            <th>챌린지번호</th>
            <th>챌린지 제목</th>
            <th>내용</th>
            <th>종료일자</th>
            <th>달성여부</th>
            <th>챌린지포기</th>
          </tr>
        </thead>
        <tbody>
        
          <list-row-team-challenge-participate
            v-for="(challenge, index) in team_challenging"
            :key="index"
            :no=challenge.value.teamChallenge.teamChallengeId
            :title="challenge.value.teamChallenge.title"
            :contents="challenge.value.teamChallenge.contents"
            :endDate="challenge.value.teamChallenge.endDate"
            :done="challenge.value.done"
          />
          </tbody>
      </table>
    </div>

    <br><br>

    <br>
    <v-layout align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
          <span><b>팀에서 진행중인</b></span>
          <span class="green--text"><b>&nbsp;챌린지</b></span>
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>
    
    <br>

    <div v-if="team_challenges.length">
      <table id="book-list">
        <colgroup>
          <col style="width: 10%" />
          <col style="width: 30%" />
          <col style="width: 20%" />
          <col style="width: 20%" />
          <col style="width: 20%" />
        </colgroup>
        <thead>
          <tr>
            <th>챌린지번호</th>
            <th>챌린지 제목</th>
            <th>내용</th>
            <th>종료일자</th>
            <th>챌린지신청</th>
          </tr>
        </thead>
        <tbody>
        
          <list-row-team-challenge
            v-for="(challenge, index) in items"
            :key="index"
            :no=challenge.text.teamChallengeId
            :title="challenge.text.title"
            :contents="challenge.text.contents"
            :endDate="challenge.text.endDate"
          />
          </tbody>
      </table>
    </div>

    <br><br>

    <v-layout justify-end data-aos="fade-right">
    <v-btn flat to="/challengemake" :disabled="this.memberInfo.memberId !== this.selectTeam.memberId" active-class="green--text headline"><i class="fas fa-plus"></i>챌린지 만들기</v-btn>
    </v-layout>
  </v-container>
</template>

<script>
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
import { mapGetters, mapState } from 'vuex';
import { createInstance } from "@/api/teamindex.js";
import ListRowWholeChallenge from "@/components/ListRowWholeChallenge.vue";
import ListRowTeamChallenge from "@/components/ListRowTeamChallenge.vue";
import TeamHeader from "@/components/TeamHeader.vue"
import ListRowTeamChallengeParticipate from '../../components/ListRowTeamChallengeParticipate.vue';

export default {
  components:{
    DatePicker,
    ListRowWholeChallenge,
    ListRowTeamChallenge,
    TeamHeader,
    ListRowTeamChallengeParticipate
  },
  computed:{
    ...mapGetters (["team_challenging"]),
    ...mapState(["memberInfo","teamInfo","selectTeam","team_challenges","team_challenging"])
  },
  created: function(){
    if(this.team_challenges.length>0){
      for(var i = 0; i< this.team_challenges.length;i++){
        if(this.team_challenges[i].text.teamId===this.selectTeam.teamId)
          this.items.push(this.team_challenges[i]);
      }
    }
  },

  data() {
    return {
      items:[],
      headers: [
        { text: '챌린지ID', align: 'center', value: 'teamChallengeId' },
        { text: '챌린지제목', align: 'center',value: 'title' },
        { text: '챌린지내용', align: 'center',  value: 'contents' },
        { text: '종료일자', align: 'center', value: 'endDate' },
      ],
      page: 1,
      pageCount: 0,
      loading: false,
      itemsPerPage: 10,
      dialog: false,
      
    }
  },
  
  methods: {
      check(){
        
      },
    },
      
};
</script>


<style>
#book-list {
  border-collapse: collapse;
  width: 100%;
}

#book-list thead {
  font-weight: bold;
}

#book-list td,
#book-list th {
  text-align: center;
  border-bottom: 1px solid #ddd;
  height: 50px;
}
</style>
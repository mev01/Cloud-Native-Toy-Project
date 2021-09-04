<template>
  <tr>
    <td><b>{{ no }}</b></td>
    <td><b>{{ title }}</b></td>
    <td><b>{{ contents }}</b></td>
    <td><b>{{ date }}</b></td>
    <td><b>{{doneFlag}}</b></td>
    <td><v-btn color="error" @click="giveUp">포기</v-btn></td>
  </tr>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { createInstance } from "@/api/teamindex.js";


export default {
  name: "ListRow",
  props: {
    no: Number,
    title: String,
    contents: String,
    endDate: String,
    done: Boolean,
  },
  data() {
    return {
      doneFlag:"",
    }
  },
  created: function(){
    if(this.done==false){
      this.doneFlag="미달성"
    }else{
      this.doneFlag="달성"
    }
    var today = new Date(this.endDate);
    var year = today.getFullYear();
    var month = ('0' + (today.getMonth() + 1)).slice(-2);
    var day = ('0' + today.getDate()).slice(-2);
    this.date=year +"-"+month  +"-"+day;
  },
  computed:{
    ...mapGetters(["team_challenging"]),
    ...mapState(["memberInfo","teamInfo","selectTeam","team_challenges"])
  },
  methods:{
    participate(){
        const instance = createInstance();
        const body = {
          memberId: this.memberInfo.memberId,
          teamChallengeId: this.no,
        }; 
        console.log(body);
        instance.post("/team_challenge_participate", JSON.stringify(body))
        .then(
          (response) => {
            console.log(response);
            if (response.data.data === "success") {
              alert("챌린지에 등록되었습니다.");
              
            } else {
              alert("챌린지 추가에 실패하였습니다.");
            }
          }
        )
        .catch();
      },
      giveUp(){
        const body = {
          done: this.done,
          memberId: this.memberInfo.memberId,
          teamChallengeId: this.no,
          teamchallengerId: 0,
        }; 
        console.log(body);
        const instance = createInstance();
      
        instance.post("/team_challenge_giveup", JSON.stringify(body))
        .then(
          (response) => {
            console.log(response);
            if (response.data.data === "success") {
              this.$store.dispatch("GET_TEAMCHALLENGE_INFO", this.memberInfo.memberId);
              const token={
                memberId: this.memberInfo.memberId,
                teamId:this.selectTeam.teamId
              };
              this.$store.dispatch("GET_TEAMCHALLENGER_INFO", token);  
              
              alert("챌린지를 포기하셨습니다.");
              
            } else {
              alert("챌린지 포기에 실패하셨습니다.");
            }
          }
        )
        .catch();
      },
      check(){
        console.log(this.team_challenging);
      }  
  }
};
</script>
<style scope>
td {
  text-align: center;
  border-bottom: 1px solid #ddd;
  height: 30px;
  width: 70px;
}
</style>

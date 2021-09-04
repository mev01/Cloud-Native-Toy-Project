<template>
  <v-container grid-list-xl>
    <v-layout>
      <v-bottom-navigation
        class="mx-auto"
        shift
        x-large
      >
        <v-btn color="purple" @click="all">
          <i class="fas fa-list-ul fa-2x white--text" ></i>
        </v-btn>

        <v-btn color="secondary" @click="running">
          <i class="fas fa-running fa-2x" ></i>
        </v-btn>

        <v-btn color="success" @click="health">
          <i class="fas fa-dumbbell fa-2x" ></i>
        </v-btn>

        <v-btn color="primary" @click="swimming">
          <i class="fas fa-swimmer fa-2x" ></i>
        </v-btn>
        
        <v-btn color="error" @click="tableTennis">
          <i class="fas fa-table-tennis fa-2x" ></i>
        </v-btn>
      </v-bottom-navigation>
    </v-layout>
    <br><br>
    <div v-if="this.teamLists">
      <v-layout column justify-center align-center data-aos="fade-right">
        <v-toolbar-title class="headline">
            <span><b>전체</b></span>
            <span class="green--text"><b>&nbsp;팀</b></span>
        </v-toolbar-title>
        <br>
      </v-layout>

      <v-layout row justify-center align-center wrap class="mt-0 pt-0">
          
          <v-flex
          v-for="(teamList, idx) in this.filteredTeam"
          :key="idx"
          xs12 sm6 md4 lg3 xl3
          >
          <v-card
            hover
            flat
            height="230"
            @click="confirm(teamList)"
          >
            <div v-if="teamList.photoDto === null">
              <v-img v-bind:src="thumbnail" aspect-ratio="2.75" height="130" contain></v-img>
            </div>
            <div v-else>
              <v-img :src="teamList.photoDto.filePath" aspect-ratio="2.75" height="130" contain></v-img>
            </div>
            <v-card-title primary-title class="justify-center">
              <v-flex text-xs-center subheading font-weight-bold>{{teamList.name.replaceAll("\"", "")}}
                <br>
                <div v-if="teamList.introduction.length >10 " class="txt_line">{{teamList.introduction.replaceAll("\"", "").slice(0,13)+"..."}}</div>                
                <div v-else class="txt_line">{{teamList.introduction.replaceAll("\"", "")}}</div>            
              </v-flex>
            </v-card-title> 
          </v-card>
        </v-flex>
      </v-layout>
      <br><br><br><br><br><br>
      <v-layout column justify-center align-center data-aos="fade-up" v-if="!this.recommendFlag">
        <v-toolbar-title class="headline">
            <span><b>어떤 팀이 나와 맞을지</b></span>
            <span class="green--text"><b>&nbsp;모르시겠다구요?</b></span>
        </v-toolbar-title>
        <br><br>
        <v-btn color="primary" @click="recommend">팀 추천받기</v-btn>
      </v-layout>

      <br><br>

      <div v-if="this.memberInfo.authenticated&&this.recommendFlag==true">
        <v-layout column justify-center align-center data-aos="fade-right">
        <v-toolbar-title class="headline">
            <span><b>추천</b></span>
            <span class="green--text"><b>&nbsp;팀</b></span>
        </v-toolbar-title>
      </v-layout>

      <v-layout row justify-center align-center wrap class="mt-4 pt-2" data-aos="">
          <v-flex
          v-for="(recomendTeam, idx) in this.filteredRecomendTeam"
          :key="idx"
          xs12 sm6 md4 lg3 xl3
          >
          <v-card
            hover
            flat
            height="230"
            @click="confirm(recomendTeam)"
          >
            <div v-if="recomendTeam.photoDto === null">
              <v-img v-bind:src="thumbnail" aspect-ratio="2.75" height="130" contain></v-img>
            </div>
            <div v-else>
              <v-img :src="recomendTeam.photoDto.filePath" aspect-ratio="2.75" height="130" contain></v-img>
            </div>
            <v-card-title primary-title class="justify-center">
              <v-flex text-xs-center subheading font-weight-bold>{{recomendTeam.name.replaceAll("\"", "")}}
                <br>
                <div class="txt_line">팀과의 궁합도<v-icon color="red" small>favorite</v-icon> {{Math.round(recomendTeam.score*100/5)}}%</div>                
              </v-flex>
            </v-card-title> 
          </v-card>
        </v-flex>
      </v-layout>
      </div>

      <v-btn
        v-if="leadercheck===false"
        @click="mvTeam"
        color="secondary"
        elevation="7"
        fab
        large
        x-large
        x-small
        class="create"
      ><i class="fas fa-plus"></i>
      </v-btn>
    </div>
    
    <div v-else>
      <v-btn
        @click="mvTeam"
        color="secondary"
        elevation="7"
        fab
        large
        x-large
        x-small
        class="create"
      ><i class="fas fa-plus"></i>
      </v-btn>
    </div>
  </v-container>
</template>

<script>
import { mapGetters } from "vuex";
import thumbnail from "@/assets/images/thumbnail.jpg";


export default {
  components: {
    
  },
  computed: {
    ...mapGetters(["teamLists", "memberInfo", "myRecomendTeams"]),
    
    filteredTeam : function() {
        var cname = this.name.trim();
        return this.teamLists.filter(function(item,index) {
        if (item.sportDto.name.indexOf(cname) >-1) {
            return true;
        }
      });
    },
    filteredRecomendTeam : function() { 
        var cname = this.name.trim();
        return this.myRecomendTeams.filter(function(item,index) {
        if (item.sportDto.name.indexOf(cname) >-1) {
            return true;
        }
      });
    }
  },
  created() {
    this.$store.dispatch("getTeamLists");
    this.checkleader();

    this.feedget.memberId = this.memberInfo.memberId;
    this.feedget.page = 0;
    this.page = 0;

    this.$store.dispatch("getFeeds", this.feedget);
    this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);
    this.$store.dispatch("getTeamLists");
    this.$store.dispatch("GET_ENTIRECHALLENGE_INFO", this.memberInfo.memberId);
    this.$store.dispatch("getMyFeeds", this.memberInfo.memberId);
    this.$store.dispatch("GET_RECOMEND_TEAMS", this.memberInfo.memberId);
    this.$store.dispatch("GET_MANAGE_TEAM", this.memberInfo.memberId);
  },
  data() {
    return {
      selected: 5,
      thumbnail: thumbnail,
      name: "",
      leadercheck: false,
      recommendFlag: false,
      feedget: {
        memberId: "",
        page: ""
      },
    };
  },
  methods: {
    mvTeam() {
      this.$router.push("/teammake");
    },
    all() {
      this.name="";
    },
    running() {
      this.name ="러닝";
      console.log("러닝");
    },
    health() {
      this.name ="헬스";
      console.log("헬스");
    },
    swimming() {
      this.name="수영";
      console.log("수영");
    },
    tableTennis() {
      this.name="탁구";
      console.log("탁구");
    },
    confirm(data){
      this.$store.dispatch("SET_SELECT_TEAM", data).then(()=>{
        this.$router.replace("/teammain");
      });
    },
    checkleader() {
      for(let i=0; i<this.teamLists.length; i++) {
        if (this.teamLists[i].memberId === this.memberInfo.memberId) {
          this.leadercheck = true;
          break;
        }
      }
    },
    recommend(){
      if(this.memberInfo.authenticated!=true){
        alert("프리미엄회원에게만 제공되는 서비스입니다.");
        this.$router.push(`/payhome`);

        return;
      }
      this.recommendFlag=true;
    },
    check(){
      console.log(this.myRecomendTeams);
    }
  },
};
</script>

<style scoped>
.create {
  position: absolute;
  right: 80px;
  bottom: 50px;
};
.txt_line {
  width:70px;
  padding:0 5px;
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
};
</style>

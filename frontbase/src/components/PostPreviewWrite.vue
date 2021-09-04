<template>
  <v-flex xs12 sm6 md4 lg4 xl4>
  
    <v-card @click="writeCntTeam()" hover>
    
      <v-img :src="imgPath" aspect-ratio="2.75" height="230" :alt="name"></v-img>
      <v-card-title primary-title>
        <div>
          <p class="headline mb-0"><b>{{name.replaceAll("\"", "")}}</b></p>
          <div>
            <p class="green--text font-weight-medium"><b>{{introduction.replaceAll("\"", "")}}</b></p>
          </div>
        </div>
      </v-card-title>
      
    </v-card>
  

  </v-flex>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["selectTeam","myTeamList","memberInfo","SET_SELECT_TEAM"]),
  },
  props: {
    name: {
      type: String,
      required: true
    },
    introduction:{
      type:String,
      required:true
    },
    imgPath: {
      type: String,
      required: true
    },
    teamId: {
      type: Number,
      required: true
    },
    selectMyTeam:{
      type: Object,
      required:true
    },
    leader:{
      type:String,
      required:true
    }
  },
  methods: {
    writeCntTeam(){
      console.log(this.selectTeam);
      const body = {
        name: this.name,
        introduction: this.introduction,
        leader: this.leader,
        teamId: this.teamId,
        photoDto:{
          filePath : this.imgPath,
        }
      };
      this.$store.dispatch("SET_SELECT_TEAM", this.selectMyTeam).then(()=>{
        //this.$router.replace("/teammain");
      });
      this.$store.dispatch(
        "GET_TEAMCHALLENGEING_INFO",
        this.memberInfo.memberId
      ).then(()=>{
          this.$router.push("/writefeed");
      });
      
    }
    
  },
};
</script>

<style scoped>
</style>
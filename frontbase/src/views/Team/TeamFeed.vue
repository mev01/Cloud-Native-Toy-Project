<template>
  <div class="feed newsfeed">
    <v-layout>
      <v-bottom-navigation
        v-if="
          (teamcheck === true) |
            (this.selectTeam.memberId === this.memberInfo.memberId)
        "
        class="mx-auto"
        shift
        x-large
      >
        <team-header />
      </v-bottom-navigation>

      <v-bottom-navigation v-else class="mx-auto" shift x-large>
        <team-header-2 />
      </v-bottom-navigation>
    </v-layout>


    <br>
    <div v-if="lists" class="wrapB">
      <v-layout justify-center align-center data-aos="fade-down">
        <v-toolbar-title v-if="teamcheck === true" class="headline">
          <v-btn
            @click="mvWrite"
            color="secondary"
            small
            ><v-icon>fas fa-plus</v-icon>
          </v-btn>
          
        </v-toolbar-title>
      </v-layout>
      <feed-item
        v-for="(feed, index, idx) in teamFeeds"
        :key="idx"
        :feed="feed"
        :index="index"
        :lists="lists"
      />
      
    </div>
  </div>
</template>
<script>
import "@/components/css/feed/feed-item.scss";
import "@/components/css/feed/newsfeed.scss";
import FeedItem from "@/views/Feed/FeedItem.vue";
import TeamHeader from "@/components/TeamHeader.vue";
import TeamHeader2 from "../../components/TeamHeader2.vue";
import { createInstance } from "@/api/teamindex.js";
import { mapGetters } from "vuex";

export default {
  components: {
    FeedItem,
    TeamHeader,
    TeamHeader2
  },
  data() {
    return {
      teamcheck: false,
      lists: null,
      teamcheck: false,
    };
  },
  computed: {
    ...mapGetters(["teamFeeds", "selectTeam", "myTeamList", "memberInfo"])
  },
  created() {
    this.$store.dispatch("getMyTeamFeeds", this.selectTeam.teamId);
    this.teamchecking();
    console.log(this.selectTeam.memberId);
    console.log(this.memberInfo.memberId);
    console.log(this.teamcheck);

    const instance = createInstance();
    instance
      .get("/feedlike/member/" + this.memberInfo.memberId)
      .then(({ data }) => {
        this.lists = data.object;
        console.log(this.lists);
        this.$store.dispatch("SET_LIKELIST", data.object);
      })
      .catch(() => {
        console.log("에러발생");
      });
  },
  methods: {
    teamchecking() {
      for (let i = 0; i < this.myTeamList.length; i++) {
        if (this.myTeamList[i].value.teamId === this.selectTeam.teamId) {
          this.teamcheck = true;
          break;
        }
      }
    },
    mvWrite() {
      this.$store.dispatch(
        "GET_TEAMCHALLENGEING_INFO",
        this.memberInfo.memberId
      );
      this.$router.push("/writefeed");
    },
    teamchecking() {
      for (let i = 0; i < this.myTeamList.length; i++) {
        if (this.myTeamList[i].value.teamId === this.selectTeam.teamId) {
          this.teamcheck = true;
          break;
        }
      }
    },
  }
};
</script>

<style scoped>
.writebtn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background-image: url("../../assets/plus.png");
  background-size: cover;
}
.writebtn:hover {
  cursor: pointer;
}


</style>

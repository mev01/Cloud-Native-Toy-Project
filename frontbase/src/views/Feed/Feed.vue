<template>
  <div class="feed newsfeed">
    <div v-if="lists" class="wrapB">
      <v-layout justify-center align-center data-aos="fade-up">
        <v-toolbar-title class="headline">
          <v-btn
            @click="mvMyteamWrite"
            color="secondary"
            elevation="7"
            large
            class="create"
            >피드작성&nbsp;<i class="fas fa-plus"></i>
          </v-btn>
          <v-btn @click="mvTeamList" large color="primary">
            다른 팀 찾아보기<v-icon>login</v-icon>
          </v-btn>
        </v-toolbar-title>
        <br /><br /><br /><br />
      </v-layout>

      <feed-item
        v-for="(feed, index, idx) in feeds"
        :key="idx"
        :feed="feed"
        :index="index"
        :lists="lists"
      />
    </div>

    <infinite-loading
      @infinite="infiniteHandler"
      spinner="spinner"
    ></infinite-loading>
  </div>
</template>
<script>
import "../../components/css/feed/newsfeed.scss";
import FeedItem from "@/views/Feed/FeedItem.vue";
import "../../components/css/feed/feed-item.scss";
import { mapGetters } from "vuex";
import InfiniteLoading from "vue-infinite-loading";
import { createInstance } from "@/api/teamindex.js";
export default {
  data() {
    return {
      feedget: {
        memberId: "",
        page: ""
      },
      limit: 3,
      page: 0,
      scrollHeight: 0,
      scrollTop: 0,
      clientHeight: 0,
      lists: null
    };
  },
  components: {
    FeedItem,
    InfiniteLoading
  },
  computed: {
    ...mapGetters(["memberInfo", "feeds"])
  },
  mounted() {},
  created() {
    this.feedget.memberId = this.memberInfo.memberId;
    this.feedget.page = 0;
    this.page = 0;
    
    this.$store.dispatch("getFeeds", this.feedget);
    this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);
    this.$store.dispatch("getTeamLists");
    this.$store.dispatch("GET_ENTIRECHALLENGE_INFO", this.memberInfo.memberId);
    this.$store.dispatch("getMyFeeds", this.memberInfo.memberId);
    this.$store.dispatch("GET_RECOMEND_TEAMS", this.memberInfo.memberId);

    const instance = createInstance();
    instance
      .get("/feedlike/member/" + this.memberInfo.memberId)
      .then(({ data }) => {
        this.lists = data.object;
        this.$store.dispatch("SET_LIKELIST", data.object);
      })
      .catch(() => {
      });
  },
  methods: {
    mvWrite() {
      this.$store.dispatch(
        "GET_TEAMCHALLENGEING_INFO",
        this.memberInfo.memberId
      );
      this.$router.push("/writefeed");
    },

    mvMyteam() {
      this.$router.push("/myteam");
    },
    mvMyteamWrite(){
      this.$router.push("/myteamwrite");
    },

    check() {
    },

    infiniteHandler($state) {
      const instance = createInstance();
      this.page += 1;
      instance
        .get("/feed/" + this.memberInfo.memberId + "/" + this.page)
        .then(response => {
          setTimeout(() => {
            if (response.data.object.length) {
              this.$store.commit("setFeeds", response.data.object);
              $state.loaded();
              this.limit += 3;
            } else {
              $state.complete();
            }
          }, 1300);
        })
        .catch(error => {
          console.log(error);
        });
    },
    mvTeamList() {
      this.$router.push("/teamlist");
    }
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

.create {
  
}
</style>

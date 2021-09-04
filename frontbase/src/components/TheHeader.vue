<template>
  <div>
    <v-navigation-drawer
      v-model="drawer"
      absolute
      temporary
      app
      width="150"
      height="340"
    >
      <v-list class="pt-4" v-if="memberInfo === null">
        <v-list-tile active-class="green--text" to="/">
          <v-list-tile-content>
            <v-list-tile-title>HOME</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile active-class="green--text" to="/signup">
          <v-list-tile-content>
            <v-list-tile-title>SIGNUP</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>

      <v-list class="pt-4" v-else>
        <v-list-tile active-class="green--text" to="/feed">
          <v-list-tile-content>
            <v-list-tile-title>FEED</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile active-class="green--text" to="/challenge">
          <v-list-tile-content>
            <v-list-tile-title>CHALLENGE</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile active-class="green--text" to="/teamlist">
          <v-list-tile-content>
            <v-list-tile-title>TEAMLIST</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile active-class="green--text" to="/myteam">
          <v-list-tile-content>
            <v-list-tile-title>MYTEAM</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>

        <v-list-tile
          v-if="this.waitingReqests && this.waitingReqests != 0"
          active-class="green--text"
          to="/myfeed"
        >
          <v-list-tile-content>
            <v-list-tile-title>
              MYPAGE &nbsp;<b-badge pill variant="danger">{{
                waitingReqests.length
              }}</b-badge>
            </v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile v-else active-class="green--text" to="/myfeed">
          <v-list-tile-content>
            <v-list-tile-title>MYPAGE</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile active-class="green--text" to="/payhome">
          <v-list-tile-content>
            <v-list-tile-title>PREMIUM</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>

        <v-list-tile active-class="" @click.prevent="onClickLogout" to="/">
          <v-list-tile-content>
            <v-list-tile-title>LOGOUT</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar flat dense color="transparent" scroll-off-screen>
      <v-toolbar-side-icon
        class="hidden-md-and-up"
        @click.stop="drawer = !drawer"
      ></v-toolbar-side-icon>
      <v-toolbar-title class="headline">
        <span class="font-weight-bold">혼자</span>
        <span class="green--text font-weight-bold">뭐하니?</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <!--<v-btn @click="changeTheme" depressed small icon class="hidden-md-and-up">
        <v-icon v-if="goDark == true">fas fa-sun</v-icon>
        <v-icon v-else>fas fa-moon</v-icon>
      </v-btn>-->

      <v-toolbar-items class="hidden-sm-and-down" v-if="memberInfo === null">
        <v-btn flat to="/" active-class="green--text headline"><b>Home</b></v-btn>
        <v-btn flat to="/signup" active-class="green--text headline"
          ><b>SignUp</b></v-btn
        >
        <!--<v-btn @click="changeTheme" depressed small icon>
          <v-icon v-if="goDark == true">fas fa-sun</v-icon>
          <v-icon v-else>fas fa-moon</v-icon>
        </v-btn>-->
      </v-toolbar-items>

      <v-toolbar-items class="hidden-sm-and-down" v-else>
        <v-btn flat to="/feed" active-class="green--text headline"><b>Feed</b></v-btn>

        <v-btn flat to="/myteam" active-class="green--text headline"
          ><b>MyTeam</b></v-btn
        >
        <v-btn flat to="/challenge" active-class="green--text headline"
          ><b>Record</b></v-btn
        >
        <v-btn
          v-if="this.waitingReqests && this.waitingReqests != 0"
          flat
          to="/myfeed"
          active-class="green--text headline"
        >
          <v-badge color="red" overlab>
            <template v-slot:badge>
              <span class="badge">{{ waitingReqests.length }}</span>
            </template>
            <span><b>MyPAGE</b></span>
          </v-badge>
        </v-btn>
        <v-btn v-else flat to="/myfeed" active-class="green--text headline"
          ><b>MyPAGE</b></v-btn
        >

        <v-btn flat to="/payhome" active-class="green--text headline"
          ><b>PREMIUM</b></v-btn
        >

        <v-btn flat to="/" active-class="" @click.prevent="onClickLogout"
          ><b>Logout</b></v-btn
        >

        <!--<v-btn @click="changeTheme" depressed small icon>
          <v-icon v-if="goDark == true">fas fa-sun</v-icon>
          <v-icon v-else>fas fa-moon</v-icon>
        </v-btn>-->
      </v-toolbar-items>
    </v-toolbar>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  props: {
    goDark: {
      type: Boolean
    }
  },
  data() {
    return {
      drawer: null
    };
  },
  computed: {
    waitingReqests: function() {
      if (
        !$.isEmptyObject(this.managingTeam) &&
        this.managingTeam.memberId == this.memberInfo.memberId
      ) {
        return this.joinRequests.filter(i => i.status.includes("WAITING"));
      }
    },
    ...mapState(["memberInfo", "isLogin", "managingTeam", "joinRequests"])
  },
  created() {
    console.log(this.waitingReqests);
  },
  methods: {
    changeTheme() {
      this.$emit("changeTheme", this.goDark);
    },
    onClickLogout() {
      let checkLogout = confirm("로그아웃 하시겠습니까?");
      console.log(checkLogout);
      if(checkLogout){
      this.$store
        .dispatch("LOGOUT")
        .then(() => {
          if (this.$route.path !== "/") this.$router.replace("/");
        })
        .catch(() => {
          console.log("로그아웃 에러입니다.");
        });
      }
    }
  }
};
</script>

<style scope>
.badge {
  text-align: center;
  text-justify: center;
}
</style>

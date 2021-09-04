<template>
  <v-container>
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
    <v-layout align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
        <span
          ><b>{{ selectTeam.name.replaceAll('"', "") }}</b></span
        >
      </v-toolbar-title>
      <br /><br /><br /><br />
    </v-layout>

    <v-layout justify-end data-aos="fade-right">
      <v-toolbar-title class="headline">
        <span class="text-xs-center mb-4 pb-2" style="color:green"
          ><b>팀장 &nbsp;</b></span
        >
        <span class="text-xs-center mb-4 pb-2"
          ><b>{{ selectTeam.leader.replaceAll('"', "") }}</b></span
        >
      </v-toolbar-title>
    </v-layout>

    <v-layout row column justify-center class="mt-4 pt-2">
      <div v-if="selectTeam.photoDto === null">
        <v-img
          :src="thumbnail1"
          aspect-ratio="2.75"
          height="330"
          contain
        ></v-img>
      </div>
      <div v-else>
        <v-img
          :src="selectTeam.photoDto.filePath"
          aspect-ratio="2.75"
          height="330"
          contain
          data-aos="fade-up"
        ></v-img>
      </div>
      <v-layout column justify-center align-center class="mt-4 pt-2">
        <v-flex wrap justify-center align-center class="textbox">
          <p>
            <b>{{ selectTeam.introduction.replaceAll('"', "") }}</b>
          </p>
        </v-flex>
        <template>
          <v-card>
            <v-card-title>
              <v-text-field
                v-model="search"
                append-icon="search"
                label="Search"
                single-line
                hide-details
              ></v-text-field>
            </v-card-title>
            <v-data-table
              :headers="headers"
              :items="this.selectTeamMembers"
              item-key="member.memberId"
              hide-actions
              :pagination.sync="pagination"
              :search="search"
              tr:hover
              class="elevation-1"
            >
              <template v-slot:items="props">
                <tr
                  style="cursor:pointer;"
                  @click="sendMemberId(props.item.member)"
                >
                  <td class="text-xs-right">
                    <img
                      v-bind:src="props.item.member.photo.filePath"
                      style="border-radius: 50%;"
                      width="30"
                      height="30"
                    />
                  </td>
                  <td class="text-xs-right">
                    <v-chip :color="getColor(props.item.member.point)" dark>
                      {{ props.item.member.name }}
                    </v-chip>
                  </td>
                  <td class="text-xs-right">
                    <v-chip :color="getColor(props.item.member.point)" dark>
                      {{ props.item.member.point }}
                    </v-chip>
                  </td>
                </tr>
              </template>
            </v-data-table>
            <div class="text-xs-center pt-2">
              <v-pagination
                v-model="pagination.page"
                :length="pages"
              ></v-pagination>
            </div>
          </v-card>
        </template>
        <br />
        <v-btn
          v-if="
            selectTeam.memberId != memberInfo.memberId && teamcheck === false
          "
          @click="join"
          type="button"
          color="green"
          class="white--text"
          >가입하기
        </v-btn>
        <v-btn
          v-else
          @click="leave"
          type="button"
          color="red"
          class="white--text"
          >팀탈퇴
        </v-btn>
      </v-layout>
      <v-btn
        v-if="selectTeam.memberId != memberInfo.memberId && teamcheck === false"
        color="secondary"
        @click="join"
        elevation="7"
        fab
        large
        x-large
        x-small
        class="join"
        ><i class="fas fa-sign-in-alt fa-2x"></i>
      </v-btn>
      <br />
      <br />
      <v-layout>
        <v-btn large flat to="/teamlist" class="green--text">
          <v-icon>arrow_back</v-icon>Back to Teamlist
        </v-btn>
      
      </v-layout>
    </v-layout>
  </v-container>
</template>

<script>
import { mapGetters } from "vuex";
import TeamHeader from "@/components/TeamHeader.vue";
import { createInstance } from "@/api/teamindex.js";
import thumbnail1 from "@/assets/images/thumbnail.jpg";
import TeamHeader2 from "../../components/TeamHeader2.vue";

export default {
  name: "TeamMain",
  computed: {
    ...mapGetters([
      "yourInfo",
      "selectTeam",
      "memberInfo",
      "myTeamList",
      "team_challenges",
      "team_challenging",
      "managingTeam",
      "selectTeamMembers"
    ]),
    pages() {
      if (
        this.pagination.rowsPerPage == null ||
        this.pagination.totalItems == null
      )
        return 0;

      return Math.ceil(
        this.pagination.totalItems / this.pagination.rowsPerPage
      );
    }
  },
  mounted() {},
  mounted() {},
  created() {
    this.$store.dispatch("GET_MY_TEAM_INFO", this.memberInfo.memberId);
    this.teamchecking();
    this.$store.dispatch("GET_TEAMCHALLENGE_INFO", this.memberInfo.memberId);
    const token = {
      memberId: this.memberInfo.memberId,
      teamId: this.selectTeam.teamId
    };
    this.$store.dispatch("GET_TEAMCHALLENGER_INFO", token);
    this.$store.dispatch("getSelectTeamMembers", this.selectTeam.teamId);
    this.feedget.memberId = this.memberInfo.memberId;
    this.feedget.page = 0;
    this.$store.dispatch("getFeeds", this.feedget);
  },
  data() {
    return {
      teamcheck: false,
      thumbnail1: thumbnail1,
      pagination: {},
      search: "",
      nameColor: "#8B4513",
      headers: [
        {
          text: "프로필",
          value: "member.photo.filePath",
          width: "50",
          sortable: false,
          align: "center"
        },
        { text: "이름", value: "member.name", width: "200", align: "center" },
        { text: "포인트", value: "member.point", width: "200", align: "center" }
      ],
      feedget: {
        memberId: "",
        page: ""
      }
    };
  },
  components: {
    TeamHeader,
    TeamHeader2
  },
  methods: {
    join() {
      const instance = createInstance();
      instance
        .post("/request", {
          member: {
            memberId: this.memberInfo.memberId
          },
          team: {
            teamId: this.selectTeam.teamId
          }
        })
        .then(response => {
          console.log(response);
          if (response.status === 200) {
            alert("가입 요청 완료");
          }
        })
        .catch(() => {
          alert("이미 가입된 요청입니다.");
        });
    },
    moveMain() {
      this.$router.push("/teammain");
    },
    moveTeamChallenge() {
      this.$store.dispatch("GET_TEAMCHALLENGE_INFO", this.memberInfo.memberId);
      this.$router.push("/teamChallenge");
    },
    moveBoard() {
      this.$router.push("/board");
    },
    moveChattingRoom() {
      this.$router.push("/chattingroom");
    },
    moveTeamFeed() {
      this.$router.push("/teamFeed");
    },
    teamchecking() {
      for (let i = 0; i < this.myTeamList.length; i++) {
        if (this.myTeamList[i].value.teamId === this.selectTeam.teamId) {
          this.teamcheck = true;
          break;
        }
      }
    },
    sendMemberId(member) {
      console.log("sendMemberId");
      console.log(member.email);
      this.$store.dispatch("getMemberByEmail", member.email);
      this.$store.dispatch("getYourFeeds", member.memberId);
      this.$router.push("/memberdetail");
    },
    getColor(point) {
      if (point >= 100) return "#9400D3";
      else if (point >= 75) return "#7AD7BE";
      else if (point >= 50) return "#FFA500";
      else if (point >= 25) return "#52478B";
      else return "#8B4513";
    },
    leave() {
      if (this.selectTeam.memberId == this.memberInfo.memberId) {
        alert("팀장을 넘겨주세요:)");
      } else {
        const instance = createInstance();
        instance
          .delete(
            "/jointeam?memberId=" +
              this.memberInfo.memberId +
              "&teamId=" +
              this.selectTeam.teamId
          )
          .then(response => {
            if (response.data.message === "success") {
              this.$store.dispatch(
                "GET_MY_TEAM_INFO",
                this.memberInfo.memberId
              );
              alert("팀 탈퇴가 완료되었습니다.");
              this.$router.push("/feed");
              console.log(response);
            } else {
              alert("팀 탈퇴 실패하였습니다.");
              console.log(response);
            }
          })
          .catch();
      }
    },
    check() {
      console.log(this.selectTeam);
    }
  }
};
</script>

<style scoped>
.join {
  position: absolute;
  right: 0px;
  bottom: 0px;
}
.textbox {
  padding: 0px 150px;
}
.birthday {
  position: relative;
  right: 0px;
}
.elevation-1 {
  table-layout: flex;
}
</style>

<template>
  <div>
    
    
    <template>
      <v-container style="padding-bottom: 0px;" fluid grid-list-md>
        <v-data-iterator
          :items="this.waitingReqests"
          :rows-per-page-items="rowsPerPageItems"
          :pagination.sync="pagination"
          content-tag="v-layout"
          hide-default-footer
          no-data-text="가입 요청이 존재하지 않습니다"
          row
          wrap
        >
          <template v-slot:header>
            <v-toolbar
              dark
              color="green darken-3"
              class="mb-1"
            >
              <v-toolbar-title>가입신청 목록</v-toolbar-title>
            </v-toolbar>
          </template>
          <template v-slot:item="props">
            <v-flex
              xs12
              sm6
              md4
              lg3
            >
              <v-card>
                <v-card-title class="align-center justify-center subheading font-weight-bold" style="padding-bottom: 0px"  @click="sendMemberId(props.item.member)">
                  <div>
                    <img v-bind:src="props.item.member.photo.filePath" class="profile"  style="border-radius: 50%;"
                        width="40"
                        height="40"/>
                  </div>
                  &nbsp;&nbsp;{{ props.item.member.name }} 님
                </v-card-title>
                <v-divider></v-divider>
                <v-list dense>
                  <v-list-tile>
                    <v-list-tile-content class="align-center">
                      <b-button variant="primary" v-on:click="acceptRequest(props.item.requestId, props.item.member.memberId)">가입요청 수락</b-button>
                    </v-list-tile-content>
                  </v-list-tile>

                  <v-list-tile>
                    <v-list-tile-content class="align-center">
                      <b-button variant="danger" v-on:click="rejectRequest(props.item.requestId)">가입요청 거절</b-button>
                    </v-list-tile-content>
                  </v-list-tile>
                </v-list>
              </v-card>
            </v-flex>
          </template>
          
        </v-data-iterator>
      </v-container>
    </template>

  </div>
</template>

<script>
import { mapState } from "vuex";
import { createInstance } from "@/api/teamindex.js";

export default {
  name: "JoinRequest",
  data() {
    return {
      rowsPerPageItems: [4, 8, 12],
      pagination: {
        rowsPerPage: 4
      },
      items: [
        {
          name: 'member.name',
        },
      ],
    }
  },
  computed: {
    waitingReqests: function() {
      return this.joinRequests.filter(i => i.status.includes("WAITING"));
    },
    ...mapState(["managingTeam", "joinRequests", "memberInfo"]),
    numberOfPages () {
      return Math.ceil(this.items.length / this.itemsPerPage)
    },
  },
  mounted() {
    this.$nextTick(function() {
    });
  },
  methods: {
    rejectRequest: function(requestId) {
      const instance = createInstance();
      instance
        .put("/request/reject/" + requestId)
        .then(() =>
          this.$store.dispatch("getRequests", this.managingTeam.teamId)
        );
    },
    acceptRequest: function(requestId, memberId) {
      const instance = createInstance();
      instance
        .put("/request/accept/" + requestId)
        .then(() =>{
            this.$store.dispatch("getRequests", this.managingTeam.teamId);
            this.$store.dispatch("getTeamMembers", this.managingTeam.teamId);
            alert("요청이 승인되었습니다. 잠시 뒤 반영됩니다.");
          }
        );
      let joinTeam = {
        member: {
          memberId: this.memberInfo.memberId
        },
        team: {
          teamId: this.managingTeam.teamId
        }
      };
    },
    sendMemberId(member) {
      this.$store.dispatch("getMemberByEmail", member.email);
      this.$store.dispatch("getYourFeeds", member.memberId);
      this.$router.push("/memberdetail");
    },
  }
};
</script>

<style scope>

</style>

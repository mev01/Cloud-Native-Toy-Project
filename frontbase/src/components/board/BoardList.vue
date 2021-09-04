<template>
  <b-container>
      <v-layout >
      <v-bottom-navigation
        class="mx-auto"
        shift
        x-large
      >
      <team-header />
      </v-bottom-navigation>
    </v-layout>

    <br>
    <br>

      <v-layout column justify-center class="mt-4 pt-2">
        <b-row class="mt-5">
            <b-col class="text-left">
                <b-input-group class="mb-2">
                    <b-input-group-prepend is-text>
                        <b-icon icon="search"></b-icon>
                    </b-input-group-prepend>
                    <b-input type="text" v-model.lazy="word" placeholder="제목으로 검색" />
                </b-input-group>
            </b-col>
            <b-col class="text-right">
                <b-button pill class="btn-main" @click="check()">글쓰기</b-button>
            </b-col>
        </b-row>
        <div v-if="noticeItems" class="text-center">
            <b-table
                style="cursor:pointer;"
                hover
                id="list_table"
                :items="filtered"
                :per-page="perPage"
                :current-page="currentPage"
                :fields="fields"
                :sort-by.sync="sortBy"
                :sort-desc.sync="sortDesc"
                @row-clicked="moveDetail"
            >
            </b-table>
            <b-pagination
                align="center"
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
                aria-controls="list_table"
            ></b-pagination>
        </div>
        <div v-else>글이 없습니다.</div>
        <b-button id="up" @click="pageUp">▲</b-button>
        </v-layout>
    </b-container>
</template>

<script>
import { mapGetters } from "vuex";
import ListRow from "@/components/board/include/ListRow.vue";
import TeamHeader from '@/components/TeamHeader.vue';
import moment from 'moment';

export default {
  name: "board",
  components: {
    ListRow,
    TeamHeader
  },
   data: function () {
        return {
            word: "",
            perPage: 10,
            currentPage: 1,
            sortBy: 'boardid',
            sortDesc: true,

            fields: [
                { key: "boardId", label: "번호", thStyle: { width: "10%" }, sortable:true},
                { key: "writer", label: "작성자", thStyle: { width: "20%" } },
                { key: "title", label: "제목", thStyle: { width: "50%" } },
                {
                    key: "writeDate",
                    label: "작성일",
                    thStyle: { width: "20%" },
                    formatter: "getFormatDate",
                    sortable: true,
                },
            ],
        };
    },
    computed: {
        ...mapGetters(["noticeItems","selectTeam","memberInfo"]),
        filtered: function () {
            var stitle = this.word.trim();
            return this.noticeItems.filter(function (item) {
                if (item.title.indexOf(stitle) > -1) {
                    return true;
                }
            });
        },
        rows() {
            return this.filtered.length;
        },
    },
    created() {
        console.log(this.selectTeam);
        console.log(this.memberInfo.memberId);
        this.$store.dispatch("getNoticeItems",this.selectTeam.teamId);
    },
    methods: {
        movePage() {
            this.$router.push("/board/create");
        },
        search(e) {
            this.word = e.target.value;
        },
        pageUp() {
            window.scrollTo(0, 100);
        },
        moveDetail(e) {
            //console.log(this);
            // console.log(e);
            this.$router.push(`/board/view?boardId=` + e.boardId);
        },
        getFormatDate(regtime) {
            return moment(new Date(regtime)).format("YYYY년 MM월 DD일");
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
        },
        check(){
            if(this.memberInfo.memberId !== this.selectTeam.memberId){
                alert("팀 임원이 아닙니다.");
            }else{
                this.movePage();
            }
        }
    },
  // methods: {
  //   movePage() {
  //     this.$router.push({ name: "book-create" });
  //   },
  //   moveMain(){
  //     this.$router.push("/teammain");
  //   },
  //   moveTeamChallenge(){
  //     this.$router.push("/teamChallenge");
  //   },
  //   moveBoard(){
  //     this.$router.push("/board");
  //   },
  //   moveChattingRoom(){
  //     this.$router.push("/chattingroom");
  //   },
  //   moveTeamFeed(){
  //     this.$router.push("/teamFeed");
  //   }
  // }
};
</script>

<style scoped>
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

#up {
    position: fixed;
    right: 50px;
    bottom: 80px;
    opacity: 0.5;
    border-radius: 100%;
}
#list_table th {
    background-color: #5a7d67;
    color: white;
}
.btn-main {
    background-color: #5a7d67 !important;
}
.page-item button {
    color: #5a7d67;
}
.page-item.active button {
    background-color: #5a7d67 !important;
    border-color: #5a7d67 !important;
}
.page-link {
    font-family: Arial, Helvetica, sans-serif !important;
}
#noticeTitle {
    font-weight: bold;
    margin-top: 72px;
}
</style>

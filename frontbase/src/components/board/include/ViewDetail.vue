<template>
   <b-container align="center">
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
            <div class="text-center mb-3" v-if="boardtype == 'notice'">
                <b-col class="text-right mt-5">
                    <router-link :to="'/board/modify?boardId=' + boardId"
                        ><b-button size="sm" class="m-1" :disabled="this.memberInfo.memberId !== this.selectTeam.memberId">수정</b-button></router-link
                    >
                    <b-button variant="outline-danger" size="sm" class="m-1" @click="deleteBoard" :disabled="this.memberInfo.memberId !== this.selectTeam.memberId"
                        >삭제</b-button
                    >
                </b-col>
            </div>
        <table class="table">
            <colgroup>
                <col style="width: 10%" />
                <col style="width: 70%" />
                <col style="width: 20%" />
            </colgroup>
            <tr>
                <th colspan="3" class="text-center view-title" style="color:green">{{ title }}</th>
            </tr>
            <tr>
                <th style="color:green">게시자</th>
                <td style="color:green">{{ writer }}</td>
                <td style="color:green">{{ getFormatDate(writeDate) }}</td>
            </tr>
            <tr>
                <td colspan="3" class="content-row" v-html="enterToBr(contents)" style="color:green"></td>
            </tr>
        </table>
        <hr />

        <b-row>
            <b-col v-if="boardtype == 'notice'">
                <router-link to="/board"
                    ><b-button squared class="shadow m-1 gotoListBtn">목록</b-button></router-link
                >
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
import { mapGetters } from "vuex";
// import http from "@/util/http-common";
import { createInstance } from "@/api/teamindex.js";
import TeamHeader from '@/components/TeamHeader.vue';
import moment from 'moment';

export default {
  name: "viewdetail",
  components: {
    TeamHeader
  },
  computed:{
    ...mapGetters(["selectTeam","memberInfo"]),
  },
  props: {
        boardtype: { type: String },
        boardId: { type: Number },
        writer: { type: String },
        title: { type: String },
        contents: { type: String },
        writeDate: { type: String }
    },
  methods: {
    getFormatDate(writeDate) {
      return moment(new Date(writeDate)).format('YYYY년MM월DD일 HH시 mm분');
    },
   deleteBoard() {
            const instance = createInstance();
            if (confirm("삭제하시겠습니까?")) {
                    instance.delete(`/board/${this.boardId}`).then(({ data }) => {
                        let msg = "삭제 처리시 문제가 발생했습니다.";
                        if (data.data === "success") {
                            msg = "삭제가 완료되었습니다.";
                        }
                        alert(msg);
                        this.$router.push("/board");
                    });

                }
            },

    numberWithCommas(x) {
      if (x) return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    enterToBr(str) {
      if (str) return str.replace(/(?:\r\n|\r|\n)/g, "<br />");
    }
  }
};
</script>

<style scoped>
.regist {
  padding: 10px;
}
.regist_form {
  text-align: left;
  border-radius: 5px;
  background-color: white;
  padding: 20px;
}
input,
textarea,
.view {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  color: #787878;
  font-size: medium;
}

.table {
    border-top: 1px solid black !important;
}
.content-row {
    height: 200px;
}
.answer-row {
    height: 150px;
}
.gotoListBtn {
    background-color: #5a7d67 !important;
    color: white;
    width: 150px;
    height: 50px;
}
.view-title {
    border-top: 2px;
    height: 70px;
    font-size: 25px;
}
</style>

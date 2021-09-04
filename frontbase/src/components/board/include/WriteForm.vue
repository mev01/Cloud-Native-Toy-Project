<template>
  <div>
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
            <b-card bg-variant="light">
                <b-form-group
                    label-cols-lg="3"
                    label="✏입력"
                    label-size="lg"
                    label-class="font-weight-bold pt-0"
                    class="mb-0 mt-2 ml-3"
                >
                    <b-form-group>
                        <label for="userId">작성자</label>
                        <input
                            type="text"
                            class="form-control"
                            ref="writer"
                            v-bind:value="memberInfo.name"
                            readonly
                        />
                    </b-form-group>
                    <b-form-group>
                        <label for="title">제목</label>
                        <input
                            type="text"
                            class="form-control"
                            ref="title"
                            placeholder="제목을 입력하세요"
                            v-model="title"
                        />
                    </b-form-group>
                    <b-form-group>
                        <label for="content">내용</label>
                        <b-textarea
                            type="text"
                            class="form-control"
                            ref="contents"
                            placeholder="내용을 입력하세요"
                            v-model="contents"
                            rows="5"
                        ></b-textarea>
                    </b-form-group>
                    <div class="text-right">
                        <b-button
                            variant="success"
                            class="mr-2"
                            v-if="type == 'create'"
                            @click="checkHandler"
                            :disabled="this.memberInfo.memberId !== this.selectTeam.memberId"
                            >등록</b-button
                        >
                        <b-button variant="success" class="mr-2" v-else @click="checkHandler"
                            :disabled="this.memberInfo.memberId !== this.selectTeam.memberId"
                            >수정</b-button
                        >
                        <b-button @click="moveList">목록</b-button>
                    </div>
                </b-form-group>
            </b-card>
        </b-container>
    </div>
</template>

<script>
// import http from "@/util/http-common";
import { createInstance } from "@/api/teamindex.js";
import { mapGetters } from "vuex";
import TeamHeader from '@/components/TeamHeader.vue';
export default {
  name: "writeform",
  components: {
    TeamHeader
  },
  props: {
    type: { type: String }
  },
  props: {
        type: { type: String },
        boardtype: { boardtype: String },
    },
    data: function () {
        return {
            boardT: "",
            boardId: Number(),
            writeDate: "",
            writer: "",
            title: "",
            contents: "",
            teamId: Number(),
        };
    },
    computed: {
        ...mapGetters(["noticeItem", "memberInfo","selectTeam"]),
    },
    methods: {
        checkHandler() {
            this.writer = this.memberInfo.name;
            let err = true;
            let msg = "";
            !this.writer &&
                ((msg = "작성자를 입력해주세요"), (err = false), this.$refs.writer.focus());
            err &&
                !this.title &&
                ((msg = "제목 입력해주세요"), (err = false), this.$refs.title.focus());
            err &&
                !this.contents &&
                ((msg = "내용 입력해주세요"), (err = false), this.$refs.contents.focus());

            if (!err) alert(msg);
            else {
                this.type == "create" ? this.createHandler() : this.updateHandler();
            }
        },
        createHandler() {
            // 공지사항 글쓰기
            const instance = createInstance(); 
            this.teamId = this.selectTeam.teamId;
                instance.post("/board", {
                    teamId: this.teamId,
                    title: this.title,
                    contents: this.contents,
                    writer:this.writer,
                })
                    .then(({ data }) => {
                        let msg = "등록 처리시 문제가 발생했습니다.";
                        //console.log(data);
                        if (data.data === "success") {
                            msg = "등록이 완료되었습니다.";
                        }
                        alert(msg);
                        this.moveList();
                    })
                    .catch(() => {
                        alert("등록 처리시 에러가 발생했습니다.");
                    });

        },
        updateHandler() {
            // 공지사항 글수정
            const instance = createInstance(); 
            console.log(this.boardId);
                //console.log("공지사항글수정");
                instance.put(`/board`, {
                    boardId: this.boardId,
                    writer: this.writer,
                    title: this.title,
                    contents: this.contents,
                    teamId:this.selectTeam.teamId
                })
                    .then(({ data }) => {
                        let msg = "수정 처리시 문제가 발생했습니다.";
                        if (data.data === "success") {
                            msg = "수정이 완료되었습니다.";
                        }
                        alert(msg);
                        this.moveList();
                    })
                    .catch(() => {
                        alert("수정 처리시 에러가 발생했습니다.");
                    });
            
        },
        moveList() {
            //console.log(this.boardtype);
            this.$router.push("/board");
        },
    },
  created() {
    if (this.boardtype == "notice") this.boardT = "board";

    console.log(this.type);
    if (this.type === "modify") {
        // 공지사항 게시글 하나 불러오기
            //console.log("공지사항 하나 get");
            const instance = createInstance();
            instance.get(`/board/${this.$route.query.boardId}`)
                .then(({ data }) => {
                    this.boardId = data.object.boardId;
                    this.writeDate = data.object.writeDate;
                    this.writer = data.object.writer;
                    this.title = data.object.title;
                    this.contents = data.object.contents;
                })
                .catch(() => {
                    alert("에러가 발생했습니다.");
                });

    }
  },
};
</script>

<style scoped>

.createFormTitle {
    font-weight: bold;
    margin-bottom: 60px;
}
.regist {
  padding: 10px;
}
.regist_form {
  text-align: left;
  border-radius: 5px;
  background-color: #f2f2f2;
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
</style>

<template>
  <div class="wrap">
    <div class="comments">
      <comment-item
        v-for="(comment, idx) in comments"
        :key="idx"
        :comment="comment"
      ></comment-item>
    </div>
    <div class="writeC">
      <input
        type="text"
        id="writeC"
        v-model="inputC"
        placeholder="댓글달기..."
        @keyup.enter="writeComment()"
      />
      <div @click="writeComment" class="writeC_btn">작성</div>
    </div>
  </div>
</template>
<script>
import "../../components/css/feed/comment.scss";
import { createInstance } from "@/api/teamindex.js";
import CommentItem from "@/views/Feed/CommentItem.vue";
import { mapGetters } from "vuex";

export default {
  data: () => {
    return {
      inputC: "",
      comment: ""
    };
  },
  components: {
    CommentItem
  },
  computed: {
    ...mapGetters(["comments", "memberInfo", "feedid"])
  },
  methods: {
    writeComment() {
      const instance = createInstance();
      const body = {
        parentId: 0,
        feedId: this.feedid,
        memberId: this.memberInfo.memberId,
        contents: this.inputC,
        className: 1,
        order: 1
      };
      console.log(JSON.stringify(body));
      instance
        .post("/comment/", JSON.stringify(body))
        .then(response => {
          if (response.data.data === "success") {
            this.inputC = "";
            this.$store.dispatch("GET_COMMENTS", this.feedid);
          } else {
          }
        })
        .catch();
    }
  }
};
</script>

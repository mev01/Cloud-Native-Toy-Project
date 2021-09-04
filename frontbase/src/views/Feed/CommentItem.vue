<template>
  <div class="comment">
    
    <div>
    <img v-bind:src="comment.member.photo.filePath" class="profile"  style="border-radius: 50%;"
        width="40"
        height="40"/>
    </div>
    <div class="co_wrap">
      <div class="content">
        <div class="writer">{{ comment.member.name }}</div>
        <div class="content_btn_wrap">
          <div class="desc">{{ comment.contents }}</div>
          <span @click="makeCom" class="coco_btn">
            답글달기
          </span>
        </div>
      </div>
      <div v-if="this.parentId == null"></div>
      <div v-else class="writeCom">
        <input
          type="text"
          v-model="inputCom"
          class="inputSpace"
          @keyup.enter="writeComment()"
        />
        <div @click="writeComment" class="write_comment"></div>
      </div>
      <div class="reCom">
        <inner-item
          v-for="(inner, idx) in comment.coComments"
          :key="idx"
          :inner="inner"
        ></inner-item>
      </div>
    </div>
  </div>
</template>

<script>
import InnerItem from "@/views/Feed/InnerItem.vue";
import { mapGetters } from "vuex";
import { createInstance } from "@/api/teamindex.js";

export default {
  props: ["comment"],
  data: () => {
    return {
      parentId: null,
      inputCom: "",
      showTemp: false
    };
  },
  components: {
    InnerItem
  },
  computed: {
    ...mapGetters(["comments", "memberInfo", "feedid"])
  },
  methods: {
    makeCom() {
      if (!this.showTemp) {
        this.showTemp = true;
        this.parentId = this.comment.commentId;
      } else {
        this.showTemp = false;
        this.parentId = null;
      }
    },
    writeComment() {
      const instance = createInstance();
      const body = {
        parentId: this.parentId,
        feedId: this.feedid,
        memberId: this.memberInfo.memberId,
        contents: this.inputCom,
        className: 1,
        order: 1
      };
      console.log(JSON.stringify(body));
      instance
        .post("/comment/", JSON.stringify(body))
        .then(response => {
          if (response.data.data === "success") {
            this.parentId = null;
            this.inputCom = "";
            this.$store.dispatch("GET_COMMENTS", this.feedid);
          } else {
            alert("대댓글 등록 실패");
          }
        })
        .catch();
    }
  }
};
</script>

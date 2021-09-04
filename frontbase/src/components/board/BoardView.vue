<template>
  <div>
        <view-detail
            boardtype="notice"
            :boardId="noticeItem.boardId"
            :writer="noticeItem.writer"
            :title="noticeItem.title"
            :writeDate="noticeItem.writeDate"
            :contents="noticeItem.contents"
        />
    </div>
</template>

<script>
import ViewDetail from "@/components/board/include/ViewDetail.vue";
import { mapGetters } from "vuex";
export default {
  name: "boardview",
  components: {
    ViewDetail
  },
  computed: {
        ...mapGetters(["noticeItem"]),
    },
  created() {
    this.$store.dispatch("getNoticeItem", `/${this.$route.query.boardId}`);
  }
};
</script>

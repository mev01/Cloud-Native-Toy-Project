<template>
  <div>
    <v-btn @click="writeFeed">글쓰기</v-btn>
    <div>정보 | 피드 | 공지사항 | 채팅 | 챌린지</div>
  </div>
</template>

<
<script>
export default {
  methods: {
    writeFeed() {
      this.$router.push("/writefeed");
    }
  }
};
</script>

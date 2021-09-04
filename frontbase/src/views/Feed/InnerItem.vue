<template>
  <div class="mini_wrap">
    <img v-bind:src="inner.member.photo.filePath" class="mini_profile" />
    <div class="mini_writer">{{ inner.member.name }}</div>
    <div class="mini_contents">{{ inner.contents }}</div>
  </div>
</template>

<script>
export default {
  props: ["inner"],
  data: () => {
    return {};
  },
  created() {
    console.log(this.inner);
  }
};
</script>

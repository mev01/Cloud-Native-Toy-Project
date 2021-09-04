<template>
  <div>
    <write-form  type ="create" boardtype="notice"/>
  </div>
</template>

<script>
import WriteForm from "@/components/board/include/WriteForm.vue";

export default {
  name: "boardcreate",
  components: {
    WriteForm
  }
};
</script>

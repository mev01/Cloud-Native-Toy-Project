<template>
  <b-tr class="list-tr">
    <b-td v-if="boardtype=='notice'">{{ boardId }}</b-td>
    <b-td>{{ writer }}</b-td>
    <b-td v-if="boardtype == 'notice'">
      <router-link :to="`/notice-view?boardid=${boardId}`" boardtype="notice" class="list-title">{{ title }}</router-link>
    </b-td>
    <b-td>{{ getFormatDate(writeDate) }}</b-td>
    <!-- <b-td v-if="ansYes">Y</b-td>
    <b-td v-else></b-td> -->
  </b-tr>
</template>

<script>
import moment from 'moment';
export default {
  name: "listrow",
  props: {
    boardtype: String,
    boardid: Number,
    writer: String,
    title: String,
    writeDate: String
  },
  methods: {
    getFormatDate(writeDate) {
      return moment(new Date(writeDate)).format('YYYY년MM월DD일 HH시 mm분');
    },
  },
};
</script>
<style scope>
td {
  text-align: center;
  border-bottom: 1px solid #ddd;
  height: 50px;
}
</style>

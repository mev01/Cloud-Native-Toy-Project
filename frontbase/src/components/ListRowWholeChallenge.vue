<template>
  <tr>
    <td>{{ no }}</td>
    <td>{{ title }}</td>
    <td>{{ contents }}</td>
    <td>{{ endDate }}</td>
  </tr>
</template>

<script>
export default {
  name: "ListRow",
  props: {
    no: Number,
    title: String,
    contents: String,
    endDate: String,
  },
  data() {
    return {
    
    }
  },
};
</script>
<style scope>
td {
  text-align: center;
  border-bottom: 1px solid #ddd;
  height: 30px;
  width: 70px;
}
</style>

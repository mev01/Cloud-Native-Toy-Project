<template>
  <div class>
    <v-container>
    <v-layout column align-center data-aos="fade-left">
    <br><br>
    <a-icon
      v-if="success"
      type="check-circle"
      :style="{ fontSize: '200px', color: '#52c41a' }"
    />
    <a-icon
      v-else
      type="exclamation-circle"
      :style="{ fontSize: '200px', color: '#f5222d' }"
    />
    <br><br>
    <h1>{{ type === 'payment' ? '결제' : '본인인증' }}에 {{ success ? '성공' : '실패'}}하였습니다</h1>
    <ul>
      <li>
        <span>아임포트 번호</span>
        <span>{{ impUid }}</span>
      </li>
      <li>
        <span>주문번호</span>
        <span>{{ merchantUid }}</span>
      </li>
      <li v-if="!success">
        <span>에러 메시지</span>
        <span>{{ errorMessage }}</span>
      </li>
    </ul>
    <a-button size="large" @click="handleGoBack">
      뒤로가기
    </a-button>
    </v-layout>
    </v-container>
  </div>
</template>

<script>
import { createInstance } from "@/api/teamindex.js";
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["memberInfo"])
  },
  mounted(){
    if(this.success==true){
          const instance = createInstance();
          console.log(this.memberInfo.email);
          instance.get("/member/"+this.memberInfo.email)
          .then(response => {
            console.log(response);
            if(response.data.message=="success"){
              this.$store.commit("SET_AUTHENTICATED", true);
            }
          }).catch(error =>{
            console.log(error);
          })
      }
  },
  data() {
    const { query } = this.$router.history.current;
    const { type } = query;
    return {
      type,
      success: this.getSuccess(query),
      impUid: query.imp_uid,
      merchantUid: query.merchant_uid,
      errorMessage: `[${query.error_code}] ${query.error_msg}`,
    
    };
  },
  methods: {
    getSuccess(query) {
      const { success } = query;
      const impSuccess = query.imp_success;
      if (impSuccess === undefined) {
        if (typeof success === 'string') {
          return success === 'true';
        }
        return success;
      }
      if (typeof impSuccess === 'string') {

        return impSuccess === 'true';
      }
    
      
      console.log("impSuccess: "+impSuccess);
      return impSuccess;
    },
    handleGoBack() {
      this.$router.push(`/feed`);
    },
  },
};
</script>

<style lang="less">

</style>

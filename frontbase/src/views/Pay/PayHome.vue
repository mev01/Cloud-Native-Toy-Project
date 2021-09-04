<template>
  <div>
    <div class="feed_t">
  <v-container>
    <v-layout justify-center align-center data-aos="fade-up">
      <v-toolbar-title class="headline">
          <h1><b>프리미엄</b><span class="green--text"><b>&nbsp;서비스</b></span></h1>
          
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>

    <v-layout justify-center align-center data-aos="fade-left">
      <v-toolbar-title class="headline">
          <span><b>당신이 누릴</b></span>
          <span class="green--text"><b>&nbsp;최고의 혜택</b></span>
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>

    <br>
    <v-layout align-center column>
      <v-flex  v-for="portfolio in portfolios" :key="portfolio.src"  class="feed-item">
        <v-img :src="portfolio.src"  width="440" margin auto contain data-aos="fade-right"></v-img>
        <v-card-title primary-title class="justify-center">
          <h4 data-aos="fade-left"><b>{{portfolio.title}}</b></h4>
        </v-card-title>
      </v-flex>
    </v-layout>
    
    <v-flex text-xs-center subheading font-weight-bold data-aos="fade-right"><h2><b>지금 당장 만나보세요</b></h2></v-flex> 
      
      <v-layout align-center data-aos="fade-left">
        <br><br>
          <v-toolbar-title class="headline">
          <span><b></b></span>
          <span class="green--text"><b>&nbsp;</b></span>
          </v-toolbar-title>
      </v-layout>

      <v-layout justify-center align-center data-aos="fade-top">
        <v-btn @click="goPay()" large color="primary">
            <a-icon type="credit-card" />
            결제
        </v-btn>
      </v-layout>
  
  </v-container>
  </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  
  computed: {
    ...mapGetters(["memberInfo"]),
  },
  data() {
	  return {
      portfolios: [
        {
          src: "https://i.ibb.co/mv9Szc8/image.jpg",
          title: "성격적으로 안맞아 팀원끼리 싸운 경험 없으신가요?"
        },
        {
          src: "https://i.ibb.co/MS9mMSr/mbti-2.jpg",
          title: "저희가 MBTI를 통해 해결해드리도록 하겠습니다!"
        },

        {
          src: "https://i.ibb.co/42YTTjL/1.jpg",
          title: "자신에게 맞는 팀을 찾으세요!"
        },

        {
          src: "https://i.ibb.co/cbcRTYY/image.jpg",
          title: "저희가 추천해드리겠습니다!"
        },
        
      ]
    };
	},
  
  methods: {
    goPay(){
      if(!this.memberInfo.authenticated){
        this.$router.push("/payment");
      }else{
        alert("이미 프리미엄 회원입니다.")
      }
    }
  }
};
</script>

<style>


  .nav-container {
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    top: -75px;
    left: 0;
    right: 0;
    
  }


</style>

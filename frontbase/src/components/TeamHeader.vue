<template>
    <div>
        <b-button variant="info" class="mr-2" color="secondary" @click="moveMain">
            <i class="fas fa-address-card fa-2x" ></i>
            <span v-if="this.window.width > 770">&nbsp;&nbsp;&nbsp;정보</span>
        </b-button>

        <b-button variant="success" class="mr-2" color="success" @click="moveTeamFeed">
            <i class="fas fa-clipboard fa-2x" ></i>
            <span v-if="this.window.width > 770">&nbsp;&nbsp;&nbsp;피드</span>
        </b-button>

        <b-button variant="primary" class="mr-2" color="primary" @click="moveBoard">
            <i class="fas fa-check fa-2x" ></i>
            <span v-if="this.window.width > 770">&nbsp;&nbsp;&nbsp;공지사항</span>
        </b-button>

        <b-button variant="warning" class="mr-2" color="warning"  @click="moveChattingRoom">
            <span style="color:white">
                <i class="fas fa-comments fa-2x"></i>
                <span v-if="this.window.width > 770">&nbsp;&nbsp;&nbsp;채팅</span>
            </span>
        </b-button>

        <b-button variant="danger" class=" mr-2" color="error" @click="moveTeamChallenge">
            <i class="fas fa-trophy fa-2x" ></i>
            <span v-if="this.window.width > 770">&nbsp;&nbsp;&nbsp;챌린지</span>
        </b-button>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    
    data() {
        return {
            window: {
                width: 0,
                height: 0
            }
        }
    },
    created() {
        window.addEventListener('resize', this.handleResize);
        this.handleResize();
    },
    destroyed() {
        window.removeEventListener('resize', this.handleResize);
    },
    computed: {
        ...mapGetters(["selectTeam","memberInfo","myTeamList","team_challenges"]),
    },
    methods: {
        handleResize() {
            this.window.width = window.innerWidth;
            this.window.height = window.innerHeight;
        },
        moveMain(){
            console.log("메인으로가자");
            this.$router.push("/teammain");
        },
        moveTeamChallenge(){
            this.$store.dispatch("GET_TEAMCHALLENGE_INFO", this.memberInfo.memberId);
            const token={
                memberId: this.memberInfo.memberId,
                teamId:this.selectTeam.teamId
            };
            this.$store.dispatch("GET_TEAMCHALLENGER_INFO", token);       
            this.$router.push("/teamChallenge");
            
        },
        moveBoard(){
            this.$router.push("/board");
        },
        moveChattingRoom(){
            this.$router.push("/chattingroom");
        },
        moveTeamFeed(){
            this.$router.push("/teamFeed");
        },          
    },
}
</script>

<style scoped>

</style>

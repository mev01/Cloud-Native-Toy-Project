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
        moveTeamFeed(){
            this.$router.push("/teamFeed");
        },          
    },
}
</script>

<style >
</style>
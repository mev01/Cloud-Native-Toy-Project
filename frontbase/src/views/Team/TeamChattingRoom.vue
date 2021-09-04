<template>
    <div class="room">
        <v-layout >
            <v-bottom-navigation
                class="mx-auto"
                shift
                x-large
            >
                <team-header />
            </v-bottom-navigation>
        </v-layout>
        
        <v-layout align-center data-aos="fade-right">
            <v-toolbar-title class="headline">
                <span><b>채팅방</b></span>
            </v-toolbar-title>
            <br><br><br><br>
        </v-layout>
        <hr />
        <div v-for="(m, idx) in msg" :key="idx">
            <div v-bind:class="m.style">
                <div v-if="m.style == 'otherMsg'" class="entete">
                    <span style="margin:3px;font-size:15px">
                    {{ m.senderNickname }}
                    </span>
                    <br>
                    <span class="content">
                        {{m.content}}
                    </span>
                    &nbsp;
                    <span class="date">
                        {{m.writeDate.slice(11, 16)}}
                    </span>
                </div>
                <div v-else class="entete">
                    <span class="date">
                        {{m.writeDate.slice(11, 16)}}
                    </span>
                    &nbsp;
                    <span class="content">
                        {{m.content}}
                    </span>
                </div>
            </div>
        </div>
        <hr />
        <v-layout justify-center align-center wrap class="mt-4 pt-2">
        <input type="text" style="width:240px;" @keyup.enter="sendMessage()" v-model="content" placeholder="보낼 메시지" size="100" />
        <button @click="sendMessage()"> SEND</button>  
        </v-layout>
    </div>
</template>

<script>
import TeamHeader from '@/components/TeamHeader.vue';
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { createInstance, url} from "@/api/teamindex.js";
import { mapState} from "vuex";

export default {
    name: "Room",
    components:{
        TeamHeader
    },
    computed: {
        ...mapState(["memberInfo", "selectTeam"]),
    },
    data: () => {
        return {
                id: -1,
                nickname: '',
                title:'채팅방',
                roomid:-1,
                idx:0,
                msg:[],
                content:"",
                stompClient:null    
            }
    },
    created(){
        const instance = createInstance();

        instance
            .get('/message/'+this.selectTeam.teamId+'?page=0', )
            .then(res=>{
                console.log(res);

                this.msg = []
                let resMsg = res.data.data;
                for(let i=resMsg.length-1; i>-1; i--){
                    let m={
                        'senderNickname':resMsg[i].writer,
                        'content':resMsg[i].contents,
                        'style': resMsg[i].memberId == this.memberInfo.memberId ? 'myMsg':'otherMsg',
                        'writeDate': resMsg[i].writeDate
                    }
                    this.msg.push(m)
                    if(this.msg.length>10){
                        this.msg.shift();
                    }
                }
            }, err=>{
                console.log(err)
                alert("error : 새로고침하세요")
            })

        let socket = new SockJS(url+'/ws')
        this.stompClient = Stomp.over(socket)
        this.stompClient.connect({}, frame=>{
            console.log("success", frame)
            this.stompClient.subscribe("/sub/"+this.selectTeam.teamId, res=>{
                let jsonBody = JSON.parse(res.body)
                let m={
                    'senderNickname':jsonBody.writer,
                    'content':jsonBody.contents,
                    'style': jsonBody.memberId == this.memberInfo.memberId ? 'myMsg':'otherMsg',
                    'writeDate': jsonBody.writeDate
                }
                this.msg.push(m)
                if(this.msg.length>10){
                    this.msg.shift();
                }
            })
        }, err=>{
            console.log("fail", err)
        })
    },
        
    methods:{
        sendMessage(){
            if(this.content.trim() !='' && this.stompClient!=null) {
                let chatMessage = {
                    'teamId': this.selectTeam.teamId,
                    'memberId': this.memberInfo.memberId,
                    'contents': this.content,
                    'writer': this.memberInfo.name,
                }
                this.stompClient.send("/pub/message", JSON.stringify(chatMessage),{})
        
                this.content=''
            }
        }
    }
};
</script>
<style scoped>
.myMsg{
text-align: right;
}
.otherMsg{
    text-align: left;
}
.content{
    font-size: 15;
    margin:5px;
    padding:10px;
    color:#fff;
    line-height:25px;
    max-width:90%;
    display:inline-block;
    text-align:left;
    border-radius:5px;
    background-color:#58b666;
    width: auto;
}
.date{
    font-size: 13px;
}
.entete {
    margin-bottom:5px;
}
</style>

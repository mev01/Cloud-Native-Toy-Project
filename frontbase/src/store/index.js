import Vue from "vue";
import Vuex, { Store } from "vuex";
import jwt_decode from "jwt-decode";
import createPersistedState from "vuex-persistedstate";
import { findById } from "@/api/user.js";
import { createInstance } from "../api/teamindex";
Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  state: {
    isLogin: false, // 로그인 여부
    memberInfo: null,
    teamLists: [], // 모든팀 정보
    feeds: [],
    teamFeeds: [],
    comments: [],
    whole_challenges: [], // 전체 챌린지
    team_challenges: [],
    team: [], // 해당팀 관련
    teamInfo: null,
    myTeamList: [],
    managingTeam: {}, // 내가 팀장으로 있는 팀의 정보
    managingTeamMembers: [], // 내가 팀장으로 있는 팀의 멤버 목록
    book: {},
    books: [],
    joinRequests: [], // managingTeam의 가입 요청 목록
    selectTeam: {},
    feedid: {},
    team_challenging: [], //내가 진행중인 챌린지
    entire_challenge: {}, // 공통 챌린지
    feed_challenging: [], //feed 입력시 넣는 챌린지 목록
    selectTeamMembers: [], //선택한 팀의 멤버 정보

    // 공지사항
    noticeItems: [],
    noticeItem: {},

    oneFeed: null,
    myFeeds: [],

    //TOken
    token: "",

    feedLike: "", // 현재 피드의 좋아요 상태
    likeList: [], // 내가 좋아요한 피드들
    yourFeeds: [],
    yourInfo: null,
    myRecomendTeams:[],
  },

  getters: {
    team_challenges(state) {
      return state.team_challenges;
    },
    whole_challenges(state) {
      return state.whole_challenges;
    },
    myTeamList(state) {
      return state.myTeamList;
    },
    feeds(state) {
      return state.feeds;
    },
    teamFeeds(state) {
      return state.teamFeeds;
    },
    comments(state) {
      return state.comments;
    },
    teamLists(state) {
      return state.teamLists;
    },
    books(state) {
      return state.books;
    },
    book(state) {
      return state.book;
    },
    memberInfo(state) {
      return state.memberInfo;
    },
    yourInfo(state) {
      return state.yourInfo;
    },
    joinRequests(state) {
      return state.joinRequests;
    },
    selectTeam(state) {
      return state.selectTeam;
    },
    feedid(state) {
      return state.feedid;
    },
    team_challenging(state) {
      return state.team_challenging;
    },
    entire_challenge(state) {
      return state.entire_challenge;
    },
    managingTeam(state) {
      return state.managingTeam;
    },
    managingTeamMembers(state) {
      return state.managingTeamMembers;
    },
    feed_challenging(state) {
      return state.feed_challenging;
    },
    noticeItems(state) {
      return state.noticeItems;
    },
    noticeItem(state) {
      return state.noticeItem;
    },
    oneFeed(state) {
      return state.oneFeed;
    },
    myFeeds(state) {
      return state.myFeeds;
    },
    yourFeeds(state) {
      return state.yourFeeds;
    },
    //TOken
    getToken(state) {
      return state.token;
    },
    feedLike(state) {
      return state.feedLike;
    },
    likeList(state) {
      return state.likeList;
    },
    selectTeamMembers(state) {
      return state.selectTeamMembers
    },
    myRecomendTeams(state) {
      return state.myRecomendTeams
    },
  },
  mutations: {
    setIsLogined(state, isLogin) {
      state.isLogin = isLogin;
    },
    SET_AUTHENTICATED(state, authenticated) {
      state.memberInfo.authenticated = authenticated;
    },
    setMemberInfo(state, memberInfo) {
      state.isLogin = true;
      state.memberInfo = null;
      state.memberInfo = memberInfo;
      console.log(memberInfo);
    },
    logout(state) {
      state.isLogin = false;
      state.memberInfo = null;
      state.team_challenging = [];
      state.feed_challenging = [];
      state.whole_challenges = [];
      state.managingTeam = null;
      state.managingTeamMembers = [];
      state.comments= [];
      state.entire_challenge = null;
      state.feedid = {};
      state.joinRequests = [];
      state.myRecomendTeams = [];
      state.noticeItem = [];
      state.oneFeed = null;
      state.selectTeam = null;
      state.selectTeamMembers = [];
      state.teamFeeds = [];
      state.teamLists = [];
      state.team_challenges = [];
      state.yourInfo = null;
      state.myTeamList = [];
      state.myFeeds = [];
      state.feeds = [];
      state.likeList = [];
      //window.localstorage.clear();
    },
    setFeeds(state, data) {
      state.feeds = state.feeds.concat(data);
    },
    setInitFeeds(state, data) {
      state.feeds.length = 0;
      state.feeds = data;
    },

    setMyFeeds(state, payload) {
      state.myFeeds = payload;
    },
    setRecomendTeams(state, payload) {
      state.myRecomendTeams = payload;
    },
    setYourFeeds(state, payload) {
      state.yourFeeds.length = 0;
      state.yourFeeds = payload;
    },
    setTeamFeeds(state, payload) {
      state.teamFeeds = payload;
    },
    setComments(state, payload) {
      state.comments = payload;
    },
    SET_WHOLECHALLENGE(state, whole_projects) {
      state.whole_projects = whole_projects;
    },
    SET_TEAMCHALLENGE(state, data) {
      state.team_challenges.length = 0;
      data.forEach(element => {
        state.team_challenges.push({ value: element, text: element });
      });
    },
    SET_TEAMCHALLENGER(state, data) {
      state.team_challenging.length = 0;
      data.forEach(element => {
        state.team_challenging.push({ value: element });
      });
    },
    SET_WHOLETEAMCHALLENGE(state, data) {
      state.whole_challenges.length = 0;
      data.forEach(element => {
        state.whole_challenges.push({ value: element, text: element });
      });
    },
    SET_MY_TEAMLIST(state, data) {
      state.myTeamList.length = 0;
      data.forEach(element => {
        state.myTeamList.push({ value: element, text: element });
      });
    },
    SET_MANAGING_TEAM(state, data) {
      console.log("내가 관리하는팀");
      console.log(data);
      state.managingTeam = data;
    },
    setTeamLists(state, payload) {
      state.teamLists = payload;
    },
    setBooks(state, payload) {
      state.books = payload;
    },
    setBook(state, payload) {
      state.book = payload;
    },
    SET_REQUESTS(state, payload) {
      state.joinRequests = payload;
    },
    SET_SELECT_TEAM(state, data) {
      state.selectTeam = data;
    },
    SET_COMMENTS(state, payload) {
      state.comments = payload;
    },
    SET_FEEDID(state, payload) {
      state.feedid = payload;
    },
    SET_ENTIRECHALLEGE(state, payload) {
      state.entire_challenge = payload;
    },
    SET_TEAMCHALLENGING(state, data) {
      state.feed_challenging.length = 0;
      data.forEach(element => {
        state.feed_challenging.push({ value: element, text: element });
      });
    },
    setNoticeItems(state, payload) {
      state.noticeItems = payload;
    },
    setNoticeItem(state, payload) {
      state.noticeItem = payload;
    },
    SET_ONEFEED(state, data) {
      state.oneFeed = data;
    },
    
    SET_MANAGING_TEAM_MEMBERS(state, payload) {
      state.managingTeamMembers.length = 0;
      state.managingTeamMembers = payload;
    },
    //Token
    setToken(state, token) {
      state.token = token;
    },
    SET_FEEDLIKE(state, payload) {
      state.feedLike = payload;
    },
    SET_LIKELIST(state, payload) {
      state.likeList = payload;
    },
    SET_SELECT_TEAM_MEMBERS(state, payload) {
      state.selectTeamMembers = payload;
    },
    SET_INFO(state, payload) {
      state.yourInfo = payload;
    },
  },
  actions: {
    async GET_MEMBER_INFO({ commit }, token) {
      let decode = jwt_decode(token);
      await findById(
        decode.memberEmail,
        response => {
          if (response.data.message === "success") {
            commit("setMemberInfo", response.data.memberInfo);
            commit("setToken", token);
          } else {
            console.log("유저 정보 없음!!");
          }
        },
        error => {
          console.log(error);
        }
      );
    },

    LOGOUT({ commit }) {
      commit("logout");
      //this.state.myTeamList.length = 0;
      localStorage.removeItem("access-token");
    },
    SET_SELECT_TEAM(context, payload) {
      this.state.selectTeam = {};
      context.commit("SET_SELECT_TEAM", payload);
    },

    GET_TEAMCHALLENGE_INFO(context, payload) {
      const instance = createInstance();
      instance
        .get("/my_teamchallenge_list/" + "{member_id}?member_id=" + payload)
        .then(data => {
          context.commit("SET_TEAMCHALLENGE", data.data.object);
        })
        .catch(() => {});
    },

    GET_TEAMCHALLENGER_INFO(context, payload) {
      const instance = createInstance();
      instance
        .get(
          "/my_teamchallenger_list?" +
            "member_id=" +
            payload.memberId +
            "&team_id=" +
            payload.teamId
        )
        .then(response => {
          context.commit("SET_TEAMCHALLENGER", response.data.object);
        })
        .catch(() => {});
    },

    GET_TEAMCHALLENGEING_INFO(context, payload) {
      const instance = createInstance();
      instance
        .get("/my_teamchalleging_list?" + "member_id=" + payload)
        .then(response => {
          console.log(response);
          context.commit("SET_TEAMCHALLENGING", response.data.object);
        })
        .catch(() => {});
    },

    async GET_WHOLECHALLENGE_INFO({ commit }) {
      const instance = createInstance();
      await instance
        .get("/challenge/whole_challenge_list")
        .then(data => {
          commit("SET_WHOLETEAMCHALLENGE", data.data);
        })
        .catch(() => {
          //alert("에러발생!");
        });
    },

    async GET_MY_TEAM_INFO({ commit, dispatch, state }, payload) {
      const instance = createInstance();
      await instance
        .get("/team/my_team_list/" + payload)
        .then(data => {
          commit("SET_MY_TEAMLIST", data.data.object);
          console.log("내 모든 팀정보");
          console.log(data.data.object);
          /*data.data.object.forEach(element => {
            let managerId = element.member.memberId;
            console.log("리더");
            console.log(memberId);
            if (managerId === state.memberInfo.memberId) {
              commit("SET_MANAGING_TEAM", element);
              dispatch("getRequests", element.teamId);
              dispatch("getTeamMembers", element.teamId);
            }
          });*/
        })
        .catch(() => {
          //alert("에러발생!");
        });
    },

    getTeamLists({ commit }) {
      const instance = createInstance();
      instance
        .get("/team")
        .then(response => {
          commit("setTeamLists", response.data.object);
        })
        .catch(() => {
          console.log("에러");
        });
    },
    getFeeds({ commit }, payload) {
      const instance = createInstance();
      instance
        .get("/feed/" + payload.memberId + "/" + payload.page)
        .then(response => {
          commit("setInitFeeds", response.data.object);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    getMyFeeds({ commit }, payload) {
      const instance = createInstance();
      instance
        .get("/feed/member/" + payload)
        .then(response => {
          commit("setMyFeeds", response.data.object);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    GET_RECOMEND_TEAMS({ commit }, payload) {
      const instance = createInstance();
      instance
        .get("/recommend/" + payload)
        .then(response => {
          commit("setRecomendTeams", response.data.object);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    async getYourFeeds({ commit }, payload) {
      const instance = createInstance();
      await instance
        .get("/feed/member/" + payload)
        .then(response => {
          commit("setYourFeeds", response.data.object);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    getMemberByEmail({ commit }, payload) {
      const instance = createInstance();
      instance
        .get("/member/your/" + payload)
        .then(response => {
          commit("SET_INFO", response.data.object);
        })
        .catch(() => {
          alert("에러발생");
        });
    },
    getMyTeamFeeds({ commit }, teamId) {
      const instance = createInstance();
      instance
        .get("/feed/team/" + teamId)
        .then(response => {
          commit("setTeamFeeds", response.data.object);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    getComments(context) {
      const instance = createInstance();
      instance
        .get("")
        .then(({ data }) => {
          context.commit("setComments", data);
        })
        .catch(() => {
          //alert("에러발생");
        });
    },
    getRequests(context, teamId) {
      const instance = createInstance();
      instance
        .get("/request/" + teamId)
        .then(({ data }) => {
          context.commit("SET_REQUESTS", data);
        })
        .catch(() => {
          console.log("index.js getrequest error");
        });
    },
    GET_COMMENTS(context, payload) {
      const instance = createInstance();
      instance
        .get("/comment/" + payload)
        .then(data => {
          context.commit("SET_COMMENTS", data.data.object);
        })
        .catch(() => {});
    },
    SET_FEEDID(context, payload) {
      this.state.feedid = {};
      context.commit("SET_FEEDID", payload);
    },
    GET_ENTIRECHALLENGE_INFO(context, memberId) {
      const instance = createInstance();
      instance
        .get("/member/challenge/" + memberId)
        .then(({ data }) => {
          console.log("공통챌린지", data);
          context.commit("SET_ENTIRECHALLEGE", data);
        })
        .catch(() => {
          console.log("에러발생");
        });
    },
    getNoticeItems({ commit }, teamId) {
      const instance = createInstance();
      instance.get("/board/list/" + teamId).then(({ data }) => {
        commit("setNoticeItems", data.object);
      });
    },
    getNoticeItem({ commit }, boardid) {
      const instance = createInstance();
      instance.get("/board" + boardid).then(({ data }) => {
        commit("setNoticeItem", data.object);
      });
    },
    SET_ONEFEED(context, payload) {
      context.commit("SET_ONEFEED", payload);
    },
    async getTeamMembers({ commit }, teamId) {
      const instance = createInstance();
      console.log("매니저 팀원들구하기");
      console.log(teamId);
      await instance.get("/jointeam/member/" + teamId).then(({ data }) => {
        console.log(data.data);
        commit("SET_MANAGING_TEAM_MEMBERS", data.data);
      });
    },
    changeTeamLeader({ teamId, memberId }) {
      http
        .get("/team/leader/" + memberId + "?teamId=" + teamId)
        .then(({ data }) => {
          console.log("changeTeamLeader : " + data.message);
        });
    },
    // changeTeamLeader({commit}, {teamId, memberId}) {
    //   http.put("/team/leader/"+memberId+"?teamId="+teamId).then(({ data }) => {
    //     console.log("changeTeamLeader : " + data.message);
    //   });
    // },
    GET_FEEDLIKE(context, feedId) {
      const instance = createInstance();
      instance
        .get("/feedlike/feed/" + feedId)
        .then(({ data }) => {
          context.commit("SET_FEEDLIKE", data.object);
        })
        .catch(() => {
          console.log("에러발생");
        });
    },
    SET_LIKELIST(context, payload) {
      context.commit("SET_LIKELIST", payload);
    },
    getSelectTeamMembers({ commit }, teamId) {
      const instance = createInstance();
      instance.get("/jointeam/member/" + teamId).then(({ data }) => {
        commit("SET_SELECT_TEAM_MEMBERS", data.data);
      });
    },
    getMemberByMemberId({commit},memberId){
      const instance = createInstance();
      instance
        .get("/member/id/"+memberId)
        .then(({data}) => {
          commit("setMemberInfo",data.object);
        });
    },
    GET_MANAGE_TEAM({ commit, dispatch }, payload) {
      console.log("매니지팀 호출");
      const instance = createInstance();
      instance
        .get("/team/"+payload)
        .then(({ data }) => {
          if (data.object) {
            commit("SET_MANAGING_TEAM", data.object);
            console.log(data.object.teamId);
            dispatch("getRequests", data.object.teamId);
            dispatch("getTeamMembers", data.object.teamId);
          }
        });
    }
  }
});

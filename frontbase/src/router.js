import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import Meta from "vue-meta";
import Login from "@/views/user/Login.vue";
import TeamChallenge from "@/views/Challenge/TeamChallenge.vue";
import Challenge from "@/views/Challenge/WholeChallenge.vue";
import TeamMain from "@/views/Team/TeamMain.vue";
import Board from "@/views/Board.vue";
import Chatting from "@/views/Team/TeamChattingRoom.vue";
import TeamFeed from "@/views/Team/TeamFeed.vue";
import MyTeam from "@/views/Team/MyTeam.vue";
import MyTeamWrite from "@/views/Team/MyTeamWrite.vue";
import MyPage from "@/views/user/MyPage.vue";
import MyFeed from "@/views/user/MyFeed.vue";
import MemberModify from "@/views/user/MemberModify.vue";
import MemberPasswordModify from "@/views/user/MemberPasswordModify.vue";
import ChallengeMake from "@/views/Challenge/TeamChallengeMake.vue";
import TeamManagement from "@/views/user/TeamManagement.vue";
import TeamModify from "@/views/user/TeamModify.vue";
import PayHome from "@/views/Pay/PayHome.vue";
import Payment from "@/views/Pay/Payment.vue";
import Result from "@/views/Pay/Result.vue";
import Certification from "@/views/Pay/Certification.vue";
import MemberDetail from "@/views/user/MemberDetail.vue";
Vue.use(Router);
Vue.use(Meta);

const requireAuth = () => (to, from, next) => {
  if (localStorage.getItem("access-token")) {
    return next();
  }
  alert("로그인이 필요합니다.");
  next("/");
};

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,

  routes: [
    {
      path: "/",
      name: "home",
      component: Home
    },
    {
      path: "/signup",
      name: "signup",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/SignUp.vue")
    },
    {
      path: "/kakaosignup",
      name: "kakaosignup",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/KakaoSignUp.vue")
    },
    {
      path: "/naversignup",
      name: "naversignup",
      component: () =>
        import( /* webpackChunkName: "about" */ "./views/NaverSignUp.vue")
    },
    {
      path: "/myteam",
      name: "myteam",
      component: MyTeam,
      beforeEnter: requireAuth()
    },
    {
      path: "/myteamwrite",
      name: "myteamwrite",
      component: MyTeamWrite,
      beforeEnter: requireAuth()
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MyPage,
      beforeEnter: requireAuth()
    },
    {
      path: "/myfeed",
      name: "myfeed",
      component: MyFeed,
      beforeEnter: requireAuth()
    },
    {
      path: "/membermodify",
      name: "membermodify",
      component: MemberModify,
      beforeEnter: requireAuth()
    },
    {
      path: "/memberpasswordmodify",
      name: "memberpasswordmodify",
      component: MemberPasswordModify,
      beforeEnter: requireAuth()
    },
    {
      path: "/teammake",
      name: "teammake",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Team/TeamMake.vue"),
      beforeEnter: requireAuth()
    },
    {
      path: "/teamlist",
      name: "teamlist",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Team/Teamlist.vue"),
      beforeEnter: requireAuth()
    },
    
    {
      path: "/blog",
      name: "blog",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Blog.vue")
    },

    
    {
      path: "/portfolio",
      name: "portfolio",
      component: () =>
        import(
          /* webpackChunkName: "about" */ "./views/Portfolio/Portfolio.vue"
        )
    },

    {
      path: "/portfolio/graphic",
      name: "graphicPortfolio",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Portfolio/Graphic.vue")
    },
    {
      path: "/portfolio/web",
      name: "webPortfolio",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Portfolio/Web.vue")
    },
    {
      path: "/group",
      name: "group",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Group.vue")
    },
    {
      path: "/writefeed",
      name: "writefeed",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Feed/WriteFeed.vue")
    },
    {
      path: "/updatefeed",
      name: "updatefeed",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Feed/UpdateFeed.vue")
    },
    {
      path: "/feed",
      name: "feed",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Feed/Feed.vue"),
      beforeEnter: requireAuth()
    },
    {
      path: "/comment",
      name: "comment",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Feed/Comment.vue"),
      beforeEnter: requireAuth()
    },
    {
      path: "*",
      name: "Error",
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/Error.vue")
    },
    {
      path: "/login",
      name: "login",
      component: Login
    },
    {
      path: "/teamChallenge",
      name: "teamChallenge",
      component: TeamChallenge,
      beforeEnter: requireAuth()
    },
    {
      path: "/challenge",
      name: "challenge",
      component: Challenge,
      beforeEnter: requireAuth()
    },
    {
      path: "/teammain",
      name: "teammain",
      component: TeamMain,
      beforeEnter: requireAuth()
    },
    {
      path: "/chattingroom",
      name: "chatting",
      component: Chatting,
      beforeEnter: requireAuth()
    },
    {
      name: "board",
      path: "/board",
      component: () => import("@/components/board/BoardList.vue"),
      beforeEnter: requireAuth()
    },
    {
      name: "TeamFeed",
      path: "/teamFeed",
      component: TeamFeed,
      beforeEnter: requireAuth()
    },
    {
      name: "ChallengeMake",
      path: "/challengemake",
      component: ChallengeMake,
      beforeEnter: requireAuth()
    },
    {
      path: "/board/view",
      name: "BoardView",
      component: () =>
        import(
          /* webpackChunkName: "about" */ "./components/board/BoardView.vue"
        )
    },
    {
      path: "/board/create",
      name: "BoardCreate",
      component: () =>
        import(
          /* webpackChunkName: "about" */ "./components/board/BoardCreate.vue"
        )
    },
    {
      path: "/board/modify",
      name: "BoardModify",
      component: () =>
        import(
          /* webpackChunkName: "about" */ "./components/board/BoardModify.vue"
        )
    },

    {
      name: "board",
      path: "/board",
      component: Board,
      children: [
        {
          path: "",
          name: "board-list",
          component: () => import("@/components/board/BoardList.vue")
        },
        {
          path: "/create",
          name: "board-create",
          component: () => import("@/components/board/BoardCreate.vue")
        },
        {
          path: "/view",
          name: "board-view",
          component: () => import("@/components/board/BoardView.vue")
        }
      ],
      redirect: () => {
        return "/board";
      }
    },
    {
      path: "/teamManagement",
      name: "teamManagement",
      component: TeamManagement,
      beforeEnter: requireAuth()
    },
    {
      path: "/teamModify",
      name: "teamModify",
      component: TeamModify,
      beforeEnter: requireAuth()
    },
    {
      path: '/payhome',
      name: 'PayHome',
      component: PayHome,
    },
    {
      path: '/payment',
      name: 'Payment',
      component: Payment,
    },
    {
      path: '/certification',
      name: 'Certification',
      component: Certification,
    },
    {
      path: '/result',
      name: 'Result',
      component: Result,
    },
    {
      path: '/memberdetail',
      name: 'MemberDetail',
      component: MemberDetail,
    },
  ]
});

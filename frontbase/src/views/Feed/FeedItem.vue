<template>
  <div class="feed-item" style="border-color:green">
    <div class="feed_t">
      <div class="user_wrap">
        <img v-bind:src="feed.member.photo.filePath" class="profile" />
       
        <div
          v-if="feed.member.point >= 100"
          id="feed_writer"
          class="feed_writer"
          style="color: #9400D3; font-weight: bolder"
        >
          <div @click="check()" style="cursor:pointer">{{ feed.writer }}</div>
        </div>
        <div
          v-else-if="feed.member.point >= 75"
          id="feed_writer"
          class="feed_writer"
          style="color: #7AD7BE; font-weight: bolder"
        >
          <div @click="check()" style="cursor:pointer">{{ feed.writer }}</div>
        </div>
        <div
          v-else-if="feed.member.point >= 50"
          id="feed_writer"
          class="feed_writer"
          style="color:#FFA500; font-weight: bolder"
        >
          <div @click="check()" style="cursor:pointer">{{ feed.writer }}</div>
        </div>
        <div
          v-else-if="feed.member.point >= 25"
          id="feed_writer"
          class="feed_writer"
          style="color: #52478B; font-weight: bolder"
        >
          <div @click="check()" style="cursor:pointer">{{ feed.writer }}</div>
        </div>
        <div
          v-else
          id="feed_writer"
          class="feed_writer"
          style="color: #8B4513; font-weight: bolder"
        >
          <div @click="check()" style="cursor:pointer">{{ feed.writer }}</div>
        </div>
      </div>
      <div>
        <div class="feed-btns">
          <div @click="modifyFeed(feed.feedId)" class="modiBtn"></div>
          <div @click="deleteFeed" class="delBtn"></div>
        </div>
      </div>
    </div>
    <div class="feed-card">
      <div class="feed-wrap">
        <div
          class="img"
          :style="{
            'background-image': 'url(' + feed.photos[0].filePath + ')'
          }"
        ></div>
      </div>
      <div
        v-if="feed.member.point >= 100"
        id="contentsWrap"
        class="contentsWrap"
        style="border: 2px solid #9400D3"
      >
        <div class="like_wrap" @click="changeLike">
          <img
            class="likeBtn"
            v-if="this.isLike == true"
            src="../../assets/heart.png"
          />
          <img class="likeBtn" v-else src="../../assets/heart_b.png" />
          <div class="likeCnt">좋아요 {{ this.likeCount }}개</div>
        </div>
        <div class="desc">
          {{ feed.contents }}
        </div>
        <div class="showComment" @click="mvComment()">댓글보기</div>
        <div class="feed_date">
          <b>{{ getFormatDate(feed.writeDate) }}</b>
        </div>
      </div>
      <div
        v-else-if="feed.member.point >= 75"
        id="contentsWrap"
        class="contentsWrap"
        style="border: 2px solid #7AD7BE"
      >
        <div class="like_wrap" @click="changeLike">
          <img
            class="likeBtn"
            v-if="this.isLike == true"
            src="../../assets/heart.png"
          />
          <img class="likeBtn" v-else src="../../assets/heart_b.png" />
          <div class="likeCnt">좋아요 {{ this.likeCount }}개</div>
        </div>
        <div class="desc">
          {{ feed.contents }}
        </div>
        <div class="showComment" @click="mvComment()">댓글보기</div>
        <div class="feed_date">
          <b>{{ getFormatDate(feed.writeDate) }}</b>
        </div>
      </div>
      <div
        v-else-if="feed.member.point >= 50"
        id="contentsWrap"
        class="contentsWrap"
        style="border: 2px solid #FFA500"
      >
        <div class="like_wrap" @click="changeLike">
          <img
            class="likeBtn"
            v-if="this.isLike == true"
            src="../../assets/heart.png"
          />
          <img class="likeBtn" v-else src="../../assets/heart_b.png" />
          <div class="likeCnt">좋아요 {{ this.likeCount }}개</div>
        </div>
        <div class="desc">
          {{ feed.contents }}
        </div>
        <div class="showComment" @click="mvComment()">댓글보기</div>
        <div class="feed_date">
          <b>{{ getFormatDate(feed.writeDate) }}</b>
        </div>
      </div>
      <div
        v-else-if="feed.member.point >= 25"
        id="contentsWrap"
        class="contentsWrap"
        style="border: 2px solid #52478B"
      >
        <div class="like_wrap" @click="changeLike">
          <img
            class="likeBtn"
            v-if="this.isLike == true"
            src="../../assets/heart.png"
          />
          <img class="likeBtn" v-else src="../../assets/heart_b.png" />
          <div class="likeCnt">좋아요 {{ this.likeCount }}개</div>
        </div>
        <div class="desc">
          {{ feed.contents }}
        </div>
        <div class="showComment" @click="mvComment()">댓글보기</div>
        <div class="feed_date">
          <b>{{ getFormatDate(feed.writeDate) }}</b>
        </div>
      </div>
      <div
        v-else
        id="contentsWrap"
        class="contentsWrap"
        style="border: 2px solid #8B4513"
      >
        <div class="like_wrap" @click="changeLike">
          <img
            class="likeBtn"
            v-if="this.isLike == true"
            src="../../assets/heart.png"
          />
          <img class="likeBtn" v-else src="../../assets/heart_b.png" />
          <div class="likeCnt">좋아요 {{ this.likeCount }}개</div>
        </div>
        <div class="desc">
          {{ feed.contents }}
        </div>
        <div class="showComment" @click="mvComment()">댓글보기</div>
        <div class="feed_date">
          <b>{{ getFormatDate(feed.writeDate) }}</b>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { createInstance } from "@/api/teamindex.js";
import defaultImage from "../../assets/images/img-placeholder.png";
import defaultProfile from "../../assets/images/profile_default.png";
import moment from "moment";

export default {
  props: ["feed", "index", "lists"],
  data: () => {
    return {
      defaultImage,
      defaultProfile,
      feedget: {
        memberId: "",
        page: ""
      },
      isLike: null,
      feedlikeId: "",
      likeCount: ""
    };
  },
  computed: {
    ...mapGetters(["memberInfo", "myTeamList", "feedLike"])
  },
  mounted() {},
  created() {
    for (let index = 0; index < this.lists.length; index++) {
      if (
        this.feed.feedId == this.lists[index].feedId &&
        this.lists[index].feedLike == 1
      ) {
        this.feedlikeId = this.lists[index].feedlikeId;
        this.isLike = true;
        break;
      } else {
        this.isLike = false;
      }
    }
    const instance = createInstance();
    instance
      .get("/feedlike/feed/" + this.feed.feedId)
      .then(({ data }) => {
        this.likeCount = data.object.length;
      })
      .catch(() => {});
  },
  methods: {
    getFormatDate(writeDate) {
      return moment(new Date(writeDate)).format("YYYY년 MM월 DD일 HH:mm");
    },
    changeLike() {
      if (!this.isLike) {
        var feedlike = {
          feedId: this.feed.feedId,
          feedLike: 1,
          memberId: this.memberInfo.memberId
        };
        const instance = createInstance();
        instance
          .post("/feedlike", feedlike)
          .then(response => {
            if (response.data.data === "success") {
              this.isLike = true;
              const instance = createInstance();
              instance
                .get("/feedlike/feed/" + this.feed.feedId)
                .then(({ data }) => {
                  this.feedlikeId = data.object[0].feedlikeId;
                  this.likeCount = data.object.length;
                })
                .catch(() => {});
              alert("좋아요!");
              this.$store.dispatch(
                "GET_MEMBER_INFO",
                window.localStorage.getItem("access-token")
              );
            } else {
              alert("좋아요실패");
            }
          })
          .catch();
      } else {
        const instance = createInstance();
        instance
          .delete("/feedlike/" + this.feedlikeId)
          .then(response => {
            if (response.data.data === "success") {
              this.isLike = false;
              const instance = createInstance();
              instance
                .get("/feedlike/feed/" + this.feed.feedId)
                .then(({ data }) => {
                  this.likeCount = data.object.length;
                })
                .catch(() => {});
              alert("좋아요취소!");
            } else {
              alert("좋아요 취소실패");
            }
          })
          .catch();
      }
    },
    mvComment() {
      this.$store.dispatch("GET_COMMENTS", this.feed.feedId);
      this.$store.dispatch("SET_FEEDID", this.feed.feedId);
      this.$router.replace("/comment");
    },
    modifyFeed(data) {
      console.log(this.feed);
      if (this.feed.writer.replaceAll('"', "") === this.memberInfo.name) {
        this.$store.dispatch("SET_FEEDID", this.feed.feedId);
        this.$store.dispatch(
          "GET_TEAMCHALLENGEING_INFO",
          this.memberInfo.memberId
        );
        const body = {
          memberId: this.memberInfo.memberId,
          teamId: this.feed.team.teamId,
          teamName: this.feed.teamName,
          contents: this.feed.contents,
          writer: this.feed.writer,
          image: this.feed.photos[0].filePath,
          photos: this.feed.photos[0]
        };
        this.$store.dispatch("SET_ONEFEED", body);
        this.$router.push("/updateFeed");
      } else {
        alert("본인만 수정할 수 있습니다");
      }
    },
    deleteFeed() {
      if (this.feed.writer.replaceAll('"', "") === this.memberInfo.name) {
        const instance = createInstance();
        instance
          .delete("/feed/" + this.feed.feedId)
          .then(response => {
            if (response.data.data === "success") {
              this.$store.dispatch(
                "GET_TEAMCHALLENGEING_INFO",
                this.memberInfo.memberId
              );
              alert("피드 삭제 완료");
              this.feedget.memberId = this.memberInfo.memberId;
              this.feedget.page = 0;
              this.$store.dispatch("getFeeds", this.feedget);
              this.$store.dispatch("getMyFeeds", this.memberInfo.memberId);
              this.$router.push("/feed");
            } else {
              alert("피드 삭제 실패");
            }
          })
          .catch(() => {
            alert("에러");
          });
      } else {
        alert("본인만 삭제할 수 있습니다");
      }
    },
    check() {
      console.log(this.feed.member.email);
      this.$store.dispatch("getMemberByEmail", this.feed.member.email);
      this.$store.dispatch("getYourFeeds", this.feed.member.memberId);
      this.$router.push("/memberdetail");
    }
  }
};
</script>

<template>
  <div>
    <div class="header">
      <router-link :to="{ name: 'Home' }" class="home">
        <!--감자 사진 임시로...-->
        <v-img :src="require('@/assets/imgs/gamja.png')" width="60px" />
        감자캠핑
      </router-link>
      <div>
        <router-link :to="{ name: 'Home' }">전국캠핑장</router-link>
        <router-link :to="{ name: 'Test' }">커뮤니티</router-link>
      </div>
      <div class="login">
        <router-link :to="{ name: 'Login' }" v-if="!isLogin">로그인</router-link>
        <router-link :to="{ name: 'Test' }" v-else>마이페이지</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isLogin: false
    }
  },
  methods: {
    goHome () {
      this.$router.push('/')
    }
  }
}
</script>


<style scoped>
.header {
  height: 70px;
  display: flex;
  align-items: center;
  background-color: white;
}
a:link {
  text-decoration: none;
  margin-right: 20px;
  color: black;
}
a:visited {
  color: black;
}
/* a:hover {
  font-size: 17px;
  transition: 0.4s;
} */
a:hover {
  color: #FFBB98;
  transition: 0.3s;
}
.home {
  font-size: 30px;
  display: flex;
  cursor: pointer;
  margin-left: 40px;
  margin-right: 30px;
  font-weight: bolder;
}
.login {
  margin-left: auto;
  margin-right: 40px;
}
</style>
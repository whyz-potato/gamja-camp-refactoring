<template>
  <div class="form">
    <div class="tab">
      <div>
        개인회원
      </div>
      <div>
        기업회원
      </div>
    </div>
    <v-btn class="my-5" block large text outlined>카카오 로그인</v-btn>
    <v-btn class="my-5" block large text outlined>네이버 로그인</v-btn>
    <v-btn class="my-5" block large text outlined>구글 로그인</v-btn>
    
  </div>
</template>


<script>
export default {
  data (){
    return {

    }
  }
}
</script>


<style scoped>
.form {
  width: 400px;
  margin: auto;
  margin-top: 150px;
}
.tab {
  display: flex;
}
</style>
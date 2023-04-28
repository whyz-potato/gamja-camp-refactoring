<template>
  <v-app>
    <Header></Header>
    <v-main>
      <router-view/>
      <chat-btn></chat-btn>
    </v-main>
  </v-app>
</template>

<script>
import Header from '@/layouts/Header'
import ChatBtn from '@/components/ChatBtn'

export default {
  name: 'App',
  components: {
    Header,
    ChatBtn  
  },
  data: () => ({
    
  }),
}
</script>


<style>
#app {
  font-family: 'Roboto', 'Noto Sans KR', sans-serif;
  width : 100vw;
  height : 100vh;
  /* background-color: #fef1e3; */
  background-color: #f7f7f7;
  /* position: relative; */
}
@font-face {
  font-family: 'Noto Sans KR';
  src: local('Noto Sans KR');
  unicode-range: U+AC00-U+D7A3;
}
::-webkit-scrollbar {
  width: 6px;
}
::-webkit-scrollbar-track {
  background-color: transparent;
}
::-webkit-scrollbar-thumb:hover  {
  border-radius: 3px;
  background-color: gray;
}
::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background-color: transparent;
}
</style>

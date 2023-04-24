<template>
  <v-app>
    <Header></Header>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import Header from '@/layouts/Header'

export default {
  name: 'App',
  components: {
    Header  
  },
  data: () => ({
    
  }),
}
</script>


<style>
#app {
  font-family: 'Roboto', 'Noto Sans KR', sans-serif;
}
@font-face {
  font-family: 'Noto Sans KR';
  src: local('Noto Sans KR');
  unicode-range: U+AC00-U+D7A3;
}
</style>

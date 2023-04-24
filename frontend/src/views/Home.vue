<template>
  <div>
    <v-btn @click="getMyPosition">
      위치확인
    </v-btn>
  </div>
</template>

<script>
  export default {
    name: 'Home',
    data () {
      return {
        lat: null,
        lng: null
      }
    },
    mounted () {
 
    },
    methods: {
      getMyPosition () {
        navigator.geolocation.getCurrentPosition(
          this.getPosition
        )
      },
      getPosition (val) {
        console.log(val)
        this.lat = val.coords.latitude
        this.lng = val.coords.longitude
        console.log(this.lat, this.lng)
      }
    }
  }
</script>

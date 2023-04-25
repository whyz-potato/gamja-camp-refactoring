<template>
  <div id="camp">
    <div id="list">
      <v-card height="800">
        캠핑장 리스트
      </v-card>
    </div>
    
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      map: null
    }
  },
  mounted () {
    this.map = new window.naver.maps.Map('map',{
      zoom: 15
    })

    navigator.geolocation.getCurrentPosition((e) => {
      const lat = e.coords.latitude
      const lng = e.coords.longitude
      console.log(lat, lng)

      const center = new window.naver.maps.LatLng(lat, lng)
      this.map.setCenter(center)

      new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(lat, lng),
        map: this.map
      })

    })
  },
  methods: {
  }
}
</script>


<style scoped>
#camp {
  display: flex;
}
#list {
  width:30%; 
  height:800px; 
}
#map {
  width:70%; 
  height:800px;
}
</style>
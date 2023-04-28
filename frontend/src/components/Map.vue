<template>
  <div class="camp">
    <div class="list">
      캠핑장 리스트
    </div>
    
    <div id="map" class="map"></div>
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
.camp {
  display: flex;
  height:100%; 
  background-color: #f7f7f7;
}
.list {
  width:25%; 
  height:100%; 
}
.map {
  width:75%; 
  height:100%;
}
</style>
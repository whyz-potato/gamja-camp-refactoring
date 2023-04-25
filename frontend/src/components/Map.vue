<template>
  <div id="camp">
    <div id="list">
      <div width="20%" height="700px">
        캠핑장 리스트
      </div>
    </div>
    <div id="map" style="width:80%; height:700px; float:right;"></div>
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
}
</style>
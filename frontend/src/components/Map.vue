<template>
  <div>
    <div id="map" ref="maps" style="width:80%; height:700px;"></div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      lat: null,
      lng: null
    }
  },
  mounted () {
    navigator.geolocation.getCurrentPosition((e) => {
      this.lat = e.coords.latitude
      this.lng = e.coords.longitude
      console.log(this.lat, this.lng)

      const map = new window.naver.maps.Map('map',{
        center: new window.naver.maps.LatLng(this.lat, this.lng),
        zoom: 15
      })

      new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(this.lat, this.lng),
        map: map
      })
    })


    
  },
  methods: {
  }
}
</script>
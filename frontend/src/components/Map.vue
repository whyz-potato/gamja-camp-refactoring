<template>
  <div class="sheet">
    <div class="camps">
      <div class="search">
        <input type="text" placeholder="지역 또는 캠핑장 검색">
        <div>
          <input type="text" class="search_date" placeholder="기간">
          <input type="text" class="search_guest" placeholder="인원수">
        </div>
      </div>

      <div class="camps_count">검색결과 {{ this.count }} 건</div>

      <div v-for="list in campList" :key="list.index" class="camp_list">
        <div style="display:flex;">
          <div v-for="img in list.images" :key="img.index" class="camp_img">
            <img :src="require(`@/assets/test/${img}`)">
          </div>
        </div>

        <div class="camp_info">
          <div class="camp_name">{{ list.name }}</div>
          <div class="camp_rate">
            <img :src="require('@/assets/imgs/star_icon.png')">
            <div>{{ list.rate }}</div>
          </div>       
        </div>

        <div class="camp_price">
          <span>1박 / </span>
          <span class="price">{{ list.OneNightPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}</span>
          <span> 원~</span>
        </div>
      </div>
    </div>
    
    <div id="map" class="map"></div>
  </div>
</template>

<script>
import camp_list from '@/assets/test/camp_list'

export default {
  data () {
    return {
      map: null,
      neLat: null,
      neLng: null,
      swLat: null,
      swLng: null,
      count: null,
      campList: null,
      markers: []
    }
  },
  mounted () {
    this.count = camp_list.count
    this.campList = camp_list.camps
    console.log(this.campList)

    this.map = new window.naver.maps.Map('map',{
      zoom: 15
    })

    navigator.geolocation.getCurrentPosition((e) => {
      const lat = e.coords.latitude
      const lng = e.coords.longitude

      const center = new window.naver.maps.LatLng(lat, lng)
      this.map.setCenter(center)
    })

    new window.naver.maps.Event.addListener(this.map, 'idle', () => {
      const northEast = this.map.getBounds().getNE()
      const southWest = this.map.getBounds().getSW()
      
      this.neLat = northEast.lat()
      this.neLng = northEast.lng()
      this.swLat = southWest.lat()
      this.swLng = southWest.lng()

      // console.log(this.neLat,this.neLng)
    })

  
    for (let i = 0; i < this.campList.length; i++) {
      const coord = new window.naver.maps.LatLng(this.campList[i].lat, this.campList[i].lng)
      const price = this.campList[i].OneNightPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      const name = this.campList[i].name

      const marker = new window.naver.maps.Marker({
        map: this.map,
        position: coord,
        icon: { content: `<div class="marker">${price}</div>` },
        title: name
      })

      this.markers.push(marker)
    }
  },
  methods: {
  }
}
</script>


<style scoped>
.sheet {
  display: flex;
  height:100%; 
  background-color: #f7f7f7;
}
.camps {
  width:25%; 
  height:100%; 
}
.map {
  width:75%; 
  height:100%;
}
.search {
  width: 95%;
  margin: auto;
  margin-top: 10px;
}
input {
  width: 100%;
  height: 40px;
  border: 1px solid #dddddd;
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  display: flex;
  background-color: white;
  outline-color: #dddddd;
  padding-left: 10px;
  text-align: center;
  margin-bottom: 5px;
}
.search>div {
  display: flex;
}
.search_date {
  width: 70%;
}
.search_guest {
  width: 30%;
}
.camps_count {
  width: 95%;
  text-align: right;
  margin-top: 5px;
  margin-bottom: 5px;
  color: #5e5e5e;
}
.camp_list {
  width: 95%;
  margin: auto;
  margin-bottom: 5px;
  background-color: white;
  padding: 10px;
  border: 1px solid #dddddd;
  border-radius: 12px;
  cursor: pointer;
  transition: box-shadow .3s;
}
.camp_list:hover {
  border: 2px solid #dddddd;
  box-shadow: 0 14px 28px #dddddd, 0 10px 10px #dddddd;
}
.camp_img {
  width: 70px;
  margin-right: 5px;
}
.camp_img>img {
  width: 100%;
  object-fit: cover;
}
.camp_info {
  display: flex;
  margin-top: 5px;
}
.camp_name {
  font-size: 22px;
  font-weight: bold;
  color: #5e5e5e;
}
.camp_rate {
  display: flex;
  align-items: center;
  margin-left: 20px;
}
.camp_rate>img {
  width: 15px;
  height: 15px;
}
.camp_rate>div {
  color: #5e5e5e;
  margin-left: 3px;
}
.camp_price {
  color: #5e5e5e;
}
.price {
  font-size: 18px;
  font-weight: bold;
}
.marker {
  color: red;
  white-space: nowrap;
  position: relative;
  background-color: white;
  line-height: 30px;
  text-align: center;
  font-weight: bold;
  border-radius: 15px;
  transition: 0.5s;
  padding: 0 8px;
  box-shadow: #dddddd 0px 0px 0px 1px,
    #dddddd 0px 1px 2px;
  overflow-y: auto;
  transform: translate(-40%, -60%);
}
</style>
<template>
  <div class="form">
    <input type="file" accept="image/*" id="input_file" @change="inputFile">
    
    <div class="select_img" @click="selectImg">
      <div v-if="!profile">이미지 선택</div>
      <img v-else :src="imgUrl"/>
    </div>

    <div>
      <input type="text">
      <input type="text">
    </div>
  </div>
</template>


<script>
export default {
  data () {
    return {
      profile: null,
      imgUrl: null
    } 
  },
  methods: {
    inputFile (e) {
      this.profile = e.target.files[0]
      console.log(this.profile)

      try {
        this.imgUrl = URL.createObjectURL(this.profile)
      }catch(e) {
        this.imgUrl = null
      }
    },
    selectImg () {
      document.getElementById('input_file').click()
    }
  }
  
}
</script>


<style scoped>
#input_file {
  visibility: hidden;
}
.form {
  width: 400px;
  margin: auto;
}
.select_img {
  cursor: pointer;
  width: 200px;
  height: 200px;
  border: 1px solid #dddddd;
  border-radius: 100%;
  margin: auto;
}
.select_img>div {
  color: #999999;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.select_img>img {
  width: 100%;
  height: 100%;
  border-radius: 100%;
}

</style>
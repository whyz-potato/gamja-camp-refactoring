<template>
  <div class="form">
    <input type="file" accept="image/*" id="input_file" @change="inputFile">
    
    <div>
      <span class="delete_img" v-if="image">
        <img :src="require('@/assets/imgs/delete_icon.png')" @click="deleteImg"/>
      </span>
      <div class="select_img" @click="selectImg"> 
        <div v-if="!image">이미지 선택</div>
        <img v-else :src="imgUrl"/>
    </div>
    </div>
    
    <div class="input_profile">
      <span>닉네임</span>
      <input type="text" placeholder="닉네임을 입력하세요."
        v-model="nickName">
    </div>
    <div class="input_profile">
      <span>전화번호</span>
      <input type="text" placeholder="-제외하고 입력하세요." maxlength="13"
        v-model="phone" @input="inputPhone">
    </div>   

    <div class="button" @click="join">
      <span>회원가입</span>
    </div>
  </div>
</template>


<script>
//import api from '@/api/index'

export default {
  data () {
    return {
      image: null,
      imgUrl: null,
      nickName: null,
      phone: null
    } 
  },
  methods: {
    inputFile (e) {
      this.image = e.target.files[0]
      e.target.value = ''
      console.log(this.image)

      try {
        this.imgUrl = URL.createObjectURL(this.image)
      } catch(e) {
        this.imgUrl = null
        this.file = null
      }
    },
    deleteImg () {
      this.image = null
      this.imgUrl = null
    },
    selectImg () {
      document.getElementById('input_file').click()
    },
    inputPhone () {
      this.phone = this.phone.replace(/[^0-9]/g, '')
        .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`)
    },
    join () {
      console.log('회원가입')

      // const profile = {
      //   'nickName': this.nickName,
      //   'phone': this.phone
      // }

      // const formData = new FormData()
      // formData.append('image', this.image)
      // formData.append('profile', new Blob([JSON.stringify(profile)], {
      //   type: 'application/json'
      // }))

      // api.post(`/join`, formData, {
      //   headers: {
      //     "Contest-Type": "multipart/form-data"
      //   }
      // }).then(() => {

      // })
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
.delete_img {
  width: 200px;
  text-align: right;
  z-index: 2;
  position: absolute;
  left: 50%;
  transform: translate(-50%);
}
.delete_img>img {
  width: 20px;
  cursor: pointer;
  opacity: 0.5;
}
.select_img {
  cursor: pointer;
  width: 200px;
  height: 200px;
  /* border: 1px solid #dddddd; */
  border-radius: 100%;
  margin: auto;
  margin-bottom: 50px;
  position:relative;
  background-color: #f7f7f7;
}
.select_img:hover {
  border: 2px solid #dddddd;
}
.select_img>div {
  color: #dddddd;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.select_img>img {
  width: 100%;
  height: 100%;
  border-radius: 100%;
  object-fit: cover;
}
.input_profile {
  display: flex;
  width: 90%;
  margin: auto;
  margin-top: 20px;
}
.input_profile>input {
  width: 70%;
  height: 45px;
  /* border: 1px solid #999999; */
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  display: flex;
  background-color: #f7f7f7;
  outline-color: #dddddd;
  padding-left: 10px;
  text-align: center;
}
.input_profile>input::placeholder {
  color: #dddddd;
}
.input_profile>span {
  width:30%;
  line-height: 45px;
  letter-spacing: 3px;
}
.input_test {
  width: 90%;
  margin: auto;
  margin-top: 20px;
}
.button {
  width: 90%;
  height: 50px;
  margin: auto;
  margin-top: 50px;
  margin-bottom: 30px;
  border: 1px solid #dddddd;
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  display: flex;

}
.button:hover {
  background-color: #f2f2f2;
  transition: 0.6s;
}
.button>span {
  margin: auto;
  text-align: center;
  letter-spacing: 3px;
  /* font-size: 18px; */
}
</style>
<template>
  <div class="sheet" >
    <login-form></login-form>
  </div>
</template>


<script>
import LoginForm from '@/components/user/LoginForm'

export default {
  components: {
    LoginForm
  }
}
</script>


<style scoped>
.sheet {
  background-color: white;
  width: 450px;
  /* margin: auto; */
  padding: 10px;
  border-radius: 12px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
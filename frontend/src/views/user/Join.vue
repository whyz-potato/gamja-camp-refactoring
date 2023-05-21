<template>
  <div class="sheet" >
    <join-form></join-form>
  </div>
</template>


<script>
import JoinForm from '@/components/user/JoinForm'

export default {
  components: {
    JoinForm
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
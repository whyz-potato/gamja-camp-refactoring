<template>
  <div>
    <v-dialog v-model="dialog" width="400px"
      transition="dialog-bottom-transition">
      <v-card>
        채팅창~~~
      </v-card>
      
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" fab top right absolute>
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
    </v-dialog>
    
  </div>
</template>


<script>
export default {
  data () {
    return {
      dialog: false
    }
  }
}
</script>
<template>
  <v-layout column class="notFound">
    <h2>404 : Not Found!</h2>
    <p> But enjoy this funny gif:</p>
    <v-container fluid class="header">
      <v-layout row wrap>
        <v-flex xs6 offset-xs3>
            <iframe src="https://giphy.com/embed/NoGhgVB9ybIJ2" width="480" height="480" frameBorder="0" allowFullScreen></iframe>
        </v-flex>
      </v-layout>
    </v-container>
  </v-layout>
</template>

<script>
export default {
}
</script>

<style scoped>
.notFound {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  height: calc( 100% - 80px);
  border-radius: 2px;
  overflow: auto;
}
p {
    margin: 0;
}
h2 {
    margin-top: 40px;
    margin-bottom: 50px;
}
</style>

<template>
<!--Pintamos o no pintamos los nodos
<h1 v-if="age < 18">Eres menor de edad</h1>
<h1 v-else>Eres mayor de edad</h1>
-->
<!--Pintamos todos los nodos y los q no se muestran digamos que estan con css tapados-->
<h1 v-show="age < 18">Eres menor de edad</h1>
<h1 v-show="age>=18">Eres mayor de edad</h1>
</template>
  
<script lang="ts" setup >
    let age = 10
</script>
  
<style scoped>
</style>
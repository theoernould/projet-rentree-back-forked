<script setup lang="ts">
import {RouterView} from 'vue-router'
import HeaderComponent from "@/components/HeaderComponent.vue";
import {onBeforeUnmount, onMounted} from "vue";
import {useWebSocket} from "@/stores/websocket";

onMounted(async () => {
  const store = useWebSocket();
  store.connect();
});

onBeforeUnmount(() => {
  const store = useWebSocket();
  store.disconnect();
});
</script>

<template>
  <div class="global">
    <HeaderComponent />
    <RouterView class="main" />
  </div>
</template>

<style scoped lang="scss">
.global {
  display:flex;
  flex-direction: column;
  font-family: 'Poppins', sans-serif;
}

.main {
  margin-top: 56px;
}
</style>

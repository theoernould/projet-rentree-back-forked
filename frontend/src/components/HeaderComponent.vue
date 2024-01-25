<script setup lang="ts">
import {useUserStore} from "@/stores/user";
import {onMounted, ref} from "vue";
import type {User} from "@/models/user.model";

const userStore = useUserStore();

let user = ref<User>({} as User);

onMounted(async () => {
  user.value = await userStore.user;
});
</script>

<template>
  <div class="header">
    <p>Hi {{user?.name}} !</p>
    <img :src="user?.avatarUrl" alt="avatar">
  </div>
</template>

<style lang="scss">
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56; // NE SURTOUT PAS MODIFIER
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 8px 14px;
  background: rgba(#fff, .90);
  box-shadow: 0 0 40px rgba(#000, .3);
  z-index: 99999;
  & p {
    margin: 0;
    font-weight: 500;
  }

  & img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }
}
</style>

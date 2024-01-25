<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps({
  step: {
    type: String,
    default: 'step'
  },
  status: {
    type: String,
    default: 'waiting'
  }
});
console.log(props);

</script>

<template>
  <div class="step-component">
    <div class="step-component__status step-component__status--waiting" v-if="status.toUpperCase() === 'WAITING'"></div>
    <div class="step-component__status step-component__status--running" v-else-if="status.toUpperCase() === 'RUNNING'"><span class="loader"></span></div>
    <div class="step-component__status step-component__status--success" v-else-if="status.toUpperCase() === 'SUCCESS'"><img src="@/assets/success.svg" alt="V"></div>
    <div class="step-component__status step-component__status--failure" v-else-if="status.toUpperCase() === 'FAILURE'"><img src="@/assets/failure.svg" alt="X"></div>
    <p>{{ step }}</p>
  </div>
</template>

<style lang="scss">
.step-component {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    &__status {
        width: 25px;
        height: 25px;
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 50%;
        position: relative;
        &--waiting {
            background-color: #9c9797;
            position: relative;
            &::after {
                content: '';
                position: absolute;
                border-radius: 50%;
                top: 0;
                left: 0;
                transform: translate(3px, 3px);
                width: 19px;
                height: 19px;
                background-color: #fff;
            }
            & + p {
                color: #9c9797;
            }
        }
        &--running {
            background-color: #9c9797;
        }
        &--success {
            background-color: #5cc54e;
        }
        &--failure {
            background-color: #e14343;
            & > * {
                width: 15px!important;
                height: 15px!important;
            }
        }

        & > * {
            width: 20px;
            height: 20px;
        }
    }

    & p {
        position: absolute;
        top: 20px;
        left: 50%;
        transform: translateX(-50%);
        width: min-content;
        font-size: 12px;
        word-break: normal;
        text-align: center;
    }
}

.loader {
  width: 20px;
  height: 20px;
  display: inline-block;
  position: relative;
}
.loader::after,
.loader::before {
  content: '';  
  box-sizing: border-box;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid #FFF;
  position: absolute;
  left: 0;
  top: 0;
  animation: animloader 2s linear infinite;
}
.loader::after {
  animation-delay: 1s;
}

@keyframes animloader {
  0% {
    transform: scale(0);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

</style>
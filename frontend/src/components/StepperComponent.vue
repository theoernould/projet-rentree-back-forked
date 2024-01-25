<script setup lang="ts">
import StepComponent from '@/components/StepComponent.vue';
import { defineProps } from 'vue';

export type Step = {
    name: string;
    status: string;
};

const props = defineProps({
    steps: Array as () => Step[]
});
</script>

<template>
    <div class="stepper-component">
        <StepComponent
            v-for="(step, index) in props.steps"
            :key="index"
            :step="step.name"
            :status="step.status" />
    </div>
</template>

<style lang="scss">
.stepper-component {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 100%;
    position: relative;
    &::after {
        content: '';
        position: absolute;
        left: 0;
        right: 0;
        top: 12px;
        height: 1px;
        background-color: #9c9797;
        z-index: -1;
    }
}
</style>
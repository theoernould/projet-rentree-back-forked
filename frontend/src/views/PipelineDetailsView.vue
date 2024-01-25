<template>
  <div class="pipeline-details">
    <div v-if="pipeline">
      <p class="pipeline-details__path"><a :href=pipeline.repository.url class="pipeline-details__path__link"
                                           target="_blank">{{ formattedRepositoryName() }}</a> <span
          class="pipeline-details__path__separation">></span> {{ formattedBranchName() }}</p>
      <h2 class="pipeline-details__title">Pipeline <span class="strong">{{
          pipeline.id
        }} launched by {{ pipeline.user.name }} on {{
          formatDate(pipeline.timestamp)
        }} at {{ formatHour(pipeline.timestamp) }}</span></h2>
      <p class="pipeline-details__infos">Commit <a :href=pipeline.commit.url target="_blank">{{
          pipeline.commit.id
        }}</a> by <strong>{{ pipeline.commit.author }}</strong> on <strong>{{
          formatDate(pipeline.commit.timestamp)
        }}</strong> at <strong>{{ formatHour(pipeline.commit.timestamp) }}</strong> : "{{ pipeline.commit.message }}"
      </p>
      <div class="pipeline-details__stepper">
        <StepperComponent :steps="formattedSteps()"/>
      </div>
    </div>
    <div v-else>
      <p>Loading pipeline details...</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import type {Pipeline} from '@/models/pipeline.model';
import {usePipelineStore} from "@/stores/pipeline";
import router from "@/router";
import {useWebSocket} from "@/stores/websocket";
import StepperComponent, {type Step} from '@/components/StepperComponent.vue';

const props = defineProps({
  id: {
    type: String,
    required: true
  }
});

const pipeline = ref<Pipeline>({} as Pipeline);

const fetchPipelineDetails = async (pipelineId: string) => {
  const pipelineStore = usePipelineStore();
  pipeline.value = await pipelineStore.pipeline(pipelineId);
};

onMounted(async () => {
  const webSocket = useWebSocket();
  while (!webSocket.isConnected) {
    await new Promise(resolve => setTimeout(resolve, 100));
  }
  await initObserver(props.id);
});

router.beforeEach(async (to, from, next) => {
  console.log('before each', to, from);
  if (to.name === 'pipelineDetail') {
    const webSocket = useWebSocket();
    webSocket.unsubscribe(getPipelineTopic(from.params.id as string));
    await initObserver(to.params.id as string);
  }
  next();
});

function getPipelineTopic(pipelineId: string): string {
  return `/topic/pipelines/${pipelineId}`;
}

async function initObserver(pipelineId: string) {
  await fetchPipelineDetails(pipelineId);
  const webSocket = useWebSocket();
  webSocket.subscribe(getPipelineTopic(pipelineId), (data: any) => {
    const refreshedPipeline = JSON.parse(data.body) as Pipeline;
    pipeline.value = refreshedPipeline;
  });
}

function formattedSteps(): Step[] {
  if (pipeline.value.steps) {
    return Object.entries(pipeline.value.steps).map(([name, status]) => ({
      name: name.replace(/_/g, ' '),
      status: status
    }));
  }
  return [];
}

function formattedBranchName() {
  const parts = pipeline.value.branchName.split('/');
  return parts[parts.length - 1];
}

function formattedRepositoryName() {
  const parts = pipeline.value.repository.name.split('/');
  return parts[parts.length - 1];
}

function formatDate(timestamp: Date) {
  const date = new Date(timestamp);
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear();

  return day + "/" + month + "/" + year;
}

function formatHour(timestamp: Date) {
  const date = new Date(timestamp);
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');

  return hours + ":" + minutes;
}
</script>

<style lang="scss">
.pipeline-details {
  &__stepper {
    width: 90%;
    margin: 75px 20px 75px 20px;
  }

  &__infos {
    font-size: 12px;
    width: 100%;
  }

  &__path {
    opacity: 60%;

    &__separation {
      position: relative;
      margin: 12px;
      color: transparent;
      font-size: 26px;

      &::after {
        content: '>';
        position: absolute;
        top: 0;
        left: 0;
        color: black;
        transform: translateY(3px);
      }
    }

    &__link {
      color: black;
      transition: opacity ease-out .2s;

      &:hover {
        opacity: 40%;
      }
    }
  }
}
</style>
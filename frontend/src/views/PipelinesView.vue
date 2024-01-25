<script setup lang="ts">
import {onMounted, ref} from "vue";
import {type Pipelines, usePipelineStore} from "@/stores/pipeline";
import play from "@/assets/play.svg";
import pipelineSVG from "@/assets/pipeline.svg";
import type { Pipeline } from "@/models/pipeline.model";
import {useWebSocket} from "@/stores/websocket";

const pipelineStore = usePipelineStore();

const pipelines = ref<Pipelines>({} as Pipelines);

onMounted(async () => {
  pipelines.value = await pipelineStore.getPipelines();
  const webSocket = useWebSocket();
  while (!webSocket.isConnected) {
    await new Promise(resolve => setTimeout(resolve, 100));
  }
  webSocket.subscribe('/topic/pipelines', (data: any) => {
    const refreshedPipelines = JSON.parse(data.body) as Pipelines;
    pipelines.value = refreshedPipelines;
  });
});

function triggerPipeline() {
  pipelineStore.triggerPipeline();
}

function formattedDate(pipeline: Pipeline) {
  const commitDate = new Date(pipeline.timestamp);

  const day = commitDate.getDate().toString().padStart(2, '0');
  const month = (commitDate.getMonth() + 1).toString().padStart(2, '0');
  const year = commitDate.getFullYear();
  
  const hours = commitDate.getHours().toString().padStart(2, '0');
  const minutes = commitDate.getMinutes().toString().padStart(2, '0');

  return day + "/" + month + "/" + year + " " + hours + ":" + minutes;
}
</script>

<template>
  <div class="pipelines">
    <div class="side">
      <Button class="button" @click="triggerPipeline" :loading="pipelineStore.isLoading"><img :src="play" alt="play">Trigger manually</button>
      <ul class="menu">
        <li v-for="pipeline in pipelines" :key="pipeline.id">
          <router-link class="link" :to="`/pipelines/${pipeline.id}`">
            <img class="pipelineSVG" :src="pipelineSVG" alt="pipeline">
            <div>
              <p># {{ pipeline.number }}</p>
              <p>{{ formattedDate(pipeline) }}</p>
            </div>
          </router-link>
        </li>
      </ul>
    </div>
    
    <div class="content">
      <router-view/>
    </div>
  </div>
</template>

<style lang="scss">

.pipelines {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  .side {
    margin: 30px 30px 0 30px;
    position: sticky;
    height: min-content;
    top: 85px; // NE SURTOUT PAS MODIFIER
    .button {
      text-align: left;
      padding: 12px;
      width: 100%;
    }
    img {
      width: 20px;
      height: 20px;
      margin: 0 12px 0 4px;
    }
    .menu {
      margin: 30px 0 0 0;
      padding: 0 20px;
      max-height: 70vh;
      overflow-y: auto;
      overflow-x: hidden;
      .link {
        text-decoration: none;
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 5px 15px 5px 5px;
      }
      & li {
        list-style: none;
        margin: 5px 0;

        transition: background-color ease-in .2s;
        border-radius: 10px;
        &:hover {
          background-color: rgba(#000000, .1);
        }
        p {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          font-size: 12px;
          color: black;
          text-decoration: none;
          margin: 0;
        }
        .pipelineSVG {
          width: 30px;
          height: 30px;
        }
      }
    }
  }
  .content {
    margin: 10px 50px;
    height: min-content;
  }
}
</style>

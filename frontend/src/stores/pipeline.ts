import {defineStore} from 'pinia'
import axios from "axios";
import type {Pipeline} from "@/models/pipeline.model";
import {api} from "@/main";

export type Pipelines =  Pipeline[];

interface State {
    isLoading: boolean
}

export const usePipelineStore = defineStore('pipelines', {
    state: (): State => ({
        isLoading: false
    }),
    actions: {
        async getPipelines(): Promise<Pipelines> {
            return (await axios.get(api.url + 'pipelines')).data as Pipelines;
        },
        pipeline: async (id: string): Promise<Pipeline> => {
            return (await axios.get(`${api.url}pipelines/${id}`)).data as Pipeline;
        },
        async triggerPipeline() {
            this.isLoading = true;
            const response = await axios.post(`${api.url}pipelines`);
            if (response.status === 200) {
                // TODO show success message
            } else {
                // TODO show error message
            }
            this.isLoading = false;
            return response;
        }
    }
})

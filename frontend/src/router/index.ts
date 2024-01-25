import {createRouter, createWebHashHistory} from 'vue-router'
import HomeView from '../views/PipelinesView.vue'
import PipelinesView from "@/views/PipelinesView.vue";
import PipelineDetailsView from "@/views/PipelineDetailsView.vue";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/pipelines',
            name: 'pipelines',
            component: PipelinesView,
            children: [
                {
                    path: ':id',
                    name: 'pipelineDetail',
                    component: PipelineDetailsView,
                    props: true
                }
            ]
        }
    ]
})

export default router

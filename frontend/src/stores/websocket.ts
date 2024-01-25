import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {defineStore} from "pinia";
import {api} from "@/main";

interface State {
    stompClient: any;
    subscriptions: Map<string, any>;
}

export const useWebSocket = defineStore('websocket', {
    state: (): State => ({
        stompClient: null as any,
        subscriptions: new Map<string, any>(),
    }) as State,
    getters: {
        isConnected(): boolean {
            return this.stompClient?.connected;
        }
    },
    actions: {
        connect() {
            const socket = new SockJS(api.websocket);
            this.stompClient = Stomp.over(socket);
            this.stompClient.connect({});
        },
        disconnect() {
            if (this.stompClient) {
                this.stompClient.disconnect();
            }
        },
        subscribe(topic: string, callback: (data: any) => void) {
            const subscription = this.stompClient.subscribe(topic, callback);
            this.subscriptions.set(topic, subscription);
            console.log(`Subscribed to ${topic}`);
            console.log(this.subscriptions);
        },
        unsubscribe(topic: string) {
            const subscription = this.subscriptions.get(topic);
            if (subscription) {
                subscription.unsubscribe();
                this.subscriptions.delete(topic);
                console.log(`Unsubscribed from ${topic}`);
                console.log(this.subscriptions);
            }
        }
    }
});
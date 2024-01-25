import {defineStore} from 'pinia'
import axios from "axios";
import type {User} from "@/models/user.model";
import {api} from "@/main";

export const useUserStore = defineStore('user', {
    getters: {
        user: async (): Promise<User> => {
            const user: any = (await axios.get(api.url + 'user')).data as User;
            return user;
        }
    }
})

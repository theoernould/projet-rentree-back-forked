import type {User} from "@/models/user.model";
import type {Commit} from "@/models/commit.model";
import type {Repository} from "@/models/repository.model";

export interface Pipeline {
    id: string;
    number: number;
    user: User;
    branchName: string;
    commit: Commit;
    repository: Repository;
    timestamp: Date;
    steps: { [key in string]: string };
}
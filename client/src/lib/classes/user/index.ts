import type {Program} from "$lib/classes/program";
import type {Team} from "$lib/classes/team";

export interface UserDTO {
    id: string,
    athleteData: AthleteData | null,
    coachData: CoachData | null,
    createdAt: string,
    email: string,
    roles: any[],
    updatedAt: string,
    username: string
}

export interface AthleteData {
    id: string,
    name: string,
    currentProgram: Program,
    programs: Program[],
    coach: CoachData
}

export interface CoachData {
    id: string,
    programs: Program[],
    athletes: AthleteData[],
    teams: Team[]

}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()

        user.athleteData = userDTO.athleteData;
        user.coachData = userDTO.coachData;
        user.createdAt = new Date(userDTO.createdAt);
        user.updatedAt = new Date(userDTO.updatedAt);
        user.email = userDTO.email;
        user.id = userDTO.id;
        user.username = userDTO.username;
        user.roles = userDTO.roles;

        return user;
    }

    athleteData: AthleteData | null = null;
    coachData: CoachData | null = null;
    createdAt: Date = new Date();
    email: string = '';
    id: string = '';
    roles: any[] = [];
    updatedAt: Date = new Date();
    username: string = '';
}
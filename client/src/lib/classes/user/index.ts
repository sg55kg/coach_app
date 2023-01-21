import dayjs, {Dayjs} from "dayjs";
import {CoachData, type CoachDataDTO} from "$lib/classes/user/coach";
import {AthleteData, type AthleteDataDTO} from "$lib/classes/user/athlete";

export interface UserDTO {
    id: string,
    athleteData: AthleteDataDTO | null,
    coachData: CoachDataDTO | null,
    createdAt: string,
    email: string,
    roles: any[],
    updatedAt: string,
    username: string
}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()

        user.athleteData = userDTO.athleteData ? AthleteData.createFrom(userDTO.athleteData) : null
        user.coachData = userDTO.coachData ? CoachData.createFrom(userDTO.coachData) : null
        user.createdAt = dayjs(userDTO.createdAt)
        user.updatedAt = dayjs(userDTO.updatedAt)
        user.email = userDTO.email
        user.id = userDTO.id
        user.username = userDTO.username
        user.roles = userDTO.roles

        return user
    }

    athleteData: AthleteData | null = null
    coachData: CoachData | null = null
    createdAt: Dayjs = dayjs()
    email: string = ''
    id: string = ''
    roles: any[] = []
    updatedAt: Dayjs = dayjs()
    username: string = ''
}
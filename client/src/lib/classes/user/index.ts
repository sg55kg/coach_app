import dayjs, {Dayjs} from "dayjs";
import {CoachData, type CoachDataDTO} from "$lib/classes/user/coach";
import {AthleteData, type AthleteDataDTO} from "$lib/classes/user/athlete";
import type {ChatMemberDTO} from "$lib/classes/chat";
import {ChatMember} from "$lib/classes/chat";
import {UserPreferences, type UserPreferencesDTO} from "./preferences";

export interface UserDTO {
    id: string,
    athleteData: AthleteDataDTO | null,
    coachData: CoachDataDTO | null,
    createdAt: string,
    email: string,
    roles: any[],
    updatedAt: string,
    username: string,
    members: Array<ChatMemberDTO>,
    photoUrl: string,
    preferences: UserPreferencesDTO
}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()
        console.log(userDTO)
        user.athleteData = userDTO.athleteData ? AthleteData.createFrom(userDTO.athleteData) : null
        user.coachData = userDTO.coachData ? CoachData.createFrom(userDTO.coachData) : null
        user.createdAt = userDTO.createdAt ? dayjs(userDTO.createdAt) : dayjs()
        user.updatedAt = dayjs(userDTO.updatedAt)
        user.email = userDTO.email
        user.id = userDTO.id
        user.username = userDTO.username
        user.roles = userDTO.roles
        user.members = userDTO.members ? userDTO.members.map(m => ChatMember.createFrom(m)) : []
        user.photoUrl = userDTO.photoUrl
        user.preferences = UserPreferences.createFrom(userDTO.preferences)

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
    members: ChatMember[] = []
    photoUrl: string = ''
    preferences: UserPreferences = new UserPreferences()
}
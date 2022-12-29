import type {Program} from "$lib/classes/program"
import type {Team} from "$lib/classes/team"

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
    coach: CoachData,
    records: AthleteRecord
}

export interface CoachData {
    id: string,
    programs: Program[],
    athletes: AthleteData[],
    teams: Team[]

}

export interface AthleteRecord {
    id: string,
    snatch: number
    cleanAndJerk: number
    jerk: number
    clean: number
    backSquat: number
    frontSquat: number
    deadlift: number
    snatchDeadlift: number
    pushPress: number
    strictPress: number
    bentOverRow: number
    powerSnatch: number
    powerClean: number
    powerJerk: number
    hangSnatch: number
    hangPowerSnatch: number
    hangPowerClean: number
    blockSnatch: number
    blockClean: number
    blockPowerSnatch: number
    blockPowerClean: number
    cleanPull: number
    snatchPull: number
    snatchHighPull: number
    benchPress: number
    pendlayRows: number
    snatchPushPress: number
    overheadSquat: number
    squatJerk: number
    maxPullUps: number
    weightedPullUp: number
    maxChinUps: number
    weightedChinUp: number
    pauseSnatch: number
    pauseClean: number
    sandbagCarryMeters: number
    sandbagCarryWeight: number
    farmerCarryMeters: number
    farmerCarryWeight: number
}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()

        user.athleteData = userDTO.athleteData
        user.coachData = userDTO.coachData
        user.createdAt = new Date(userDTO.createdAt)
        user.updatedAt = new Date(userDTO.updatedAt)
        user.email = userDTO.email
        user.id = userDTO.id
        user.username = userDTO.username
        user.roles = userDTO.roles

        return user
    }

    athleteData: AthleteData | null = null
    coachData: CoachData | null = null
    createdAt: Date = new Date()
    email: string = ''
    id: string = ''
    roles: any[] = []
    updatedAt: Date = new Date()
    username: string = ''
}
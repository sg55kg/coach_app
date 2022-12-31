import type {Program} from "$lib/classes/program"
import {Team, type TeamDTO} from "$lib/classes/team"
import dayjs, {Dayjs} from "dayjs";

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

export interface AthleteDataDTO {
    id: string,
    name: string,
    currentProgram: Program,
    programs: Program[],
    coach: CoachDataDTO,
    records: AthleteRecordDTO
    team: TeamDTO
}

export interface CoachDataDTO {
    id: string,
    programs: Program[],
    athletes: AthleteDataDTO[],
    teams: TeamDTO[]
}

export class AthleteData {
    id: string = ''
    name: string = ''
    currentProgram: Program | null = null
    programs: Program[] = []
    coach: CoachData | null = null
    records: AthleteRecord | null = null
    team: Team | null = null

    public static createFrom(data: AthleteDataDTO) {
        const athlete = new AthleteData()

        athlete.id = data.id
        athlete.name = data.name
        athlete.currentProgram = data.currentProgram
        athlete.programs = data.programs
        athlete.coach = data.coach ? CoachData.createFrom(data.coach) : null
        athlete.records = new AthleteRecord(data.records)
        athlete.team = data.team ? Team.createFrom(data.team) : null

        return athlete
    }
}

export class CoachData {
    id: string = ''
    programs: Program[] = []
    athletes: AthleteData[] = []
    teams: Team[] = []

    public static createFrom(data: CoachDataDTO) {
        const coach = new CoachData()

        coach.id = data.id
        coach.programs = data.programs
        coach.athletes = data.athletes.map(athlete => AthleteData.createFrom(athlete))
        coach.teams = data.teams ? data.teams.map(t => Team.createFrom(t)) : []

        return coach
    }

}

export interface AthleteRecordDTO {
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

export class AthleteRecord {

    public records: Map<string,number> = new Map<string, number>()

    constructor(data: AthleteRecordDTO) {
        for (const [key, value] of Object.entries(data)) {
            this.records.set(key.toUpperCase(), value)
        }
    }
}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()
        console.log('coach', userDTO.coachData)

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
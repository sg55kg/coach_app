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
    records: AthleteRecordDTO[]
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
    records: AthleteRecord[] = []
    team: Team | null = null

    public static createFrom(data: AthleteDataDTO) {
        const athlete = new AthleteData()

        athlete.id = data.id
        athlete.name = data.name
        athlete.currentProgram = data.currentProgram
        athlete.programs = data.programs
        athlete.coach = data.coach ? CoachData.createFrom(data.coach) : null
        athlete.records = data.records.map(r => new AthleteRecord(r))
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
    createdAt: string,
    lastUpdated: string,
    snatch: number
    clean_and_jerk: number
    jerk: number
    clean: number
    back_squat: number
    front_squat: number
    deadlift: number
    snatch_deadlift: number
    push_press: number
    strict_press: number
    bent_over_row: number
    power_snatch: number
    power_clean: number
    power_jerk: number
    hang_snatch: number
    hang_power_snatch: number
    hang_power_clean: number
    block_snatch: number
    block_clean: number
    block_power_snatch: number
    block_power_clean: number
    clean_pull: number
    snatch_pull: number
    snatch_high_pull: number
    bench_press: number
    pendlay_rows: number
    snatch_push_press: number
    overhead_squat: number
    squat_jerk: number
    max_pull_ups: number
    weighted_pull_up: number
    max_chin_ups: number
    weighted_chin_up: number
    pause_snatch: number
    pause_clean: number
    sandbag_carry_meters: number
    sandbag_carry_weight: number
    farmer_carry_meters: number
    farmer_carry_weight: number
}

export class AthleteRecord {

    public id: string = ''
    public records: Map<string,number> = new Map<string, number>()
    public createdAt: Dayjs = dayjs()
    public lastUpdated: string = ''

    constructor(data: AthleteRecordDTO) {
        for (const [key, value] of Object.entries(data)) {
            if (key === 'createdAt') {
                this.createdAt = dayjs(value)
            } else if (key === 'lastUpdated') {
                this.lastUpdated = value;
            } else if (key === 'id') {
                this.id = value;
            } else {
                console.log(key)
                this.records.set(key.toLowerCase(), value)
            }
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
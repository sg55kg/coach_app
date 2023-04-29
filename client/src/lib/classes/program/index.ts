import {Day} from "./day";
import type {DayDTO} from "./day";
import type {TeamDTO} from "$lib/classes/team";
import type {CoachDataDTO} from "$lib/classes/user/coach";
import dayjs, {Dayjs} from "dayjs";

export interface IProgram {
    id?: string,
    name: string,
    startDate: Dayjs | null,
    endDate: Dayjs | null,
    days?: Day[],
    athleteId?: string,
    coachId?: string,
    isCurrent?: boolean
}

export interface ProgramDTO {
    id: string,
    name: string,
    startDate: string,
    endDate: string,
    days: DayDTO[],
    coachId?: string,
    teamId?: string,
    athleteId?: string,
    lastEnteredDay?: string,
    athleteName?: string,
    updatedAt: string,

}




export class Program implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new Program()
        console.log(programDTO)

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = dayjs(programDTO.startDate)
        program.endDate = dayjs(programDTO.endDate)
        program.days = programDTO.days.map(d => Day.createFrom(d)).sort((a, b) => a.date.valueOf() - b.date.valueOf())
        program.coachId = programDTO.coachId ? programDTO.coachId : ''
        program.teamId = programDTO.teamId ? programDTO.teamId : ''
        program.athleteId = programDTO.athleteId ? programDTO.athleteId : ''
        program.updatedAt = programDTO.updatedAt ? dayjs(programDTO.updatedAt) : dayjs()

        return { ...program }
    }

    id: string = ''
    name: string = ''
    startDate: Dayjs = dayjs()
    endDate: Dayjs = dayjs()
    days: Day[] = []
    athleteId: string = ''
    coachId: string = ''
    teamId: string = ''
    isCurrent: boolean = true
    updatedAt: Dayjs = dayjs()
}

export class DisplayProgram implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new DisplayProgram()

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = programDTO.startDate ? dayjs(programDTO.startDate) : null
        program.endDate = programDTO.endDate ? dayjs(programDTO.endDate) : null
        program.lastEnteredDay = new Date(programDTO.lastEnteredDay!)
        program.athleteId = programDTO.athleteId ? programDTO.athleteId : ''
        program.athleteName = programDTO.athleteName ? programDTO.athleteName : ''

        return program
    }

    id: string = ''
    name: string = ''
    startDate: Dayjs | null = dayjs()
    endDate: Dayjs | null = dayjs()
    lastEnteredDay: Date = new Date()
    athleteId: string = ''
    athleteName: string = ''
    //coach: string = ''
}
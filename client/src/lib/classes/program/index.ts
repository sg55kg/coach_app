import {Day} from "./day";
import type {DayDTO} from "./day";
import type {CoachDataDTO} from "$lib/classes/user";

export interface IProgram {
    id?: string,
    name: string,
    startDate: Date,
    endDate: Date,
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
    coach?: CoachDataDTO,
    lastEnteredDay?: string
}



export enum WeightIntensity {
    LIGHT = "LIGHT",
    MODERATE = "MODERATE",
    HEAVY = "HEAVY",
    BODY_WEIGHT = "BODY_WEIGHT",
    NONE = "NONE"
}




export class Program implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new Program()

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = new Date(programDTO.startDate)
        program.endDate = new Date(programDTO.endDate)
        program.days = programDTO.days.map(d => Day.build(d)).sort((a, b) => a.date.valueOf() - b.date.valueOf())
        program.coachId = programDTO.coach ? programDTO.coach.id : ''

        return { ...program }
    }

    id: string = ''
    name: string = ''
    startDate: Date = new Date()
    endDate: Date = new Date()
    days: Day[] = []
    athleteId: string = ''
    coachId: string = ''
    isCurrent: boolean = true
}

export class DisplayProgram implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new DisplayProgram()

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = new Date(programDTO.startDate)
        program.endDate = new Date(programDTO.endDate)
        program.lastEnteredDay = new Date(programDTO.lastEnteredDay!)

        return program
    }

    id: string = ''
    name: string = ''
    startDate: Date = new Date()
    endDate: Date = new Date()
    lastEnteredDay: Date = new Date()
    //coach: string = ''
}
import {Day} from "../day";
import type {DayDTO} from "../day";

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
}



export enum WeightIntensity {
    LIGHT = "LIGHT",
    MODERATE = "MODERATE",
    HEAVY = "HEAVY",
    BODY_WEIGHT = "BODY_WEIGHT",
    NONE = "NONE"
}

export class Exercise {
    id: string = ''
    name: string = ''
    sets: number = 0
    repsPerSet: number = 0
    notes: string = ''
    weightIntensity: WeightIntensity = WeightIntensity.NONE
    isMax: boolean = false
}



export class Program implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new Program()

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = new Date(programDTO.startDate)
        program.endDate = new Date(programDTO.endDate)
        program.days = programDTO.days.map(d => Day.build(d))
        //program.coach = programDTO.coach

        return { ...program }
    }

    id: string = ''
    name: string = ''
    startDate: Date = new Date()
    endDate: Date = new Date()
    days: any[] = []
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
        //program.coach = programDTO.coach

        return { ...program }
    }

    id: string = ''
    name: string = ''
    startDate: Date = new Date()
    endDate: Date = new Date()
    //coach: string = ''
}
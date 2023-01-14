import type {Exercise} from "$lib/classes/program/exercise";

export interface IDay {
    id?: string,
    date: Date,
    exercises: Exercise[],
    isRestDay: boolean,
    warmUp: WarmUp | null
}

export interface DayDTO {
    id: string,
    date: string,
    exercises: Exercise[],
    isRestDay: boolean,
    warmUp: WarmUp | null
}

export class Day implements IDay {
    static build = (dayDTO: DayDTO) => {
        let day = new Day()

        day.id = dayDTO.id
        day.date = new Date(dayDTO.date)
        day.exercises = dayDTO.exercises
        day.isRestDay = dayDTO.isRestDay
        day.warmUp = dayDTO.warmUp

        return { ...day }
    }

    id: string = ''
    date: Date = new Date()
    exercises: Exercise[] = []
    isRestDay: boolean = false
    warmUp: WarmUp | null = null
}

export class WarmUp {
    id: string = ''
    instructions: string = ''
}
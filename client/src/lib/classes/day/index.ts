import type {Exercise} from "$lib/classes/program";

export interface IDay {
    id?: string,
    date: Date,
    exercises: Exercise[],
    isRestDay: boolean
}

export interface DayDTO {
    id: string,
    date: string,
    exercises: Exercise[],
    isRestDay: boolean
}

export class Day implements IDay {
    static build = (dayDTO: DayDTO) => {
        let day = new Day()

        day.id = dayDTO.id
        day.date = new Date(dayDTO.date)
        day.exercises = dayDTO.exercises
        day.isRestDay = dayDTO.isRestDay

        return { ...day }
    }

    id: string = ''
    date: Date = new Date()
    exercises: Exercise[] = []
    isRestDay: boolean = false
}

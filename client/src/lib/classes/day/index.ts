import type {Exercise} from "$lib/classes/program";

export interface IDay {
    id?: string,
    date: Date,
    exercises: Exercise[]
}

export interface DayDTO {
    id: string,
    date: string,
    exercises: Exercise[]
}

export class Day implements IDay {
    static build = (dayDTO: DayDTO) => {
        let day = new Day()

        day.id = dayDTO.id
        day.date = new Date(dayDTO.date)
        day.exercises = dayDTO.exercises

        return { ...day }
    }

    id: string = ''
    date: Date = new Date()
    exercises: Exercise[] = []
}

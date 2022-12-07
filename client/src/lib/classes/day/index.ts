export interface IDay {
    id?: string,
    date: Date,
    exercises: any[]
}

export interface DayDTO {
    id: string,
    date: string,
    exercises: any[]
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
    exercises: any[] = []
}

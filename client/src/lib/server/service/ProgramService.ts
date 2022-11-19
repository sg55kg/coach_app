import axios from "axios";

export interface IProgram {
    id?: string,
    name: string,
    startDate: Date,
    endDate: Date,
    days: Day[],
}

export interface ProgramDTO {
    id: string,
    name: string,
    startDate: string,
    endDate: string,
    days: DayDTO[]
}

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

export class Program implements IProgram {
    static build = (programDTO: ProgramDTO) => {
        let program = new Program()

        program.id = programDTO.id
        program.name = programDTO.name
        program.startDate = new Date(programDTO.startDate)
        program.endDate = new Date(programDTO.endDate)
        program.days = programDTO.days.map(d => Day.build(d))

        return { ...program }
    }

    id: string = ''
    name: string = ''
    startDate: Date = new Date()
    endDate: Date = new Date()
    days: any[] = []
}

export class ProgramService {

    static getPrograms = async () => {
        const { data } = await axios.get<ProgramDTO[]>(`http://localhost:8080/api/programs`)
        return data.map(p => Program.build(p))
    }

    static getProgram = async (id: string) => {
        const { data } = await axios.get<ProgramDTO>(`http://localhost:8080/api/programs/${id}`)
        return Program.build(data)
    }
}


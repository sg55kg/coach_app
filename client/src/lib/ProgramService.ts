import axios from "axios";
import {DisplayProgram, Program} from "./classes/program";
import type {ProgramDTO} from "./classes/program"

export class ProgramService {

    static getPrograms = async () => {
        const { data } = await axios.get<ProgramDTO[]>(`http://localhost:8080/api/programs`)
        console.log(data)
        return data.map(p => DisplayProgram.build(p))
    }

    static getProgram = async (id: string) => {
        const { data } = await axios.get<ProgramDTO>(`http://localhost:8080/api/programs/${id}`)
        return Program.build(data)
    }
}


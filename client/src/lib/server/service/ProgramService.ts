export class ProgramService {

    static getPrograms = async () => {
        const res = await fetch(`http://localhost:8080/api/programs`, {
            method: 'GET',
            headers: { 'Content-Type':'application/json' }
        })
        const programs = await res.json()
        return programs
    }

    static getProgram = async (id: string) => {
        const res = await fetch(`http://localhost:8080/api/programs/${id}`, {
            method: 'GET',
            headers: { 'Content-Type':'application/json' }
        })
        const program = await res.json()
        return program
    }
}


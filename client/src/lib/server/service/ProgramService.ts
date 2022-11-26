export class ProgramService {

    static getPrograms = async () => {
        const res = await fetch(`http://localhost:8180/api/programs`, {
            method: 'GET',
            headers: { 'Content-Type':'application/json' }
        })
        console.log(res)
        const programs = await res.json()
        return programs
    }

    static getProgram = async (id: string) => {
        const res = await fetch(`http://localhost:8180/api/programs/${id}`, {
            method: 'GET',
            headers: { 'Content-Type':'application/json' }
        })
        const program = await res.json()
        return program
    }
}


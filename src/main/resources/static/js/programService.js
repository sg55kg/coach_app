
class ProgramService {

    static async saveProgram(program) {
        try {
            const { text } = await fetch(`http://localhost:8080/api/programs`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(program)
            })
            const str = await text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }

    static async getAllPrograms() {
        try {
            const { text } = await fetch(`http://localhost:8080/api/programs`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            })
            const str = await text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }

    static async getProgram(id) {
        try {
            const { text } = await fetch(`http://localhost:8080/api/programs/${id}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            })
            const str = await text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }
}
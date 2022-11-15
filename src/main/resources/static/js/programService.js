
class ProgramService {

    static async saveProgram(program) {
        try {
            const res = await fetch(`http://localhost:8080/api/programs`, {
                method: 'POST',
                mode: 'same-origin',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(program)
            })
            const str = await res.text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }

    static async getAllPrograms() {
        try {
            const res = await fetch(`http://localhost:8080/api/programs`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            })
            const str = await res.text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }

    static async getProgram(id) {
        try {
            const res = await fetch(`http://localhost:8080/api/programs/${id}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            })
            const str = await res.text()
            return JSON.parse(str)
        } catch (e) {
            console.log(e)
        }
    }

    static async deleteProgram(id) {
        try {
            const res = await fetch(`http://localhost:8080/api/programs/${id}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' }
            })
            return res.status
        } catch (e) {
            console.log(e)
        }
    }
}
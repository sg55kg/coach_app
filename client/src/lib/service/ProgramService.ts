import type {Auth0Client} from "@auth0/auth0-spa-js";
import {Program} from "../classes/program";
import type {ProgramDTO} from "../classes/program"
import type {User} from "../classes/user";


export class ProgramService {

    static getCoachPrograms = async (client: Auth0Client, coachId: string) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`http://localhost:8180/api/programs/coach/${coachId}`, {
            method: 'GET',
            headers: { 'Authorization':'Bearer ' + accessToken }
        })

        const programsResponse = await res.json()
        return programsResponse.map((p: ProgramDTO) => Program.build(p))
    }

    static getProgram = async (id: string) => {
        const res = await fetch(`http://localhost:8180/api/programs/${id}`, {
            method: 'GET',
            headers: { 'Content-Type':'application/json' }
        })
        return await res.json()
    }

    static createProgram = async (client: Auth0Client, program: Program, userDB: User) => {
        const accessToken = await client.getTokenSilently()
        const id = userDB.coachData.id


        const res = await fetch(`http://localhost:8180/api/programs/coach/${id}`, {
            method: 'POST',
            body: JSON.stringify({ ...program, coach: { id: userDB.coachData.id }}),
            headers: { 'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json' }
        })

        const savedProgram = await res.json()

        return savedProgram
    }
}


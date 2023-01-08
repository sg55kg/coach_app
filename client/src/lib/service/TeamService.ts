import type {Team} from "$lib/classes/team";
import type {Auth0Client} from "@auth0/auth0-spa-js";


export class TeamService {

    static createTeam = async (client: Auth0Client, team: Team) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + accessToken },
            body: JSON.stringify(team)
        })
        
        return await res.json()
    }

    static getDisplayTeams = async (client: Auth0Client) => {
        const accessToken = await client.getTokenSilently()

        const res = await fetch(`${import.meta.env.VITE_SERVER_URL}api/teams/`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + accessToken }
        })

        return await res.json()
    }
}
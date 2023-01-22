import {Team} from "$lib/classes/team";
import type {Auth0Client} from "@auth0/auth0-spa-js";


export class TeamService {

    static createTeam = async (team: Team) => {

        const res = await fetch(`/api/team`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(team)
        })
        
        return await res.json()
    }

    static getDisplayTeams = async () => {

        const res = await fetch(`/api/team`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })

        return await res.json()
    }

    static getTeam = async (teamId: string) => {

        const res = await fetch(`/api/team/${teamId}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        })

        return Team.createFrom(await res.json())
    }

    static updateTeam = async (team: Team) => {

        const res = await fetch(`/api/team/${team.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(team)
        })

        return Team.createFrom(await res.json())
    }
}
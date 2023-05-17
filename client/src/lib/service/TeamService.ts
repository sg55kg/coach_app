import {Team} from "$lib/classes/team";
import type {TeamFinance} from "../classes/team/teamFinance";
import {goto} from "$app/navigation";

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
        const res = await fetch(`/api/team/${teamId}`)
        console.log('this one vvv')
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

    static connectStripeAccount = async (req: any): Promise<TeamFinance> => {
        const res = await fetch(`/api/stripe/connect`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(req)
        })

        return await res.json() // TODO: add typing
    }

    static getStripeRedirectUrl = async (stripeConnectId: string, teamId: string) => {
        const res = await fetch(`/api/stripe/${stripeConnectId}/onboard?returnUrl=home/coach/team/${teamId}/settings`, {
            method: 'GET',
        })

        const redirectUrl = await res.text()
        if (redirectUrl.includes('stripe')) {
            await goto(redirectUrl)
        } else {
            // TODO: find a better way to handle checking the redirect URL is correct.. error code?
            throw new Error('Could not retrieve redirect url')
        }
    }

    static getStripeAccount = async (stripeConnectId: string) => {
        const res = await fetch(`/api/stripe/${stripeConnectId}`, {
            method: 'GET'
        })

        return JSON.parse(await res.text())
    }
}
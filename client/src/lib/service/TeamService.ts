import { Team } from '$lib/classes/team';
import type { TeamFinance } from '../classes/team/teamFinance';
import { goto } from '$app/navigation';
import { srGet, srPost, srPut } from './helpers/serviceRequest';
import type { DisplayTeamDTO, TeamDTO } from '../classes/team';
import { DisplayTeam } from '../classes/team';

export class TeamService {
    static createTeam = async (team: Team) => {
        const { data } = await srPost<TeamDTO>(`/api/team`, team);
        return Team.createFrom(data);
    };

    static getDisplayTeams = async () => {
        const { data } = await srGet<DisplayTeamDTO[]>(`/api/team`);
        return data.map(d => DisplayTeam.createFrom(d));
    };

    static getTeam = async (teamId: string) => {
        const { data } = await srGet<TeamDTO>(`/api/team/${teamId}`);
        return Team.createFrom(data);
    };

    static updateTeam = async (team: Team) => {
        const { data } = await srPut<TeamDTO>(`/api/team/${team.id}`, team);
        return Team.createFrom(data);
    };

    static connectStripeAccount = async (req: any): Promise<TeamFinance> => {
        const { data } = await srPost<TeamFinance>(`/api/stripe/connect`, req);
        return data;
    };

    static getStripeRedirectUrl = async (
        stripeConnectId: string,
        teamId: string
    ) => {
        const { data } = await srGet<string>(
            `/api/stripe/${stripeConnectId}/onboard?returnUrl=home/coach/team/${teamId}/settings`,
            true
        );

        if (data.includes('stripe')) {
            await goto(data);
        } else {
            // TODO: find a better way to handle checking the redirect URL is correct.. error code?
            throw new Error('Could not retrieve redirect url');
        }
    };

    static getStripeAccount = async (stripeConnectId: string) => {
        const { data } = await srGet<any>(
            `/api/stripe/${stripeConnectId}`,
            false
        );
        return data; // TODO: add typing for stripe account response
    };
}

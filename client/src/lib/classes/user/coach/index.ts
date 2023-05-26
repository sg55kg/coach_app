// import {DisplayProgram, type ProgramDTO} from "$lib/classes/program";
import { Team, type TeamDTO } from '$lib/classes/team';
import { AthleteData, type AthleteDataDTO } from '$lib/classes/user/athlete';
import type { DisplayProgram, ProgramDTO } from '../../program';

export interface CoachDataDTO {
    id: string;
    programs: ProgramDTO[];
    athletes: AthleteDataDTO[];
    teams: TeamDTO[];
}

export class CoachData {
    id: string = '';
    programs: DisplayProgram[] = [];
    athletes: AthleteData[] = [];
    teams: Team[] = [];

    public static createFrom(data: CoachDataDTO) {
        const coach = new CoachData();

        coach.id = data.id;
        // coach.programs = data.programs.map(p => DisplayProgram.build(p))
        coach.athletes = data.athletes.map(athlete =>
            AthleteData.createFrom(athlete)
        );
        coach.teams = data.teams ? data.teams.map(t => Team.createFrom(t)) : [];

        return coach;
    }
}

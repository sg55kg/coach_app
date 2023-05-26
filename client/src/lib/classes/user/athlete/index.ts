import { Program } from '$lib/classes/program';
import { DisplayTeam } from '$lib/classes/team';
import type {
    AthleteRecord,
    AthleteRecordDTO,
} from '$lib/classes/user/athlete/records';
import type { ProgramDTO } from '../../program';

export interface AthleteDataDTO {
    id: string;
    name: string;
    currentProgram: ProgramDTO;
    programs: Program[];
    coachId: string;
    records: AthleteRecordDTO[];
    team: DisplayTeam | null;
}

export class AthleteData {
    id: string = '';
    name: string = '';
    currentProgram: Program | null = null;
    programs: Program[] = [];
    coachId: string = '';
    records: AthleteRecord[] = [];
    team: DisplayTeam | null = null;

    public static createFrom(data: AthleteDataDTO) {
        const athlete = new AthleteData();

        athlete.id = data.id;
        athlete.name = data.name;
        athlete.currentProgram = data.currentProgram
            ? Program.build(data.currentProgram)
            : null;
        athlete.programs = data.programs;
        athlete.coachId = data.coachId ? data.coachId : '';

        athlete.team = data.team ? DisplayTeam.createFrom(data.team) : null;

        return athlete;
    }
}

export { athleteRecordFields } from '$lib/classes/user/athlete/records';
export { AthleteRecord } from '$lib/classes/user/athlete/records';
export type { AthleteRecordDTO } from '$lib/classes/user/athlete/records';

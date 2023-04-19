import {Program} from "$lib/classes/program";
import {CoachData, type CoachDataDTO} from "$lib/classes/user/coach";
import {DisplayTeam, Team, type TeamDTO} from "$lib/classes/team";
import {AthleteRecord, type AthleteRecordDTO} from "$lib/classes/user/athlete/records";
import type {ProgramDTO} from "../../program";

export interface AthleteDataDTO {
    id: string,
    name: string,
    currentProgram: ProgramDTO,
    programs: Program[],
    coachId: string,
    records: AthleteRecordDTO[]
    team: DisplayTeam | null
}

export class AthleteData {
    id: string = ''
    name: string = ''
    currentProgram: Program | null = null
    programs: Program[] = []
    coachId: string = ''
    records: AthleteRecord[] = []
    team: DisplayTeam | null = null

    public static createFrom(data: AthleteDataDTO) {
        const athlete = new AthleteData()

        athlete.id = data.id
        athlete.name = data.name
        athlete.currentProgram = Program.build(data.currentProgram)
        athlete.programs = data.programs
        athlete.coachId = data.coachId ? data.coachId : ''
        athlete.records = data.records ?
            data.records
                .map(r => new AthleteRecord(r))
                .sort((a, b) => a.lastUpdated.toDate().valueOf() - b.lastUpdated.toDate().valueOf()) :
            []
        athlete.team = data.team ? DisplayTeam.createFrom(data.team) : null

        return athlete
    }
}

export {athleteRecordFields} from "$lib/classes/user/athlete/records";
export {AthleteRecord} from "$lib/classes/user/athlete/records";
export type {AthleteRecordDTO} from "$lib/classes/user/athlete/records";
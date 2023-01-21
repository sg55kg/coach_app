import type {Program} from "$lib/classes/program";
import {CoachData, type CoachDataDTO} from "$lib/classes/user/coach";
import {Team, type TeamDTO} from "$lib/classes/team";
import {AthleteRecord, type AthleteRecordDTO} from "$lib/classes/user/athlete/records";

export interface AthleteDataDTO {
    id: string,
    name: string,
    currentProgram: Program,
    programs: Program[],
    coach: CoachDataDTO,
    records: AthleteRecordDTO[]
    team: TeamDTO
}

export class AthleteData {
    id: string = ''
    name: string = ''
    currentProgram: Program | null = null
    programs: Program[] = []
    coach: CoachData | null = null
    records: AthleteRecord[] = []
    team: Team | null = null

    public static createFrom(data: AthleteDataDTO) {
        const athlete = new AthleteData()

        athlete.id = data.id
        athlete.name = data.name
        athlete.currentProgram = data.currentProgram
        athlete.programs = data.programs
        athlete.coach = data.coach ? CoachData.createFrom(data.coach) : null
        athlete.records = data.records ? data.records
            .map(r => new AthleteRecord(r))
            .sort((a, b) => a.lastUpdated.toDate().valueOf() - b.lastUpdated.toDate().valueOf()) :
            []
        athlete.team = data.team ? Team.createFrom(data.team) : null

        return athlete
    }
}

export {athleteRecordFields} from "$lib/classes/user/athlete/records";
export {AthleteRecord} from "$lib/classes/user/athlete/records";
export type {AthleteRecordDTO} from "$lib/classes/user/athlete/records";
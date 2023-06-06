import { User } from '$lib/classes/user';
import {
    AthleteData,
    type AthleteDataDTO,
    AthleteRecord,
    type AthleteRecordDTO,
} from '$lib/classes/user/athlete';
import { srGet, srPost, srPut } from './helpers/serviceRequest';
import type { AthleteProgramStatsDTO } from '../classes/program/stats';
import { AthleteProgramStats } from '../classes/program/stats';

export default class UserService {
    static createAthleteData = async (athlete: AthleteData): Promise<User> => {
        const { data } = await srPost<UserDTO>(`/api/athlete`, athlete);
        return User.build(data);
    };

    static updateAthleteRecords = async (record: AthleteRecord, id: string): Promise<AthleteRecord[]> => {
        const { data } = await srPut<AthleteRecordDTO[]>(
            `/api/athlete/${id}/record`,
            record
        );
        return data.map(r => AthleteRecord.createFrom(r));
    };

    static updateAthleteData = async (athlete: AthleteData): Promise<AthleteData> => {
        const id = athlete.id;
        // TODO: this needs to be reworked, likely need request models for frontend
        const body = JSON.stringify({
            id,
            teamId: athlete.team ? athlete.team.id : null,
            coachId: athlete.coachId ? athlete.coachId : null,
            name: athlete.name,
            records: athlete.records,
        });
        const { data } = await srPut<AthleteDataDTO>(
            `/api/athlete/${id}`,
            body
        );
        return AthleteData.createFrom(data);
    };

    static updateUserData = async (user: User): Promise<User> => {
        const { data } = await srPut<UserDTO>(`/api/user/${user.id}`, user);
        return User.build(data);
    };

    static fetchAthleteRecords = async (athleteId: string): Promise<AthleteRecord[]> => {
        const { data } = await srGet<AthleteRecordDTO[]>(
            `/api/athlete/${athleteId}/record`
        );
        return data.map(r => AthleteRecord.createFrom(r));
    };

    static fetchAthleteStats = async (athleteId: string): Promise<AthleteProgramStats[]> => {
        const { data } = await srGet<AthleteProgramStatsDTO[]>(
            `/api/athlete/${athleteId}/stats`
        );
        return data.map(s => AthleteProgramStats.createFrom(s));
    };

    static createAthleteRecord = async (
        athleteId: string,
        newRecord: AthleteRecord
    ): Promise<AthleteRecord> => {
        const { data } = await srPost<AthleteRecordDTO>(
            `/api/athlete/${athleteId}/record`,
            newRecord
        );
        return AthleteRecord.createFrom(data);
    };

    static createAthleteRecords = async (
        athleteId: string,
        records: AthleteRecord[]
    ): Promise<AthleteRecord[]> => {
        const { data } = await srPost<AthleteRecordDTO[]>(
            `/api/athlete/${athleteId}/record/list`,
            records
        );
        return data.map(d => AthleteRecord.createFrom(d));
    };
}

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
        return User.createFrom(data);
    };

    static updateAthleteRecords = async (
        record: AthleteRecord,
        id: string
    ): Promise<AthleteRecord[]> => {
        const { data } = await srPut<AthleteRecordDTO[]>(
            `/api/athlete/${id}/record`,
            record
        );
        return data.map(r => AthleteRecord.createFrom(r));
    };

    static updateAthleteData = async (
        athlete: AthleteData
    ): Promise<AthleteData> => {
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
        return User.createFrom(data);
    };

    static fetchAthleteRecords = async (
        athleteId: string
    ): Promise<AthleteRecord[]> => {
        const { data } = await srGet<AthleteRecordDTO[]>(
            `/api/athlete/${athleteId}/record`
        );
        return data.map(r => AthleteRecord.createFrom(r));
    };

    static fetchAthleteStats = async (
        athleteId: string
    ): Promise<AthleteProgramStats[]> => {
        const { data } = await srGet<AthleteProgramStatsDTO[]>(
            `/api/athlete/${athleteId}/stats`
        );
        return data.map(s => AthleteProgramStats.createFrom(s));
    };

    static createAthleteRecord = async (
        athleteId: string,
        newRecord: AthleteRecord
    ): Promise<AthleteRecord | null> => {
        const { data } = await srPost<string>(
            `/api/athlete/${athleteId}/record`,
            newRecord,
            'application/json',
            false
        );
        if (!data) {
            return null;
        }
        const dto: AthleteRecordDTO = JSON.parse(data);
        return AthleteRecord.createFrom(dto);
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

    static inviteUser = async (inviteUserRequest: any) => {
        const { data } = await srPost<string>(
            `/api/auth/invite`,
            inviteUserRequest,
            'application/json',
            false
        );
        return data;
    };

    static searchAthleteRecords = async (
        athleteId: string,
        options: AthleteRecordsSearchOptions = { current: true }
    ) => {
        let url = `/api/athlete/${athleteId}/record?current=${options.current}`;
        if (options.reps) {
            url += `&reps=${options.reps}`;
        }
        if (options.weight !== undefined) {
            url += `&weight=${options.weight}`;
        }
        if (options.name) {
            url += `&name=${options.name}`;
        }
        const { data } = await srGet<AthleteRecordDTO[]>(url);
        return data.map(r => AthleteRecord.createFrom(r));
    };
}

// TODO: move this to it's own file
export interface AthleteRecordsSearchOptions {
    name?: string;
    reps?: number;
    weight?: number;
    current: boolean;
}

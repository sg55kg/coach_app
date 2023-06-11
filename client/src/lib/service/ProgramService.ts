import { DisplayProgram, Program } from '../classes/program';
import type { ProgramDTO } from '../classes/program';
import { Exercise } from '$lib/classes/program/exercise';
import type { ExerciseComment } from '$lib/classes/program/exercise';
import type { AthleteProgramStatsDTO } from '../classes/program/stats';
import { AthleteProgramStats } from '../classes/program/stats';
import type { Dayjs } from 'dayjs';
import type { ExerciseDTO } from '../classes/program/exercise';
import { srDelete, srGet, srPost, srPut } from './helpers/serviceRequest';

export class ProgramService {
    static getProgram = async (id: string) => {
        const { data } = await srGet<ProgramDTO>(`/api/program/${id}`);
        return Program.build(data);
    };

    static createProgram = async (program: Program) => {
        const { data } = await srPost<ProgramDTO>(`/api/program`, program);
        return Program.build(data);
    };

    static deleteProgram = async (programId: string) => {
        await srDelete(`/api/program/${programId}`);
    };
    static updateProgram = async (program: Program) => {
        const id = program.id;
        const { data } = await srPut<ProgramDTO>(`/api/program/${id}`, program);
        return Program.build(data);
    };

    static updateExercise = async (exercise: Exercise) => {
        const { data } = await srPut<ExerciseDTO>(
            `/api/exercise/${exercise.id}`,
            exercise
        );
        return Exercise.createFrom(data);
    };

    static deleteExercise = async (exercise: Exercise) => {
        await srDelete(`/api/exercise/${exercise.id}`);
    };

    static addExerciseComment = async (comment: ExerciseComment) => {
        const { data } = await srPost<ExerciseComment>(
            `/api/exercise/${comment.exerciseId}/comment`,
            comment
        );
        return data;
    };

    static getCoachPrograms = async (id: string) => {};

    static searchProgramsByName = async (coachId: string, name: string) => {
        const { data } = await srGet<ProgramDTO[]>(
            `/api/program?coach=${coachId}&name=${name}`
        );
        return data.map(d => DisplayProgram.build(d));
    };

    static getProgramStats = async (programId: string) => {
        const { data } = await srGet<AthleteProgramStatsDTO>(
            `/api/stats/${programId}`
        );
        return AthleteProgramStats.createFrom(data);
    };

    static getWeeklyProgramStats = async (programId: string) => {
        const { data } = await srGet<AthleteProgramStatsDTO[]>(
            `/api/stats/${programId}/weekly`
        );
        return data.map(d => AthleteProgramStats.createFrom(d));
    };

    static getDailyProgramStats = async (
        programId: string,
        startDate: Dayjs,
        endDate: Dayjs
    ) => {
        const { data } = await srGet<AthleteProgramStatsDTO[]>(
            `/api/stats/${programId}/daily?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`
        );
        return data.map(d => AthleteProgramStats.createFrom(d));
    };
}

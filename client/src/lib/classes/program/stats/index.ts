import dayjs, { Dayjs } from 'dayjs';

export interface AthleteProgramStatsDTO {
    programId: string;
    programName: string;
    plannedTotalVolume: number;
    plannedTotalReps: number;
    plannedAverageIntensity: number;
    plannedKValue: number;
    plannedInol: number;
    actualTotalVolume: number;
    actualTotalReps: number;
    actualAverageIntensity: number;
    actualKValue: number;
    actualInol: number;
    startDate: string;
    endDate: string;
}

export class AthleteProgramStats {
    static createFrom(data: AthleteProgramStatsDTO) {
        const stats = new AthleteProgramStats();

        stats.programId = data.programId;
        stats.programName = data.programName;
        stats.plannedTotalVolume = data.plannedTotalVolume;
        stats.plannedTotalReps = data.plannedTotalReps;
        stats.plannedAverageIntensity = data.plannedAverageIntensity;
        stats.plannedKValue = data.plannedKValue;
        stats.plannedInol = data.plannedInol;
        stats.actualTotalVolume = data.actualTotalVolume;
        stats.actualTotalReps = data.actualTotalReps;
        stats.actualAverageIntensity = data.actualAverageIntensity;
        stats.actualKValue = data.actualKValue;
        stats.actualInol = data.actualInol;
        stats.startDate = dayjs(data.startDate);
        stats.endDate = dayjs(data.endDate);

        return stats;
    }

    programId: string = '';
    programName: string = '';
    plannedTotalVolume: number = 0;
    plannedTotalReps: number = 0;
    plannedAverageIntensity: number = 0;
    plannedKValue: number = 0;
    plannedInol: number = 0;
    actualTotalVolume: number = 0;
    actualTotalReps: number = 0;
    actualAverageIntensity: number = 0;
    actualKValue: number = 0;
    actualInol: number = 0;
    startDate: Dayjs = dayjs();
    endDate: Dayjs = dayjs();
}

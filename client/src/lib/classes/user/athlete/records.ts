import dayjs, {Dayjs} from "dayjs";
import type {Exercise} from "../../program/exercise";
import type {Day} from "../../program/day";

export interface AthleteRecordDTO {
    id: string,
    createdAt: string,
    numReps: number,
    weight: number,
    exerciseName: string,
    exerciseId: string,
    dayId: string,
    athleteId: string
}

export class AthleteRecord {

    public static createFrom(data: AthleteRecordDTO) {
        const record = new AthleteRecord()

        record.id = data.id
        record.createdAt = dayjs(data.createdAt)
        record.numReps = data.numReps
        record.weight = data.weight
        record.exerciseName = data.exerciseName
        record.exerciseId = data.exerciseId
        record.dayId = data.dayId
        record.athleteId = data.athleteId

        return record
    }

    public static buildRecord(exercise: Exercise, day: Day, athleteId: string = '') {
        const record = new AthleteRecord()

        record.athleteId = athleteId
        record.createdAt = dayjs()
        record.dayId = day.id
        record.exerciseId = exercise.id
        record.exerciseName = exercise.name
        record.numReps = exercise.isMax || exercise.isMaxReps ?
            exercise.totalRepsCompleted :
            Math.round(exercise.totalRepsCompleted / exercise.setsCompleted)
        record.weight = exercise.weightCompleted

        return record
    }
    public id: string = ''
    public createdAt: Dayjs = dayjs()
    public numReps: number = 0
    public weight: number = 0
    public exerciseName: string = ''
    public exerciseId: string = ''
    public dayId: string = ''
    public athleteId: string = ''
}

export const athleteRecordFields = [
    'snatch',
    'clean and jerk',
    'jerk',
    'clean',
    'back squat',
    'front squat',
    'deadlift',
    'snatch deadlift',
    'push press',
    'strict press',
    'bent over row',
    'power snatch',
    'power clean',
    'power jerk',
    'hang snatch',
    'hang power snatch',
    'hang power clean',
    'block snatch',
    'block clean',
    'block power snatch',
    'block power clean',
    'clean pull',
    'snatch pull',
    'snatch high pull',
    'bench press',
    'pendlay rows',
    'snatch push press',
    'overhead squat',
    'squat jerk',
    'max pull ups',
    'weighted pull up',
    'max chin ups',
    'weighted chin up',
    'pause snatch',
    'pause clean',
    'sandbag carry meters',
    'sandbag carry weight',
    'farmer carry meters',
    'farmer carry weight',
    'block deadlift',
    'deficit clean',
    'deficit snatch',
    'deficit deadlift',
    'romanian deadlift',
    'snatch romanian deadlift',
    'hang muscle snatch',
    'muscle snatch',
    'pause jerk dip',
    'pause jerk catch',
    'pause deadlift',
    'sumo deadlift',
    'low hang snatch',
    'high hang snatch',
    'low hang clean',
    'high hang clean',
]
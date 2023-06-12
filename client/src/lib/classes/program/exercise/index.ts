import {
    EffortIntensity,
    ExerciseType,
    WeightIntensity,
} from '$lib/classes/program/exercise/enums';
import { WeightUnit } from '../../units/weight';

export interface ExerciseDTO {
    id: string;
    name: string;
    weight: number;
    sets: number;
    repsPerSet: number;
    notes: string;
    weightIntensity: WeightIntensity;
    isMax: boolean;
    isMaxReps: boolean;
    weightCompleted: number;
    totalRepsCompleted: number;
    comments: ExerciseComment[];
    isComplete: boolean;
    setsCompleted: number;
    order: number;
    effortIntensity: string;
    unilateral: boolean;
    equipment: string;
    distanceMeters: number;
    distanceCompletedMeters: number;
    secondsPerSet: number;
    secondsPerSetCompleted: number;
    type: ExerciseType;
    dropSets: ExerciseDTO[];
    repArr: number[] | string;
    nameArr: string[] | string;
    repCompletedArr: number[] | string;
    dropSetPercent: number;
    actualIntensity: string;
}
export class Exercise {
    id: string = '';
    name: string = '';
    weight: number = 0;
    sets: number = 0;
    repsPerSet: number = 0;
    notes: string = '';
    weightIntensity: WeightIntensity = WeightIntensity.NONE;
    isMax: boolean = false;
    weightCompleted: number = 0;
    totalRepsCompleted: number = 0;
    comments: ExerciseComment[] = [];
    isComplete: boolean = false;
    setsCompleted: number = 0;
    order: number = 0;
    effortIntensity: EffortIntensity = EffortIntensity.EASY;
    unilateral: boolean = false;
    equipment: string = '';
    distanceMeters: number = 0;
    distanceCompletedMeters: number = 0;
    secondsPerSet: number = 0;
    secondsPerSetCompleted: number = 0;
    type: ExerciseType = ExerciseType.EXERCISE;
    dropSets: Exercise[] = [];
    repArr: number[] = [];
    nameArr: string[] = [];
    repCompletedArr: number[] = [];
    dropSetPercent: number = 0;
    actualIntesity: EffortIntensity | null = null;
    isMaxReps: boolean = false;
    repsPerSetComplete: number = 0;

    private _wgt: WeightUnit = new WeightUnit(0);
    private _wgtComp: WeightUnit = new WeightUnit(0);

    wgt(unit: 'kg' | 'lb') {
        return this._wgt.getValue(unit);
    }

    setWgt(val: number, unit: 'kg' | 'lb') {
        if (isNaN(val)) {
            val = isNaN(parseInt(val + '')) ? 0 : parseInt(val + '');
        }
        this._wgt.setValue(val, unit);
        this.weight = this._wgt.value;
    }

    wgtComp(unit: 'kg' | 'lb') {
        return this._wgtComp.getValue(unit);
    }

    setWgtComp(val: number, unit: 'kg' | 'lb') {
        this._wgtComp.setValue(val, unit);
        this.weightCompleted = this._wgtComp.value;
    }

    copy() {
        const copy = Exercise.createFrom(JSON.parse(JSON.stringify(this)));
        copy.id = '';
        copy.dropSets = copy.dropSets.map(d => d.copy());
        return copy;
    }

    static createFrom(data: ExerciseDTO) {
        const exercise = new Exercise();

        exercise.id = data.id;
        exercise.isMaxReps = data.isMaxReps;
        exercise.name = data.name;
        exercise.weight = data.weight;
        exercise.sets = data.sets;
        exercise.repsPerSet = data.repsPerSet;
        exercise.order = data.order;
        exercise.isComplete = data.isComplete;
        exercise.weightCompleted = data.weightCompleted;
        exercise.setWgt(data.weight, 'kg');
        exercise.setWgtComp(data.weightCompleted, 'kg');
        if (data.type === ExerciseType.COMPLEX) {
            exercise.repCompletedArr =
                typeof data.repCompletedArr === 'string'
                    ? data.repCompletedArr.split(',').map(r => parseInt(r))
                    : data.repCompletedArr;
            exercise.repArr =
                typeof data.repArr === 'string'
                    ? data.repArr.split(',').map(r => parseInt(r))
                    : data.repArr;
            exercise.nameArr =
                typeof data.nameArr === 'string'
                    ? data.nameArr.split(',')
                    : data.nameArr;
        }
        exercise.isMax = data.isMax;
        if (data.type === ExerciseType.DURATION) {
            exercise.distanceMeters = data.distanceMeters;
            exercise.distanceCompletedMeters = data.distanceCompletedMeters;
            exercise.secondsPerSetCompleted = data.secondsPerSetCompleted;
            exercise.secondsPerSet = data.secondsPerSet;
        }
        if (data.type === ExerciseType.ACCESSORY) {
            exercise.effortIntensity = getEffortIntensity(data.effortIntensity);
            exercise.actualIntesity = data.actualIntensity
                ? getEffortIntensity(data.actualIntensity)
                : null;
        }

        exercise.totalRepsCompleted = data.totalRepsCompleted;
        exercise.setsCompleted = data.setsCompleted;
        exercise.dropSetPercent = data.dropSetPercent;
        exercise.dropSets = data.dropSets
            .map(s => Exercise.createFrom(s))
            .sort((a, b) => a.order - b.order);
        exercise.type = data.type;
        exercise.equipment = data.equipment;
        exercise.unilateral = data.unilateral;
        exercise.comments = data.comments;
        exercise.notes = data.notes;

        return exercise;
    }
}

const getEffortIntensity = (str: string) => {
    switch (str) {
        case 'EASY':
            return EffortIntensity.EASY;
        case 'MODERATE':
            return EffortIntensity.MODERATE;
        case 'DIFFICULT':
            return EffortIntensity.DIFFICULT;
        case 'MAX':
            return EffortIntensity.MAX;
        default:
            return EffortIntensity.EASY;
    }
};

export class ExerciseComment {
    id: string = '';
    athleteId: string = '';
    content: string = '';
    exerciseId: string = '';
    commenterName: string = '';
    createdAt?: Date = new Date();
    updatedAt?: Date = new Date();
}

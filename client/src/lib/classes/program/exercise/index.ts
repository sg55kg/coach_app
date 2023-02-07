import {EffortIntensity, ExerciseType, WeightIntensity} from "$lib/classes/program/exercise/enums";

export interface ExerciseDTO {
    id: string
    name: string
    weight: number
    sets: number
    repsPerSet: number
    notes: string
    weightIntensity: WeightIntensity
    isMax: boolean
    weightCompleted: number
    totalRepsCompleted: number
    comments: ExerciseComment[]
    isComplete: boolean
    setsComplete: number
    order: number
    effortIntensity: EffortIntensity
    unilateral: boolean
    equipment: string
    distanceMeters: number
    distanceCompletedMeters: number
    secondsPerSet: number
    secondsPerSetCompleted: number
    type: ExerciseType
    dropSets: Exercise[]
    repArr: string
    nameArr: string
    repCompletedArr: string
    dropSetPercent: number
}
export class Exercise {
    id: string = ''
    name: string = ''
    weight: number = 0
    sets: number = 0
    repsPerSet: number = 0
    notes: string = ''
    weightIntensity: WeightIntensity = WeightIntensity.NONE
    isMax: boolean = false
    weightCompleted: number = 0
    totalRepsCompleted: number = 0
    comments: ExerciseComment[] = []
    isComplete: boolean = false
    setsCompleted: number = 0
    order: number = 0
    effortIntensity: EffortIntensity = EffortIntensity.EASY
    unilateral: boolean = false
    equipment: string = ''
    distanceMeters: number = 0
    distanceCompletedMeters: number = 0
    secondsPerSet: number = 0
    secondsPerSetCompleted: number = 0
    type: ExerciseType = ExerciseType.EXERCISE
    dropSets: Exercise[] = []
    repArr: number[] = []
    nameArr: string[] = []
    repCompletedArr: number[] = []
    dropSetPercent: number = 0
    actualIntesity: EffortIntensity = EffortIntensity.EASY
    isMaxReps: boolean = false
}


export class ExerciseComment {
    id: string = ''
    athleteId: string = ''
    content: string = ''
    exerciseId: string = ''
    commenterName: string = ''
    createdAt?: Date = new Date()
    updatedAt?: Date = new Date()
}
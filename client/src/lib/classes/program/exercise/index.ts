import {EffortIntensity, ExerciseType, WeightIntensity} from "$lib/classes/program/exercise/enums";

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
    setsComplete: number = 0
    order: number = 0
    effortIntensity: EffortIntensity = EffortIntensity.EASY
    unilateral: boolean = false
    equipment: string = ''
    distanceMeters: number = 0
    distanceCompletedMeters: number = 0
    secondsPerSet: number = 0
    secondsPerSetCompleted: number = 0
    type: ExerciseType = ExerciseType.EXERCISE

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
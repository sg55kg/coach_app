import {WeightIntensity} from "$lib/classes/program";

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
    order: number = 0
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
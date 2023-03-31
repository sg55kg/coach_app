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
    isMaxReps: boolean
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
    dropSets: ExerciseDTO[]
    repArr: string
    nameArr: string
    repCompletedArr: string
    dropSetPercent: number
    actualIntensity: EffortIntensity
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

    static createFrom(data: ExerciseDTO) {
        const exercise = new Exercise()
        console.log('exercise data',data)

        exercise.id = data.id
        exercise.isMaxReps = data.isMaxReps
        exercise.name = data.name
        exercise.weight = data.weight
        exercise.sets = data.sets
        exercise.repsPerSet = data.repsPerSet
        if (data.type === ExerciseType.COMPLEX) {
            exercise.repCompletedArr = data.repCompletedArr.split(',').map(v => parseInt(v))
            exercise.repArr = data.repArr.split(',').map(v => parseInt(v))
            exercise.nameArr = data.nameArr.split(',')
        }
        exercise.isMax = data.isMax
        if (data.type === ExerciseType.DURATION) {
            exercise.distanceMeters = data.distanceMeters
            exercise.distanceCompletedMeters = data.distanceCompletedMeters
            exercise.secondsPerSetCompleted = data.secondsPerSetCompleted
            exercise.secondsPerSet = data.secondsPerSet
        }

        exercise.totalRepsCompleted = data.totalRepsCompleted
        exercise.setsCompleted = data.setsComplete
        exercise.dropSetPercent = data.dropSetPercent
        exercise.actualIntesity = data.actualIntensity
        exercise.dropSets = data.dropSets.map(s => Exercise.createFrom(s))
        exercise.type = data.type
        exercise.equipment = data.equipment
        exercise.unilateral = data.unilateral
        exercise.effortIntensity = data.effortIntensity
        exercise.comments = data.comments
        exercise.notes = data.notes

        return exercise
    }
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
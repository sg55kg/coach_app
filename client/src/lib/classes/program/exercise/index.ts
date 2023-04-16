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
    setsCompleted: number
    order: number
    effortIntensity: string
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
    actualIntensity: string
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
    actualIntesity: EffortIntensity | null = null
    isMaxReps: boolean = false
    repsPerSetComplete: number = 0

    static createFrom(data: ExerciseDTO) {
        const exercise = new Exercise()

        exercise.id = data.id
        exercise.isMaxReps = data.isMaxReps
        exercise.name = data.name
        exercise.weight = data.weight
        exercise.sets = data.sets
        exercise.repsPerSet = data.repsPerSet
        exercise.order = data.order
        exercise.isComplete = data.isComplete
        exercise.weightCompleted = data.weightCompleted
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
        if (data.type === ExerciseType.ACCESSORY) {
            exercise.effortIntensity = getEffortIntensity(data.effortIntensity)
            exercise.actualIntesity = data.actualIntensity ? getEffortIntensity(data.actualIntensity) : null
        }

        exercise.totalRepsCompleted = data.totalRepsCompleted
        exercise.setsCompleted = data.setsCompleted
        exercise.dropSetPercent = data.dropSetPercent
        exercise.dropSets = data.dropSets.map(s => Exercise.createFrom(s)).sort((a, b) => a.order - b.order)
        exercise.type = data.type
        exercise.equipment = data.equipment
        exercise.unilateral = data.unilateral
        exercise.comments = data.comments
        exercise.notes = data.notes

        return exercise
    }
}

const getEffortIntensity = (str: string) => {
    switch (str) {
        case "EASY":
            return EffortIntensity.EASY
        case 'MODERATE':
            return EffortIntensity.MODERATE
        case 'DIFFICULT':
            return EffortIntensity.DIFFICULT
        case 'MAX':
            return EffortIntensity.MAX
        default:
            return EffortIntensity.EASY
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
import type {Exercise} from "$lib/classes/program/exercise";

export const handleChangeWeightUnits = (units: 'kg' | 'lb', exercise: Exercise) => {
    let weightUnitsSelected: string = ''
    if (units === 'kg' && weightUnitsSelected === 'lb') {
        weightUnitsSelected = 'kg'
        exercise.weight = Math.round(exercise.weight / 2.2042)
        exercise.weightCompleted = exercise.weightCompleted > 0 ? Math.round(exercise.weightCompleted / 2.2042) : 0
    } else if (units === 'lb' && weightUnitsSelected === 'kg') {
        weightUnitsSelected = 'lb'
        exercise.weight = Math.round(exercise.weight * 2.2042)
        exercise.weightCompleted = exercise.weightCompleted > 0 ? Math.round(exercise.weightCompleted * 2.2042) : 0
    }
}
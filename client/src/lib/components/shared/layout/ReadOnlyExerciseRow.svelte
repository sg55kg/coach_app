<script lang="ts">
    import { Exercise } from '../../../classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import { userDB } from '../../../stores/authStore';
    import { EffortIntensity } from '../../../classes/program/exercise/enums';
    import GoCheck from 'svelte-icons/go/GoCheck.svelte';
    import GoDash from 'svelte-icons/go/GoDash.svelte';

    export let exercise: Exercise;
    export let showName: boolean = true;

    let unit: 'kg' | 'lb' = $userDB!.preferences.weight;
    $: unit = $userDB!.preferences.weight;

    const formatProgrammedString = (exercise: Exercise, showName: boolean) => {
        let result: string = '';
        switch (exercise.type) {
            case ExerciseType.EXERCISE:
                result = formatExerciseTypeString(result, exercise, showName);
                break;
            case ExerciseType.COMPLEX:
                result = formatComplexTypeString(result, exercise, showName);
                break;
            case ExerciseType.ACCESSORY:
                result = formatAccessoryTypeString(result, exercise, showName);
                break;
        }
        return result;
    };

    const formatExerciseTypeString = (
        result: string,
        exercise: Exercise,
        showName: boolean
    ) => {
        result += showName
            ? exercise.name
                ? `${exercise.name} `
                : 'No Name: '
            : '';
        if (exercise.isMax) {
            result += `${exercise.repsPerSet}RM`;
        } else if (exercise.isMaxReps) {
            result += `${exercise.wgt(unit)}${unit} ${exercise.sets}sets AMRAP`;
        } else {
            result += `${exercise.wgt(unit)}${unit} ${exercise.sets}sets ${
                exercise.repsPerSet
            }reps`;
        }
        return result;
    };

    const formatComplexTypeString = (
        result: string,
        exercise: Exercise,
        showName: boolean
    ) => {
        result += showName ? `${exercise.nameArr.join(' + ')} ` : '';
        if (exercise.isMax) {
            result += `${exercise.repArr.join(' + ')}RM`;
        } else {
            result += `${exercise.wgt(unit)}${unit} ${
                exercise.sets
            }sets ${exercise.repArr.join(' + ')}reps`;
        }
        return result;
    };

    const formatAccessoryTypeString = (
        result: string,
        exercise: Exercise,
        showName: boolean
    ) => {
        result += showName
            ? `${exercise.name ? `${exercise.name} ` : 'No Name'}`
            : '';
        result += `${
            exercise.weight
                ? exercise.wgt(unit) + unit
                : formatEffortString(exercise.effortIntensity)
        } ${exercise.sets}sets ${exercise.repsPerSet}reps`;
        return result;
    };

    const formatCompletedString = (exercise: Exercise) => {
        let result: string = '';
        switch (exercise.type) {
            case ExerciseType.EXERCISE:
                result += `${exercise.wgtComp(unit)}${unit} ${
                    exercise.setsCompleted
                }sets ${
                    exercise.totalRepsCompleted / exercise.setsCompleted
                }reps`;
                break;
            case ExerciseType.COMPLEX:
                result += `${exercise.wgtComp(unit)}${unit} ${
                    exercise.setsCompleted
                }sets ${exercise.repCompletedArr.join(' + ')}reps`;
                break;
            case ExerciseType.ACCESSORY:
                result += `${
                    exercise.weight
                        ? exercise.wgtComp(unit) + unit
                        : formatEffortString(exercise.actualIntensity) // TODO: Fix bug associated with this
                } ${exercise.setsCompleted}sets ${
                    exercise.repsPerSetComplete
                }reps`;
                break;
        }
        return result;
    };

    const formatEffortString = (effort: EffortIntensity) => {
        switch (effort) {
            case EffortIntensity.EASY:
                return 'Easy';
            case EffortIntensity.MODERATE:
                return 'Moderate';
            case EffortIntensity.DIFFICULT:
                return 'Hard';
            case EffortIntensity.MAX:
                return 'Max Effort';
            default:
                return 'Easy';
        }
    };
</script>

<div
    class="{showName
        ? 'mb-2'
        : 'ml-10 border-l-2 border-l-textblue p-1'} flex h-fit flex-col"
>
    <div class="flex">
        {#if exercise.isComplete && exercise.weightCompleted < 1 && exercise.totalRepsCompleted < 1}
            <span class="mr-1 h-6 w-6 text-orange-shade"><GoDash /></span>
        {:else if exercise.isComplete}
            <span class="mr-1 h-6 w-6 text-green"><GoCheck /></span>
        {/if}
        <h4 class="">{formatProgrammedString(exercise, showName)}</h4>
    </div>
    {#if exercise.isComplete}
        <hr />
        <h4 class="text-textblue">{formatCompletedString(exercise)}</h4>
    {/if}
</div>

<style></style>

<script lang="ts">
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {getContext} from "svelte";

    export let expandedExerciseId: string = ''
    export let exercise: Exercise
    export let index: number

    const { getSelectedExerciseIdx } = getContext('program')
    const selectedExerciseIdx = getSelectedExerciseIdx()
</script>

<div class="bg-gray-200 border-l-2 border-textgrey w-full p-2" on:click={() => $selectedExerciseIdx = index}>
    {#if exercise.type === ExerciseType.EXERCISE}
        <h4>
            {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets {exercise.repsPerSet}reps
        </h4>
    {:else if exercise.type === ExerciseType.COMPLEX}
        <h4>
            {exercise.nameArr.join(' + ')}: {exercise.weight}kg {exercise.sets}sets {exercise.repArr.join(' + ')}reps
        </h4>
    {:else if exercise.type === ExerciseType.DURATION}
        <h4>Test</h4>
    {:else}
        <h4>Test</h4>
    {/if}
</div>

<style></style>
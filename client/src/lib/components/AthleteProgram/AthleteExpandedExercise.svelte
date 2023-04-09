<script lang="ts">
    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte'
    import GoPencil from 'svelte-icons/go/GoPencil.svelte'
    import AthleteEditExerciseRow from "$lib/components/AthleteProgram/AthleteEditExerciseRow.svelte";

    export let exercise: Exercise
    export let selectedExerciseIdx: number

    let editMode: boolean = false
</script>

<div class="bg-gray-200 mr-2 border-l-2 border-l-textblue p-2 relative">
    {#if editMode}
        {#if exercise.type === ExerciseType.EXERCISE}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
        {:else if exercise.type === ExerciseType.COMPLEX}
            <h3 class="text-lg font-semibold text-textblue">{exercise.nameArr.join(' + ')}</h3>
        {:else if exercise.type === ExerciseType.DURATION}
            <h3>Test</h3>
        {:else if exercise.type === ExerciseType.ACCESSORY}
            <h3>Test</h3>
        {/if}
        <AthleteEditExerciseRow bind:exercise={exercise} />
        {#each exercise.dropSets as dropSet}
            <AthleteEditExerciseRow bind:exercise={dropSet} />
        {/each}
    {:else}
        {#if exercise.type === ExerciseType.EXERCISE}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
            {#each [exercise, ...exercise.dropSets] as row}
                {#if row.isMax}
                    <h5>{row.repsPerSet} Rep Max</h5>
                {:else if row.isMaxReps}
                    <h5>{row.weight}kg - {exercise.sets} sets for as many reps as possible</h5>
                {:else}
                    <h5>{row.weight}kg - {exercise.sets} sets of {exercise.repsPerSet} reps</h5>
                {/if}
            {/each}
        {:else if exercise.type === ExerciseType.COMPLEX}
            <h3 class="text-lg font-semibold text-textblue">{exercise.nameArr.join('+')}</h3>
        {:else if exercise.type === ExerciseType.DURATION}
            <h3>Test</h3>
        {:else if exercise.type === ExerciseType.ACCESSORY}
            <h3>Test</h3>
        {/if}
    {/if}
    <div class="flex flex-col items-center py-2">
        <button class="mx-2 p-2 border-yellow border-2 text-yellow-shade rounded-sm">Mark Complete As Written</button>
        <button class="mx-2 p-2 text-red-shade">Skip Exercise</button>
    </div>
    <button class="h-5 w-5 absolute right-3 top-2 {editMode ? 'text-textblue' : ''}" on:click={() => editMode = !editMode}>
        <GoPencil />
    </button>
    <button class="h-4 w-4 absolute bottom-2 right-3" on:click={() => selectedExerciseIdx = -1}>
        <FaChevronUp />
    </button>
</div>

<style></style>
<script lang="ts">

    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte'

    export let exercise: Exercise
    export let exerciseIndex: number
    export let selectedExerciseIdx: number

</script>

<div class="relative bg-gray-200 mr-2 border-l-2 border-l-textblue mb-2 p-2 hover:cursor-pointer flex flex-col justify-center items-start"
     on:click={() => selectedExerciseIdx = exerciseIndex}
>
    {#if exercise.type === ExerciseType.EXERCISE}
        {#each [exercise, ...exercise.dropSets] as row}
            {#if row.isMax}
                <h4>
                    {row.name ? row.name : 'No Name'}: {row.repsPerSet}RM
                </h4>
            {:else if row.isMaxReps}
                <h4>
                    {row.name ? row.name : 'No Name'}: {row.weight}kg {row.sets}sets AMRAP
                </h4>
            {:else}
                <h4>
                    {row.name ? row.name : 'No Name'}: {row.weight}kg {row.sets}sets {row.repsPerSet}reps
                </h4>
            {/if}
        {/each}
    {:else if exercise.type === ExerciseType.COMPLEX}
        {#each [exercise, ...exercise.dropSets] as row}
            {#if row.isMax}
                <h4>
                    {row.nameArr.join(' + ')}: {row.repArr.join(' + ')}RM
                </h4>
            {:else}
                <h4>
                    {row.nameArr.join(' + ')}: {row.weight}kg {row.sets}sets {row.repArr.join(' + ')}reps
                </h4>
            {/if}
        {/each}
    {:else if exercise.type === ExerciseType.DURATION}
        <h4>Test</h4>
    {:else}
        <h4>Test</h4>
    {/if}
    <button class="absolute right-3 h-4 w-4">
        <FaChevronDown />
    </button>
</div>

<style></style>
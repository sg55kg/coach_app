<script lang="ts">

    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte'
    import GoCheck from 'svelte-icons/go/GoCheck.svelte'

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
                <div class="flex">
                    {#if row.isComplete && (row.weightCompleted < 1 && row.totalRepsCompleted < 1)}
                        <span class="h-6 w-6 text-orange-shade mr-1"><GoCheck /></span>
                    {:else if row.isComplete}
                        <span class="h-6 w-6 text-green mr-1"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}: {row.repsPerSet}RM {row.isComplete ? `- ${row.weightCompleted}kg` : ''}
                    </h4>
                </div>
            {:else if row.isMaxReps}
                <div class="flex">
                    {#if row.isComplete && (row.weightCompleted < 1 && row.totalRepsCompleted < 1)}
                        <span class="h-6 w-6 text-orange-shade mr-1"><GoCheck /></span>
                    {:else if row.isComplete}
                        <span class="h-6 w-6 text-green mr-1"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}: {row.weightCompleted}/{row.weight}kg {row.setsCompleted}/{row.sets}sets AMRAP {row.isComplete ? `- ${row.totalRepsCompleted}reps` : ''}
                    </h4>
                </div>
            {:else}
                <div class="flex">
                    {#if row.isComplete && (row.weightCompleted < 1 && row.totalRepsCompleted < 1)}
                        <span class="h-6 w-6 text-orange-shade mr-1"><GoCheck /></span>
                    {:else if row.isComplete}
                        <span class="h-6 w-6 text-green mr-1"><GoCheck /></span>
                    {/if}
                    <h4>
                        {row.name ? row.name : 'No Name'}: {row.weightCompleted}/{row.weight}kg {row.setsCompleted}/{row.sets}sets {row.totalRepsCompleted ? row.totalRepsCompleted/row.setsCompleted : 0}/{row.repsPerSet}reps
                    </h4>
                </div>
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
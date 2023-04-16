<script lang="ts">
    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte'
    import GoPencil from 'svelte-icons/go/GoPencil.svelte'
    import AthleteEditExerciseRow from "$lib/components/AthleteProgram/AthleteEditExerciseRow.svelte";
    import {getContext, onMount} from "svelte";
    import {ExerciseMaxRepsError, ExerciseMaxWeightError} from "../../contexts/athleteProgramContext";
    import {EffortIntensity} from "../../classes/program/exercise/enums";

    export let exercise: Exercise
    export let selectedExerciseIdx: number

    const { getAthleteProgramError, skipExercise, getCurrentDay, getCurrentProgram, markExerciseCompleteAsWritten } = getContext('athlete-program')
    const error = getAthleteProgramError()
    const currentDay = getCurrentDay()
    const currentProgram = getCurrentProgram()

    $: $error ? (() => {
        if ($error instanceof ExerciseMaxWeightError) {
            document.getElementById(`max-weight-input-${$error.id}`)?.focus()
        } else if ($error instanceof ExerciseMaxRepsError) {
            document.getElementById(`rep-max-input-${$error.id}`).focus()
        }
    })() : null

    let hasMax: boolean = false
    let enteredWeight: boolean = false
    onMount(() => {
        if (exercise.isMaxReps || exercise.isMax) {
            hasMax = true
        }
        if (exercise.weightCompleted > 0) {
            enteredWeight = true
        }
        for (const d of exercise.dropSets) {
            if (d.isMaxReps || d.isMax) {
                hasMax = true
            }
            if (d.weightCompleted > 0) {
                enteredWeight = true
            }
        }
    })

    const formatEffortString = (effort: EffortIntensity) => {
        console.log(effort, exercise)
        switch (effort) {
            case EffortIntensity.EASY:
                return 'Easy'
            case EffortIntensity.MODERATE:
                return 'Moderate'
            case EffortIntensity.DIFFICULT:
                return 'Hard'
            case EffortIntensity.MAX:
                return 'Max Effort'
        }
    }

    let editMode: boolean = false
</script>

<div class="bg-gray-200 mr-2 mb-2 border-l-2 border-l-textblue p-2 relative">
    {#if editMode}
        {#if exercise.type === ExerciseType.EXERCISE}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
        {:else if exercise.type === ExerciseType.COMPLEX}
            <h3 class="text-lg font-semibold text-textblue">{exercise.nameArr.join(' + ')}</h3>
        {:else if exercise.type === ExerciseType.DURATION}
            <h3>Test</h3>
        {:else if exercise.type === ExerciseType.ACCESSORY}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
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
                    <input type="number"
                           class="bg-gray-300 p-1 rounded {$error && $error.id === row.id ? 'error-highlight' : ''}"
                           id="max-weight-input-{row.id}"
                           bind:value={row.weightCompleted}
                    >
                {:else if row.isMaxReps}
                    <h5>{row.weight}kg - {exercise.sets} sets for as many reps as possible</h5>
                    <input type="number"
                           class="bg-gray-300 p-1 rounded {$error && $error.id === row.id ? 'error-highlight' : ''}"
                           id="rep-max-input-{row.id}"
                           bind:value={row.totalRepsCompleted}
                    >
                {:else}
                    <h5>{row.weight}kg - {exercise.sets} sets of {exercise.repsPerSet} reps</h5>
                {/if}
            {/each}
        {:else if exercise.type === ExerciseType.COMPLEX}
            <h3 class="text-lg font-semibold text-textblue">{exercise.nameArr.join('+')}</h3>
        {:else if exercise.type === ExerciseType.DURATION}
            <h3>Test</h3>
        {:else if exercise.type === ExerciseType.ACCESSORY}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
            {#each [exercise, ...exercise.dropSets] as row}
                <h5>{row.weight ? row.weight + 'kg' : formatEffortString(row.effortIntensity)} - {exercise.sets} sets of {exercise.repsPerSet} reps</h5>
            {/each}
        {/if}
    {/if}
    {#if (!hasMax || !enteredWeight) && !exercise.isComplete}
        <div class="flex flex-col items-center py-2">
            <button class="mx-2 p-2 border-yellow border-2 text-yellow-shade rounded-sm"
                on:click={() => markExerciseCompleteAsWritten(exercise, $currentDay, $currentProgram)}
            >
                Mark Complete As Written
            </button>
            <button class="mx-2 p-2 text-red-shade" on:click={() => skipExercise(exercise, $currentDay, $currentProgram)}>Skip Exercise</button>
        </div>
    {/if}
    <button class="h-5 w-5 absolute right-3 top-2 {editMode ? 'text-textblue' : ''}" on:click={() => editMode = !editMode}>
        <GoPencil />
    </button>
    <button class="h-4 w-4 absolute bottom-2 right-3" on:click={() => selectedExerciseIdx = -1}>
        <FaChevronUp />
    </button>
</div>

<style>
    :global(.error-highlight:focus) {
        outline: none;
        border: 2px solid red;
        border-radius: 6px;
    }
</style>
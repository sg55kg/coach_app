<script lang="ts">
    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte'
    import GoPencil from 'svelte-icons/go/GoPencil.svelte'
    import AthleteEditExerciseRow from "$lib/components/AthleteProgram/AthleteEditExerciseRow.svelte";
    import {getContext, onMount} from "svelte";
    import {ExerciseMaxRepsError, ExerciseMaxWeightError} from "../../contexts/athleteProgramContext";
    import {EffortIntensity} from "../../classes/program/exercise/enums";
    import {userDB} from "$lib/stores/authStore.js";

    export let exercise: Exercise
    export let selectedExerciseIdx: number

    const {
        getAthleteProgramError,
        skipExercise,
        getCurrentDay,
        getCurrentProgram,
        markExerciseCompleteAsWritten,
        getNewRecordExerciseIds
    } = getContext('athlete-program')

    const error = getAthleteProgramError()
    const currentDay = getCurrentDay()
    const currentProgram = getCurrentProgram()
    const newRecordIds = getNewRecordExerciseIds()

    $: $error ? (() => {
        if ($error instanceof ExerciseMaxWeightError) {
            document.getElementById(`max-weight-input-${$error.id}`)?.focus()
        } else if ($error instanceof ExerciseMaxRepsError) {
            document.getElementById(`rep-max-input-${$error.id}`).focus()
        }
    })() : null

    let hasMax: boolean = false
    let enteredWeight: boolean = false
    let unit: 'kg' | 'lb' = $userDB.preferences.weight
    $: unit = $userDB.preferences.weight

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
        {#if $newRecordIds.length > 0 && $newRecordIds.includes(exercise.id)}
            <p class="text-green">New Record!</p>
        {/if}
        <AthleteEditExerciseRow bind:exercise={exercise} />
        {#each exercise.dropSets as dropSet}
            <AthleteEditExerciseRow bind:exercise={dropSet} />
        {/each}
    {:else}
        {#if exercise.type === ExerciseType.EXERCISE}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
            {#if $newRecordIds.length > 0 && $newRecordIds.includes(exercise.id)}
                <p class="text-green">New Record!</p>
            {/if}
            {#each [exercise, ...exercise.dropSets] as row}
                {#if row.isMax}
                    <h5>{row.repsPerSet} Rep Max</h5>
                    <input type="number"
                           class="bg-gray-300 p-1 rounded {$error && $error.id === row.id ? 'error-highlight' : ''}"
                           id="max-weight-input-{row.id}"
                           value={row.wgtComp(unit)}
                           on:change={(e) => row.setWgtComp(e.target.value, unit)}
                    >
                {:else if row.isMaxReps}
                    <h5>{row.wgt(unit)}{unit} - {exercise.sets} sets for as many reps as possible</h5>
                    <input type="number"
                           class="bg-gray-300 p-1 rounded {$error && $error.id === row.id ? 'error-highlight' : ''}"
                           id="rep-max-input-{row.id}"
                           bind:value={row.totalRepsCompleted}
                    >
                {:else}
                    <h5>{row.wgt(unit)}{unit} - {row.sets} sets of {row.repsPerSet} reps</h5>
                {/if}
            {/each}
        {:else if exercise.type === ExerciseType.COMPLEX}
            <h3 class="text-lg font-semibold text-textblue">{exercise.nameArr.join(' + ')}</h3>
            {#if $newRecordIds.length > 0 && $newRecordIds.includes(exercise.id)}
                <p class="text-green">New Record!</p>
            {/if}
            {#each [exercise, ...exercise.dropSets] as row}
                {#if row.isMax}
                {:else}
                    <h5>{row.wgt(unit)}{unit} - {row.sets} sets of {row.repArr.join(' + ')}</h5>
                {/if}
            {/each}
        {:else if exercise.type === ExerciseType.DURATION}
            <h3>Test</h3>
        {:else if exercise.type === ExerciseType.ACCESSORY}
            <h3 class="text-lg font-semibold text-textblue">{exercise.name}</h3>
            {#each [exercise, ...exercise.dropSets] as row}
                <h5>{row.weight ? row.wgt(unit) + unit : formatEffortString(row.effortIntensity)} - {exercise.sets} sets of {exercise.repsPerSet} reps</h5>
            {/each}
        {/if}
    {/if}
    <div class="flex flex-col">
        <h4 class="text-md mt-1 font-medium text-textblue">Notes:</h4>
        <p class="text-sm"><i>{exercise.notes ? exercise.notes : 'No notes for this exercise'}</i></p>
    </div>
    {#if (!hasMax || !enteredWeight) && !exercise.isComplete}
        <div class="flex flex-col items-center py-2">
            <button class="mx-2 p-2 border-yellow border-2 text-yellow-shade rounded-sm"
                on:click={() => markExerciseCompleteAsWritten(exercise, $currentDay, $currentProgram, $userDB.athleteData.id)}
            >
                {exercise.weightCompleted ? 'Mark Complete' : 'Mark Complete As Written'}
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
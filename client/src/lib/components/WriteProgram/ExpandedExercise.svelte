<script lang="ts">
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte'
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import {Exercise} from "$lib/classes/program/exercise";
    import {getContext, onDestroy} from "svelte";
    import ExerciseRow from "$lib/components/WriteProgram/ExerciseRow.svelte";

    export let exercise: Exercise

    const { getProgram, getSelectedDay, getSelectedExerciseIdx } = getContext('program')
    const program = getProgram()
    const selectedDay = getSelectedDay()
    const selectedExerciseIdx = getSelectedExerciseIdx()
    const addDropSet = () => {
        const newExercise = new Exercise()
        newExercise.type = exercise.type
        newExercise.name = exercise.name
        newExercise.nameArr = [...exercise.nameArr]
        newExercise.order = exercise.dropSets.length
        exercise.dropSets = [...exercise.dropSets, newExercise]
    }

    onDestroy(() => {
        // sync the program with any potential changes that might have been made to this day
        if (!$selectedDay) return
        const day = $program.days.find(d => d.id === $selectedDay.id)
        let ex: Exercise = day.exercises[$selectedExerciseIdx]
        ex = exercise
        $program = $program
        $selectedExerciseIdx = -1
    })
</script>

<div class="absolute right-1 left-1 bg-gray-200 h-[70vh] overflow-y-auto flex flex-col items-center lg:p-8 z-20">
    <ExerciseRow bind:exercise={exercise} />
    {#each exercise.dropSets as dropSet}
        <ExerciseRow bind:exercise={dropSet} isDropSet={true} />
    {/each}
    <button class="text-yellow h-7 w-7 my-2" on:click={addDropSet}>
        <FaPlus />
    </button>
    <div class="flex flex-col items-start w-full p-2">
        <label>Exercise Notes</label>
        <textarea class="bg-gray-300 w-full p-px" bind:value={exercise.notes}></textarea>
    </div>
    <button class="text-yellow w-6 h-6 mx-4 mt-6 mb-2" on:click={() => $selectedExerciseIdx = -1}>
        <FaChevronUp />
    </button>
</div>

<style>


</style>
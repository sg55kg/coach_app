<script lang="ts">
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {getContext} from "svelte";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import {isMobile} from "$lib/stores/authStore";

    export let expandedExerciseId: string = ''
    export let exercise: Exercise
    export let index: number

    const { getSelectedExerciseIdx, getProgram, getSelectedDayIdx, getSelectedDay } = getContext('program')
    const selectedExerciseIdx = getSelectedExerciseIdx()
    const selectedDayIdx = getSelectedDayIdx()
    const selectedDay = getSelectedDay()
    const program = getProgram()

    const deleteExercise = () => {
        $program.days[$selectedDayIdx].exercises.splice(index, 1)
        $program = $program
        $selectedDay = $program.days[$selectedDayIdx]
    }
</script>

<div class="bg-gray-200 border-l-2 border-textgrey w-full p-2 my-1 relative z-0">
    <button class="absolute right-0 flex items-center {$isMobile ? 'h-7 w-7 px-1' : 'h-6 w-10 px-2'}" on:click={deleteExercise}>
        <span class="h-6 w-6 text-red hover:text-red-shade hover:cursor-pointer">
            <FaTrashAlt />
        </span>
    </button>
    <div class="hover:cursor-pointer" on:click={() => $selectedExerciseIdx = index}>
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

</div>

<style></style>
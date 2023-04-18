<script lang="ts">
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {getContext} from "svelte";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import {isMobile} from "$lib/stores/authStore";
    import {EffortIntensity} from "../../classes/program/exercise/enums";

    export let exercise: Exercise
    export let index: number

    const { getSelectedExerciseIdx, getProgram, getSelectedDayIdx, getSelectedDay, deleteExercise } = getContext('program')
    const selectedExerciseIdx = getSelectedExerciseIdx()
    const selectedDayIdx = getSelectedDayIdx()
    const selectedDay = getSelectedDay()
    const program = getProgram()

    const handleDeleteExercise = async () => {
        // if an ID exists, make a backend call to delete the exercise
        if ($program.days[$selectedDayIdx].exercises[index].id) {
            try {
                await deleteExercise(exercise)
            } catch (e) {
                return
            }
        }

        // If no ID or if successfully deleted on the backend, splice from the frontend
        $program.days[$selectedDayIdx].exercises.splice(index, 1)
        $program.days[$selectedDayIdx].exercises.forEach((e, i) => {
            e.order = i
            return e
        })
        $program = $program
        $selectedDay = $program.days[$selectedDayIdx]
    }

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
</script>

<div class="bg-gray-200 border-l-2 border-textgrey w-full p-2 my-1 relative z-0">
    <button class="absolute right-0 flex items-center {$isMobile ? 'h-7 w-7 px-1' : 'h-6 w-10 px-2'}" on:click={handleDeleteExercise}>
        <span class="h-6 w-6 text-red hover:text-red-shade hover:cursor-pointer">
            <FaTrashAlt />
        </span>
    </button>
    <div class="hover:cursor-pointer" on:click={() => $selectedExerciseIdx = index}>
        {#if exercise.type === ExerciseType.EXERCISE}
            {#if exercise.isMax}
                <h4>
                    {exercise.name ? exercise.name : 'No Name'}: {exercise.repsPerSet}RM
                </h4>
            {:else if exercise.isMaxReps}
                <h4>
                    {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets AMRAP
                </h4>
            {:else}
                <h4>
                    {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets {exercise.repsPerSet}reps
                </h4>
            {/if}
        {:else if exercise.type === ExerciseType.COMPLEX}
            {#if exercise.isMax}
                <h4>
                    {exercise.nameArr.join(' + ')}: {exercise.repArr.join(' + ')}RM
                </h4>
            {:else}
                <h4>
                    {exercise.nameArr.join(' + ')}: {exercise.weight}kg {exercise.sets}sets {exercise.repArr.join(' + ')}reps
                </h4>
            {/if}

        {:else if exercise.type === ExerciseType.DURATION}
            <h4>Test</h4>
        {:else}
            <h4>
                {exercise.name ? exercise.name : 'No Name'}: {exercise.weight ? exercise.weight + 'kg' : formatEffortString(exercise.effortIntensity)} {exercise.sets}sets {exercise.repsPerSet}reps
            </h4>
        {/if}
    </div>

</div>

<style></style>
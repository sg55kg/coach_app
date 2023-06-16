<script lang="ts">
    import { Exercise } from '$lib/classes/program/exercise';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import { getContext } from 'svelte';
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte';
    import { isMobile } from '$lib/stores/authStore';
    import { EffortIntensity } from '../../classes/program/exercise/enums';
    import { userDB } from '../../stores/authStore';
    import ReadOnlyExerciseRow from "$lib/components/WriteProgram/views/ReadOnlyExerciseRow.svelte";

    export let exercise: Exercise;
    export let index: number;

    const {
        getSelectedExerciseIdx,
        getProgram,
        getSelectedDayIdx,
        getSelectedDay,
        deleteExercise,
    } = getContext('program');
    const selectedExerciseIdx = getSelectedExerciseIdx();
    const selectedDayIdx = getSelectedDayIdx();
    const selectedDay = getSelectedDay();
    const program = getProgram();

    let unit: 'kg' | 'lb' = $userDB!.preferences.weight;
    $: unit = $userDB!.preferences.weight;

    const handleDeleteExercise = async () => {
        // if an ID exists, make a backend call to delete the exercise
        if ($program.days[$selectedDayIdx].exercises[index].id) {
            try {
                await deleteExercise(exercise);
            } catch (e) {
                return;
            }
        }

        // If no ID or if successfully deleted on the backend, splice from the frontend
        $program.days[$selectedDayIdx].exercises.splice(index, 1);
        $program.days[$selectedDayIdx].exercises.forEach((e, i) => {
            e.order = i;
            return e;
        });
        $program = $program;
        $selectedDay = $program.days[$selectedDayIdx];
    };


</script>

<div
    class="border-textgrey relative z-0 my-1 w-full border-l-2 bg-gray-200 p-2"
>
    <button
        class="absolute right-0 flex items-center {$isMobile
            ? 'h-7 w-7 px-1'
            : 'h-6 w-10 px-2'}"
        on:click="{handleDeleteExercise}"
    >
        <span
            class="h-6 w-6 text-red hover:cursor-pointer hover:text-red-shade"
        >
            <FaTrashAlt />
        </span>
    </button>
    <div
        class="hover:cursor-pointer"
        on:click="{() => ($selectedExerciseIdx = index)}"
    >
        {#each [exercise, ...exercise.dropSets] as row, idx}
            <ReadOnlyExerciseRow exercise="{row}" showName="{idx === 0}" />
        {/each}
    </div>
</div>

<style></style>

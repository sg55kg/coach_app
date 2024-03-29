<script lang="ts">
    import { Exercise } from '$lib/classes/program/exercise';
    import { getContext } from 'svelte';
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte';
    import { isMobile } from '$lib/stores/authStore';
    import { userDB } from '../../stores/authStore';
    import MdContentCopy from 'svelte-icons/md/MdContentCopy.svelte';
    import ReadOnlyExerciseRow from '$lib/components/shared/layout/ReadOnlyExerciseRow.svelte';

    export let exercise: Exercise;
    export let index: number;

    const {
        getSelectedExerciseIdx,
        getProgram,
        getSelectedDayIdx,
        deleteExercise,
        copyExercise,
        getProgramInfo,
    } = getContext('program');
    const selectedExerciseIdx = getSelectedExerciseIdx();
    const selectedDayIdx = getSelectedDayIdx();
    const program = getProgram();
    const programInfo = getProgramInfo();

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
    };
</script>

<div
    class="border-textgrey z-0 my-1 flex h-fit w-full border-l-2 bg-gray-200 p-2"
>
    <div
        class="flex-grow hover:cursor-pointer"
        on:click="{() => ($selectedExerciseIdx = index)}"
    >
        {#each [exercise, ...exercise.dropSets] as row, idx}
            <ReadOnlyExerciseRow exercise="{row}" showName="{idx === 0}" />
        {/each}
    </div>
    <div class="flex">
        <button class="{$isMobile ? 'h-7 w-7 px-1' : 'h-6 w-10 px-2'}">
            <span
                class="text-textgray hover:cursor-pointer hover:text-textblue"
                on:click="{() => {
                    copyExercise(index);
                    $programInfo = `Copied ${exercise.name}`;
                }}"
            >
                <MdContentCopy />
            </span>
        </button>
        <button
            class="{$isMobile ? 'h-7 w-7 px-1' : 'h-6 w-10 px-2'}"
            on:click="{handleDeleteExercise}"
        >
            <span
                class="h-6 w-6 text-red hover:cursor-pointer hover:text-red-shade"
            >
                <FaTrashAlt />
            </span>
        </button>
    </div>
</div>

<style></style>

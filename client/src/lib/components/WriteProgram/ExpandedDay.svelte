<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import ExerciseCard from '$lib/components/WriteProgram/ExerciseCard.svelte';
    import { getContext, onDestroy, onMount } from 'svelte';
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte';
    import FaChevronRight from 'svelte-icons/fa/FaChevronRight.svelte';
    import ExpandedExercise from '$lib/components/WriteProgram/ExpandedExercise.svelte';
    import { Exercise } from '$lib/classes/program/exercise';
    import { userDB } from '$lib/stores/authStore';
    import RichTextEditor from '$lib/components/shared/texteditor/RichTextEditor.svelte';
    import { WarmUp } from '../../classes/program/day';

    const {
        getProgram,
        getSelectedDay,
        getSelectedDayIdx,
        setSelectedDay,
        getSelectedExerciseIdx,
        updateProgram,
        getDayClipboard,
        copyDay,
        pasteDay,
        clearExercises,
    } = getContext('program');

    const program = getProgram();
    const selectedDay = getSelectedDay();
    const index = getSelectedDayIdx();
    const exerciseIndex = getSelectedExerciseIdx();
    const dayClipboard = getDayClipboard();

    let expandedExerciseId: string = ' '; //change to exercise id
    let athlete = $userDB?.coachData
        ? $userDB.coachData.athletes.find(a => a.id === $program.athleteId)
        : null;
    let stashedExercises: Exercise[] = [];

    const decrementDay = () => {
        if ($index < 1) return;
        $index = $index - 1;
        $selectedDay = $program.days[$index];
    };

    const incrementDay = () => {
        if ($index === $program.days.length - 1) return;
        $index = $index + 1;
        $selectedDay = $program.days[$index];
    };

    const addExercise = () => {
        let newExercise = new Exercise();
        newExercise.order = $selectedDay.exercises.length;
        $selectedDay.exercises = [...$selectedDay.exercises, newExercise];
        $program.days[$index].exercises = $selectedDay.exercises;
        $program = $program;
    };

    const makeRestDay = () => {
        stashedExercises = [...$program.days[$index].exercises];
        $program.days[$index].isRestDay = true;
        $program.days[$index].exercises.length = 0;
        $selectedDay = $program.days[$index];
    };

    const undoRestDay = () => {
        $program.days[$index].isRestDay = false;
        $program.days[$index].exercises = stashedExercises;
        stashedExercises = [];
        $selectedDay = $program.days[$index];
    };

    const toggleWarmup = () => {
        if ($program.days[$index].warmUp) {
            $program.days[$index].warmUp = null;
        } else {
            $program.days[$index].warmUp = new WarmUp();
        }
        $selectedDay = $program.days[$index];
    };

    let warmupContent: string = '';

    onDestroy(() => {
        if ($exerciseIndex > -1) {
            $program.days[index] = $selectedDay;
            $exerciseIndex = -1;
        }
    });
</script>

<div
    class="absolute top-12 left-0 right-0 bottom-0 flex w-screen overflow-y-auto"
>
    <div
        class="relative m-auto flex h-[88vh] w-11/12 flex-col items-center bg-gray-300"
    >
        <button
            class="absolute top-2 right-3 text-2xl font-bold"
            on:click="{() => setSelectedDay(undefined)}"
        >
            X
        </button>
        <small class="w-fit self-start p-0.5 text-sm"
            >{$program.name}{athlete ? ' - ' + athlete.name : ''}</small
        >
        <div
            class="flex items-center justify-center p-2 text-2xl font-bold text-yellow"
        >
            <button class="mx-8 h-6 w-3" on:click="{decrementDay}">
                <FaChevronLeft />
            </button>
            <h1>Day {$index + 1}</h1>
            <button class="mx-8 h-6 w-3" on:click="{incrementDay}">
                <FaChevronRight />
            </button>
        </div>
        <div
            class="mt-6 flex w-full flex-col items-start justify-start self-start pr-2"
        >
            {#if $selectedDay.isRestDay}
                <h4 class="m-2 w-full text-center text-2xl font-semibold">
                    Rest Day
                </h4>
            {:else}
                {#if $selectedDay.warmUp}
                    <div class="flex w-full items-center justify-center">
                        <RichTextEditor
                            bind:content="{$program.days[$index].warmUp
                                .instructions}"
                        />
                    </div>
                    <button
                        class="cursor-pointer self-center py-2 text-red"
                        on:click="{toggleWarmup}">Remove Warm Up</button
                    >
                {:else}
                    <button
                        class="cursor-pointer self-center py-2 text-textblue"
                        on:click="{toggleWarmup}">Add Warm Up</button
                    >
                {/if}
                {#each $selectedDay.exercises as exercise, idx}
                    {#if $exerciseIndex === idx}
                        <ExpandedExercise bind:exercise="{exercise}" />
                    {:else}
                        <ExerciseCard
                            bind:exercise="{exercise}"
                            index="{idx}"
                        />
                    {/if}
                {/each}
            {/if}
        </div>
        <div class="flex flex-col items-center p-4">
            {#if $selectedDay.isRestDay}
                <button
                    class="text-md border-textyellow my-2 rounded border p-1"
                    on:click="{undoRestDay}"
                >
                    Undo Rest Day
                </button>
            {:else}
                <button
                    class="my-2 h-6 w-6 text-yellow"
                    on:click="{addExercise}"
                >
                    <FaPlus />
                </button>
                <button
                    class="text-md border-textyellow my-2 rounded border p-1"
                    on:click="{makeRestDay}"
                >
                    Make Rest Day
                </button>
            {/if}
        </div>
    </div>
</div>
<div class="fixed bottom-0 left-0 right-0 flex justify-around bg-gray-100">
    <button
        class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
        on:click="{() => copyDay($index)}"
    >
        Copy
    </button>
    {#if dayClipboard.length > 0}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => pasteDay($index)}"
        >
            Paste
        </button>
    {/if}
    {#if $selectedDay.exercises.length > 0}
        <button
            class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
            on:click="{() => clearExercises($index)}"
        >
            Clear Exercises
        </button>
    {/if}
    <button
        class="my-2 rounded bg-yellow px-4 py-2 text-lg font-bold text-gray-300"
        on:click="{updateProgram}"
    >
        Save
    </button>
</div>

<style>
</style>

<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import ExerciseCard from '$lib/components/WriteProgram/ExerciseCard.svelte';
    import { getContext, onDestroy } from 'svelte';
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte';
    import FaChevronRight from 'svelte-icons/fa/FaChevronRight.svelte';
    import ExpandedExercise from '$lib/components/WriteProgram/ExpandedExercise.svelte';
    import { Exercise } from '$lib/classes/program/exercise';
    import { userDB } from '$lib/stores/authStore';
    import RichTextEditor from '$lib/components/shared/texteditor/RichTextEditor.svelte';
    import { WarmUp } from '../../classes/program/day';
    import DayToolbar from '$lib/components/WriteProgram/toolbars/DayToolbar.svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import WeekViewNav from '$lib/components/WriteProgram/navigation/WeekViewNav.svelte';

    const {
        getProgram,
        getSelectedDayIdx,
        getSelectedExerciseIdx,
        updateProgram,
        getDayClipboard,
    } = getContext('program');

    const program = getProgram();
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
    };

    const incrementDay = () => {
        if ($index === $program.days.length - 1) return;
        $index = $index + 1;
    };

    const addExercise = () => {
        let newExercise = new Exercise();
        newExercise.order = $program.days[$index].exercises.length;
        $program.days[$index].exercises = [
            ...$program.days[$index].exercises,
            newExercise,
        ];
        $program = $program;
    };

    const makeRestDay = () => {
        stashedExercises = [...$program.days[$index].exercises];
        $program.days[$index].isRestDay = true;
        $program.days[$index].exercises.length = 0;
    };

    const undoRestDay = () => {
        $program.days[$index].isRestDay = false;
        $program.days[$index].exercises = stashedExercises;
        stashedExercises = [];
    };

    const toggleWarmup = () => {
        if ($program.days[$index].warmUp) {
            $program.days[$index].warmUp = null;
        } else {
            $program.days[$index].warmUp = new WarmUp();
        }
    };

    let warmupContent: string = '';

    onDestroy(() => {
        if ($exerciseIndex > -1) {
            $exerciseIndex = -1;
        }
    });
</script>

<div
    class="fixed top-12 left-0 right-0 bottom-0 flex w-screen overflow-y-auto bg-gray-300 lg:top-0 lg:h-screen"
>
    <div
        class="{$isMobile
            ? 'relative m-auto flex h-[88vh] w-full flex-col items-center bg-gray-300'
            : 'm-0 m-0 flex h-full w-full flex-row bg-gray-100'}"
    >
        {#if $isMobile}
            <button
                class="absolute right-3 top-2 h-8 w-8 text-lg font-bold lg:w-fit"
                on:click="{() => ($index = -1)}"
            >
                <span><MdClose /></span>
            </button>
            <small class="w-fit self-start p-0.5 text-sm">
                {$program.name}{athlete ? ' - ' + athlete.name : ''}
            </small>
        {:else}
            <WeekViewNav />
        {/if}
        <div class="w-full">
            <div class="flex flex-col lg:flex-row lg:justify-between">
                <div
                    class="flex flex-grow items-center justify-center p-2 text-2xl font-bold text-yellow lg:justify-start lg:bg-gray-100"
                >
                    <button class="mx-8 h-6 w-3" on:click="{decrementDay}">
                        <FaChevronLeft />
                    </button>
                    <h1>
                        Day {$index + 1} - {$program.id
                            ? $program.days[$index].date.format('MMM DD')
                            : ''}
                    </h1>
                    <button class="mx-8 h-6 w-3" on:click="{incrementDay}">
                        <FaChevronRight />
                    </button>
                </div>
                <div class="flex flex-col items-center p-4 lg:bg-gray-100">
                    {#if $program.days[$index].isRestDay}
                        <button
                            class="text-md border-textyellow my-2 rounded border p-1 hover:bg-gray-200 hover:text-textblue"
                            on:click="{undoRestDay}"
                        >
                            Undo Rest Day
                        </button>
                    {:else}
                        <button
                            class="text-md border-textyellow my-2 rounded border p-1 hover:bg-gray-200 hover:text-textblue"
                            on:click="{makeRestDay}"
                        >
                            Make Rest Day
                        </button>
                    {/if}
                </div>
                {#if !$isMobile}
                    <div class="flex items-center lg:bg-gray-100">
                        <button
                            class="h-10 w-10 px-2 text-lg font-bold lg:w-fit"
                            on:click="{() => ($index = -1)}"
                        >
                            <span>Back to Calendar</span>
                        </button>
                    </div>
                {/if}
            </div>
            <div
                class="flex w-full flex-col items-start justify-start pr-2 lg:bg-gray-100 lg:p-2 lg:pt-6"
            >
                {#if $program.days[$index].isRestDay}
                    <h4 class="m-2 w-full text-center text-2xl font-semibold">
                        Rest Day
                    </h4>
                {:else}
                    {#if $program.days[$index].warmUp}
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
                    {#each $program.days[$index].exercises as exercise, idx}
                        {#if $exerciseIndex === idx}
                            <ExpandedExercise bind:exercise="{exercise}" />
                        {:else}
                            <ExerciseCard
                                bind:exercise="{exercise}"
                                index="{idx}"
                            />
                        {/if}
                    {/each}
                    <button
                        class="my-2 h-6 w-6 self-center text-yellow"
                        on:click="{addExercise}"
                    >
                        <FaPlus />
                    </button>
                {/if}
            </div>
        </div>
    </div>
</div>
<DayToolbar />

<style>
</style>

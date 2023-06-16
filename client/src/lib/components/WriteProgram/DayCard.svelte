<script lang="ts">
    import { getContext, onMount } from 'svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import { Day } from '$lib/classes/program/day';
    import { ExerciseType } from '$lib/classes/program/exercise/enums.js';
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte';
    import MdContentPaste from 'svelte-icons/md/MdContentPaste.svelte';
    import FaLongArrowAltLeft from 'svelte-icons/fa/FaLongArrowAltLeft.svelte';
    import FaLongArrowAltRight from 'svelte-icons/fa/FaLongArrowAltRight.svelte';
    import MdClearAll from 'svelte-icons/md/MdClearAll.svelte';
    import CalendarCard from '$lib/components/shared/layout/CalendarCard.svelte';
    import { EffortIntensity } from '../../classes/program/exercise/enums';
    import { userDB } from '../../stores/authStore';

    export let day: Day = new Day();
    export let idx: number = 0;
    let showContext: boolean = false;
    export let contextCoordinates: { x: number; y: number };

    let isPressing: boolean = false;
    let dayStatus: 'completed' | 'changed' | 'skipped' | 'unfinished' =
        'unfinished';
    export let container: HTMLDivElement;

    const {
        getProgram,
        getSelectedDayIdx,
        insertDayRight,
        insertDayLeft,
        clearExercises,
        copyDay,
        pasteDay,
    } = getContext('program');

    const program = getProgram();
    const selectedDayIdx = getSelectedDayIdx();

    $: isPressing
        ? setTimeout(() => {
              if (isPressing) {
                  showContext = true;
              }
          }, 750)
        : null;

    const handleBrowserContext = (e: PointerEvent) => {
        if (showContext) {
            showContext = false;
            contextCoordinates = { x: -1, y: -1 };
            return;
        }
        contextCoordinates = {
            x: e.clientX,
            y:
                e.clientY +
                container.scrollTop -
                container.getBoundingClientRect().top,
        };
        showContext = true;
    };

    const formatEffortString = (effort: EffortIntensity) => {
        switch (effort) {
            case EffortIntensity.EASY:
                return 'Easy';
            case EffortIntensity.MODERATE:
                return 'Moderate';
            case EffortIntensity.DIFFICULT:
                return 'Hard';
            case EffortIntensity.MAX:
                return 'Max Effort';
        }
    };

    let unit: 'kg' | 'lb' = $userDB!.preferences.weight;
    $: unit = $userDB!.preferences.weight;

    onMount(() => {
        document
            .getElementById(`day-card-${idx}`)
            .addEventListener('touchstart', e => {
                if (showContext) {
                    showContext = false;
                    document
                        .getElementById(`day-card-${idx}`)
                        .classList.remove('selected-day');
                    return;
                }
                showContext = false;
                isPressing = true;
                document
                    .getElementById(`day-card-${idx}`)
                    ?.classList.add('selected-day');
            });
        document
            .getElementById(`day-card-${idx}`)
            .addEventListener('touchend', () => {
                isPressing = false;
                if (!showContext) {
                    document
                        .getElementById(`day-card-${idx}`)
                        .classList.remove('selected-day');
                    $selectedDayIdx = idx;
                }
            });
    });
</script>

<svelte:window
    on:click="{() => {
        if (!$isMobile && showContext) {
            showContext = false;
            contextCoordinates = { x: -1, y: -1 };
        }
    }}"
/>

<CalendarCard
    onContextmenu="{handleBrowserContext}"
    onClick="{() => {
        if (!$isMobile) {
            $selectedDayIdx = idx;
        }
    }}"
    ID="day-card-{idx}"
    borderColor="border-green"
>
    <div class="flex w-full justify-between py-1 px-2">
        <h3 class="text-lg font-semibold">{idx + 1}</h3>
        {#if $program?.days[idx]?.date && $program?.athleteId}
            <h3>{$program.days[idx].date.format('ddd MMM DD')}</h3>
        {/if}
    </div>
    <div class="px-2 py-1">
        <ul class="text-sm">
            {#if $program.days[idx]}
                {#if $program.days[idx].isRestDay}
                    <div class="w-full text-center">
                        <h4 class="py-2 text-lg font-medium text-textblue">
                            Rest Day
                        </h4>
                    </div>
                {:else}
                    {#each $program.days[idx]?.exercises as exercise, idx (exercise?.id + idx)}
                        {#if exercise.type === ExerciseType.EXERCISE}
                            <h4>
                                {exercise.name ? exercise.name : 'No Name'}: {exercise.wgt(
                                    unit
                                )}{unit}
                                {exercise.sets}sets {exercise.repsPerSet}reps
                            </h4>
                        {:else if exercise.type === ExerciseType.COMPLEX}
                            <h4>
                                {exercise.nameArr.join(' + ')}: {exercise.wgt(
                                    unit
                                )}{unit}
                                {exercise.sets}sets {exercise.repArr.join(
                                    ' + '
                                )}reps
                            </h4>
                        {:else if exercise.type === ExerciseType.DURATION}
                            <h4>Test</h4>
                        {:else}
                            <h4>
                                {exercise.name ? exercise.name : 'No Name'}: {exercise.weight
                                    ? exercise.wgt(unit) + unit
                                    : formatEffortString(
                                          exercise.effortIntensity
                                      )}
                                {exercise.sets}sets {exercise.repsPerSet}reps
                            </h4>
                        {/if}
                    {/each}
                {/if}
            {/if}
        </ul>
    </div>
</CalendarCard>
{#if showContext && $isMobile}
    <div
        on:click="{e => {
            if (!isPressing && showContext) {
                e.preventDefault();
                document
                    .getElementById(`day-card-${idx}`)
                    .classList.remove('selected-day');
                setTimeout(() => (showContext = false), 100);
            }
        }}"
        class="fixed top-0 bottom-0 left-0 right-0 z-20 bg-gray-300 opacity-50"
    ></div>
    <div
        class="fixed bottom-0 right-0 left-0 z-30 grid grid-cols-2 bg-gray-400"
        on:click="{() => setTimeout(() => (showContext = false), 100)}"
    >
        <button
            class="p-4 py-6 text-lg font-semibold"
            on:click="{() => {
                copyDay(idx);
                showContext = false;
            }}"
        >
            Copy Day
        </button>
        <button
            class="p-4 py-6 text-lg font-semibold"
            on:click="{() => {
                pasteDay(idx);
                showContext = false;
            }}"
        >
            Paste Day
        </button>
        <button
            class="p-4 py-6 text-lg font-semibold"
            on:click="{() => {
                insertDayLeft(idx);
                showContext = false;
            }}"
        >
            Insert Day Left
        </button>
        <button
            class="p-4 py-6 text-lg font-semibold"
            on:click="{() => {
                insertDayRight(idx);
                showContext = false;
            }}"
        >
            Insert Day Right
        </button>
    </div>
{/if}
{#if showContext && !$isMobile}
    <div
        class="z-40 flex w-56 flex-col bg-gray-100 p-2 text-left shadow-lg"
        style="position: absolute; top: {contextCoordinates.y}px; left: {contextCoordinates.x}px"
        on:click="{() => setTimeout(() => (showContext = false), 100)}"
    >
        <button
            class="my-px flex items-center px-4 py-1 hover:bg-gray-200"
            on:click="{() => {
                copyDay(idx);
            }}"
        >
            <span class="mr-2 flex h-6 w-6 items-center px-1"
                ><FaRegCopy /></span
            >
            Copy Day
        </button>
        <button
            class="my-px flex items-center px-4 py-1 hover:bg-gray-200"
            on:click="{() => {
                pasteDay(idx);
            }}"
        >
            <span class="mr-2 flex h-5 w-6 items-center"
                ><MdContentPaste /></span
            >
            Paste Day
        </button>
        <button
            class="my-px flex items-center px-4 py-1 hover:bg-gray-200"
            on:click="{() => {
                insertDayLeft(idx);
            }}"
        >
            <span class="mr-2 flex h-6 w-6 items-center"
                ><FaLongArrowAltLeft /></span
            >
            Insert Day Left
        </button>
        <button
            class="my-px flex items-center px-4 py-1 hover:bg-gray-200"
            on:click="{() => {
                insertDayRight(idx);
            }}"
        >
            <span class="mr-2 flex h-6 w-6 items-center"
                ><FaLongArrowAltRight /></span
            >
            Insert Day Right
        </button>
        <button
            class="my-px flex items-center px-4 py-1 hover:bg-gray-200"
            on:click="{() => {
                clearExercises(idx);
            }}"
        >
            <span class="mr-2 flex h-6 w-6 items-center"><MdClearAll /></span>
            Clear Exercises
        </button>
    </div>
{/if}

<style>
    :global(.selected-day) {
        transform: scale(1.05);
        z-index: 30;
    }

    html,
    body,
    div,
    li,
    button,
    h3 {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
    }
</style>

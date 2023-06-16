<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import DayCard from '$lib/components/WriteProgram/DayCard.svelte';
    import ProgramSearch from '$lib/components/WriteProgram/modals/ProgramSearch.svelte';
    import { type Writable, writable } from 'svelte/store';
    import { Program } from '$lib/classes/program';
    import { setContext } from 'svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import { Day } from '$lib/classes/program/day';
    import ExpandedDay from '$lib/components/WriteProgram/ExpandedDay.svelte';
    import { Exercise } from '$lib/classes/program/exercise';
    import CreateProgramModal from '$lib/components/WriteProgram/modals/CreateProgramModal.svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { ProgramService } from '$lib/service/ProgramService';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import AssignAthleteModal from '$lib/components/WriteProgram/modals/AssignAthleteModal.svelte';
    import ProgramToolbarDesktop from "$lib/components/WriteProgram/toolbars/ProgramToolbarDesktop.svelte";
    import ProgramToolbarMobile from "$lib/components/WriteProgram/toolbars/ProgramToolbarMobile.svelte";

    export let selectedProgram = new Program();

    let showActionContext: boolean = false;
    let contextCoordinates: { x: number; y: number } = { x: -1, y: -1 };
    let showCreateProgram: boolean = false;
    let showAssignAthlete: boolean = false;
    let daysContainer: HTMLDivElement;

    const program: Writable<Program> = writable(selectedProgram);
    const selectedDay: Writable<Day | undefined> = writable(undefined);
    const selectedDayIdx: Writable<number> = writable(-1);
    const selectedExerciseIdx: Writable<number> = writable(-1);
    const exerciseClipboard: Writable<Exercise[]> = writable([]);
    const dayClipboard: Writable<Day[]> = writable([]);
    const programError: Writable<string> = writable('');
    const programSuccess: Writable<string> = writable('');
    const programLoading: Writable<boolean> = writable(false);

    //$: selectedProgram ? $program = writable(selectedProgram) : null

    const formatProgramDates = () => {
        let currentDate = $program.startDate;
        $program.days.forEach(d => {
            d.date = currentDate;
            currentDate = currentDate.add(1, 'day');
            return d;
        });
        $program.endDate = currentDate.subtract(1, 'day');
        $program = $program;
    };

    const updateProgram = async () => {
        $programLoading = true;
        $programSuccess = '';
        $programError = '';

        try {
            const res = await ProgramService.updateProgram($program);
            $program = res;
            $programSuccess = 'Updated';
        } catch (e) {
            $programError = `There was an error while updating ${$program.name}`;
        } finally {
            $programLoading = false;
            showActionContext = false;
        }
    };

    const insertDayLeft = (idx: number) => {
        const dayToInsert = new Day();
        let updatedDays = [];
        $program.days.forEach((d, i) => {
            if (idx === 0) {
                updatedDays.push(dayToInsert);
            }
            updatedDays.push(d);
            if (i === idx - 1 && idx > 0) {
                updatedDays.push(dayToInsert);
            }
        });
        $program.days = updatedDays;
        document
            .getElementById(`day-card-${idx}`)
            .classList.remove('selected-day');
        formatProgramDates();
    };

    const insertDayRight = (idx: number) => {
        const dayToInsert = new Day();
        let updatedDays = [];
        if (idx === $program.days.length - 1) {
            updatedDays = [...$program.days, dayToInsert];
        } else {
            $program.days.forEach((d, i) => {
                if (i === idx + 1) {
                    updatedDays.push(dayToInsert);
                }
                updatedDays.push(d);
            });
        }
        $program.days = updatedDays;
        document
            .getElementById(`day-card-${idx}`)
            .classList.remove('selected-day');
        formatProgramDates();
    };

    const clearExercises = (idx: number) => {
        $program.days[idx].exercises = [];
        $selectedDay = $selectedDay;
    };

    const copyDay = (idx: number) => {
        const day = $program.days[idx];
        const dayCopy = day.copy();
        $dayClipboard = [dayCopy, ...$dayClipboard];
        document
            .getElementById(`day-card-${idx}`)
            .classList.remove('selected-day');
    };

    const pasteDay = (idx: number) => {
        if ($dayClipboard.length < 1) {
            document
                .getElementById(`day-card-${idx}`)
                .classList.remove('selected-day');
            return;
        }
        const id = $program.days[idx].id;
        const dayToPaste = $dayClipboard.shift();
        $dayClipboard = $dayClipboard;
        $program.days[idx] = dayToPaste;
        $program.days[idx].id = id;
        $program = $program;
        $selectedDay = $selectedDay;
        document
            .getElementById(`day-card-${idx}`)
            .classList.remove('selected-day');
        formatProgramDates();
    };

    const deleteExercise = async (exercise: Exercise) => {
        $programLoading = true;
        $programSuccess = '';
        $programError = '';
        try {
            await ProgramService.deleteExercise(exercise);
        } catch (e) {
            console.log(e);
            $programError = 'There was an error trying to delete this exercise';
        } finally {
            $programLoading = false;
        }
    };

    const addDay = () => {
        const day = new Day();
        $program.days = [...$program.days, day];
        formatProgramDates();
    };

    const copyExercise = (index: number) => {
        if ($selectedDayIdx < 0) {
            return;
        }
        const exerciseCopy = $program.days[$selectedDayIdx].exercises[index].copy();
        $exerciseClipboard = [exerciseCopy, ...$exerciseClipboard];
    };

    const pasteExercise = () => {
        if ($selectedDayIdx < 0) {
            return;
        }
        const exerciseToPaste = $exerciseClipboard.shift();
        $exerciseClipboard = $exerciseClipboard;
        if ($selectedExerciseIdx > -1) {
            const id = $program.days[$selectedDayIdx].exercises[$selectedExerciseIdx].id;
            $program.days[$selectedDayIdx].exercises[$selectedExerciseIdx] = exerciseToPaste;
            $program.days[$selectedDayIdx].exercises[$selectedExerciseIdx].id = id;
        } else {
            $program.days[$selectedDayIdx].exercises.push(exerciseToPaste);
        }
        $program = $program;
        $selectedDay = $selectedDay;
    }

    setContext('program', {
        getProgram: () => program,
        getSelectedDay: () => selectedDay,
        setSelectedDay: (day: Day) => ($selectedDay = day),
        getSelectedDayIdx: () => selectedDayIdx,
        setSelectedDayIdx: (num: number) => ($selectedDayIdx = num),
        getSelectedExerciseIdx: () => selectedExerciseIdx,
        getExerciseClipboard: () => exerciseClipboard,
        setExerciseClipboard: (arr: Exercise[]) => ($exerciseClipboard = arr),
        getDayClipboard: () => dayClipboard,
        setDayClipboard: (arr: Day[]) => ($dayClipboard = arr),
        getProgramError: () => programError,
        getProgramSuccess: () => programSuccess,
        getProgramLoading: () => programLoading,
        updateProgram,
        formatProgramDates,
        insertDayLeft,
        insertDayRight,
        clearExercises,
        copyDay,
        pasteDay,
        deleteExercise,
        addDay,
        copyExercise,
        pasteExercise
    });


    $: $programError
        ? setTimeout(() => {
              programError.set('');
          }, 5000)
        : null;
    $: $programSuccess
        ? setTimeout(() => {
              programSuccess.set('');
          }, 5000)
        : null;


</script>

<div
    bind:this="{daysContainer}"
    class="relative flex h-[90vh] w-screen flex-col overflow-y-auto pb-32"
>
    {#if !$isMobile}
        <ProgramToolbarDesktop bind:showAssignAthlete={showAssignAthlete} bind:showCreateProgram={showCreateProgram} />
    {/if}
    <header class="flex {$isMobile ? 'flex-row' : 'flex-col'} items-center p-3">
        {#if $isMobile}
            <input
                type="text"
                class="mx-4 my-2 w-11/12 bg-gray-300 p-1 font-semibold"
                placeholder="Program Name"
                id="program-name-input"
                bind:value="{$program.name}"
            />
        {/if}
        <ProgramSearch />
    </header>
    <div
        class="grid {$isMobile
            ? 'grid-cols-1 gap-10'
            : 'grid-cols-7 gap-4'} w-full p-3"
    >
        {#each $program.days as day, index (day.id + index)}
            <DayCard
                bind:day="{day}"
                idx="{index}"
                container="{daysContainer}"
                bind:contextCoordinates="{contextCoordinates}"
            />
        {/each}
        <div
            class="flex aspect-square justify-center bg-gray-200 {$isMobile
                ? 'mx-10'
                : ''} items-center hover:scale-105 hover:cursor-pointer"
            on:click="{addDay}"
        >
            <div class="h-7 w-7 text-yellow">
                <FaPlus />
            </div>
        </div>
    </div>
</div>
{#if $isMobile}
    <ProgramToolbarMobile bind:showCreateProgram={showCreateProgram} />
{/if}

{#if $selectedDay}
    <ExpandedDay />
{/if}

{#if showCreateProgram}
    <CreateProgramModal bind:show="{showCreateProgram}" programName="{$program.name}" />
{/if}
{#if showAssignAthlete}
    <AssignAthleteModal bind:show="{showAssignAthlete}" bind:showCreateProgram={showCreateProgram} />
{/if}
{#if $programLoading}
    <div
        class="absolute top-0 bottom-0 right-0 left-0 z-[110] bg-gray-300 opacity-25"
    ></div>
    <div
        class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center"
    >
        <LoadingSpinner spinnerColor="fill-yellow" height="h-10" width="w-10" />
    </div>
{/if}
{#if $programSuccess}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-green bg-gray-200 p-4 text-green shadow-2xl shadow-black lg:w-4/12"
    >
        {$programSuccess}
        <button
            class="h-8 w-8 rounded-full p-1 text-green-dark hover:bg-gray-400 hover:text-green"
            on:click="{() => ($programSuccess = '')}"
        >
            <MdClose />
        </button>
    </div>
{:else if $programError}
    <div
        class="sticky bottom-5 left-10 z-10 flex w-8/12 items-center justify-between border-l-4 border-l-red-shade bg-gray-200 p-4 text-red shadow-2xl shadow-black lg:w-4/12"
    >
        {$programError}
        <button
            class="h-8 w-8 rounded-full p-1 hover:bg-gray-400 hover:text-red-shade"
            on:click="{() => ($programError = '')}"
        >
            <MdClose />
        </button>
    </div>
{/if}

<style>
    li,
    button,
    h3 {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
    }

    :global(.error-focus:focus) {
        outline: none;
        border: 2px solid #f62222;
        border-radius: 5px;
    }
</style>

<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import DayCard from "$lib/components/WriteProgram2/DayCard.svelte";
    import ProgramSearch from "$lib/components/WriteProgram2/ProgramSearch.svelte";
    import {type Writable, writable} from "svelte/store";
    import {Program} from "$lib/classes/program";
    import {onMount, setContext} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import ExpandedDay from "$lib/components/WriteProgram2/ExpandedDay.svelte";
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte'
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums";
    import CreateProgramModal from "$lib/components/WriteProgram2/CreateProgramModal.svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import MdClose from 'svelte-icons/md/MdClose.svelte'
    import AssignAthleteModal from "$lib/components/WriteProgram2/AssignAthleteModal.svelte";

    export let selectedProgram = new Program()

    let showActionContext: boolean = false
    let contextCoordinates: { x: number, y: number } = { x: -1, y: -1 }
    let showCreateProgram: boolean = false
    let showAssignAthlete: boolean = false

    const program: Writable<Program> = writable(selectedProgram)
    const selectedDay: Writable<Day | undefined> = writable(undefined)
    const selectedDayIdx: Writable<number> = writable(-1)
    const selectedExerciseIdx: Writable<number> = writable(-1)
    const exerciseClipboard: Writable<Exercise[]> = writable([])
    const dayClipboard: Writable<Day[]> = writable([])
    const programError: Writable<string> = writable('')
    const programSuccess: Writable<string> = writable('')
    const programLoading: Writable<boolean> = writable(false)

    const updateProgram = async () => {
        $programLoading = true
        $programSuccess = ''
        $programError = ''

        try {
            const res = await ProgramService.updateProgram($program)
            $program = res
            $programSuccess = 'Updated'
        } catch (e) {
            $programError = `There was an error while updating ${$program.name}`
        } finally {
            $programLoading = false
            showActionContext = false
        }
    }

    setContext('program', {
        getProgram: () => program,
        getSelectedDay: () => selectedDay,
        setSelectedDay: (day: Day) => $selectedDay = day,
        getSelectedDayIdx: () => selectedDayIdx,
        setSelectedDayIdx: (num: number) => $selectedDayIdx = num,
        getSelectedExerciseIdx: () => selectedExerciseIdx,
        getExerciseClipboard: () => exerciseClipboard,
        setExerciseClipboard: (arr: Exercise[]) => $exerciseClipboard = arr,
        getDayClipboard: () => dayClipboard,
        setDayClipboard: (arr: Day[]) => $dayClipboard = arr,
        getProgramError: () => programError,
        getProgramSuccess: () => programSuccess,
        getProgramLoading: () => programLoading,
        updateProgram
    })

    const generateCSV = () => {
        let str = "Day 1\nExercise,Weight,Sets,Reps,Complete,Weight Completed,RPS Completed,Sets Completed,Total Reps Completed,Notes\n";
        for(let i=0;i< $program.days.length; i++) {
            if($program.days[i].exercises.length < 1) continue
            if (i > 0) {
                str += "Day " + (i+1) + ": " + $program.days[i].date.format('ddd MMM DD') + "\n"
            }
            const d = $program.days[i]
            if (d.isRestDay) {
                str += "Rest Day\n"
                continue
            }
            for(let j = 0; j < d.exercises.length; j++) {
                const e = d.exercises[j]
                if (e.type === ExerciseType.EXERCISE) {
                    str += e.name + "," +
                        e.weight + "," +
                        e.sets + "," +
                        e.repsPerSet + "," +
                        e.isComplete + "," +
                        (e.isComplete ? e.weightCompleted : "0") + "," +
                        (e.isComplete && e.setsCompleted > 0 ? (e.totalRepsCompleted / e.setsCompleted) : "0") + "," +
                        (e.isComplete ? e.setsCompleted : "0") + "," +
                        (e.isComplete ? e.totalRepsCompleted : "0") + "," +
                        e.notes + "\n"
                } else if (e.type === ExerciseType.COMPLEX) {
                    str += e.nameArr.join('+') + "," +
                        e.weight + "," +
                        e.sets + "," +
                        e.repArr.join('+') + "," +
                        e.isComplete + "," +
                        (e.isComplete ? e.weightCompleted : "0") + "," +
                        (e.isComplete ? e.repCompletedArr.join('+') : e.repArr.map(_ => 0).join('+')) + "," +
                        (e.isComplete ? e.setsCompleted : "0") + "," +
                        (e.isComplete ? e.repArr.reduce((a, b) => a+b) : "0") + "," +
                        e.notes + "\n"
                }
            }
        }

        const downloadLink = document.createElement('a');
        const csv = str
        const blob = new Blob(["\ufeff", csv], {type: 'text/csv'});
        const url = URL.createObjectURL(blob);
        const exportFilename = $program.name;
        downloadLink.href = url;
        downloadLink.setAttribute('download', exportFilename);
        downloadLink.click()
    }

    const addDay = () => {
        const day = new Day()
        $program.days = [...$program.days, day]
        $program = $program
    }




    $: $programError ? setTimeout(() => {programError.set('')}, 5000) : null
    $: $programSuccess ? setTimeout(() => {programSuccess.set('')}, 5000) : null

</script>

<div class="relative w-screen flex flex-col lg:p-3 h-[83vh] overflow-y-auto pb-32">
    <header class="flex  {$isMobile ? 'flex-row' : 'flex-col'} p-2 items-center">
        <input type="text"
               class="bg-gray-300 p-1 {$isMobile ? 'w-11/12' : 'w-full'} mx-4 my-2 font-semibold"
               placeholder="Program Name"
               bind:value={$program.name}
        >
        <ProgramSearch />
    </header>
    <div class="grid {$isMobile ? 'grid-cols-2 gap-2' : 'grid-cols-7 gap-4'} w-full px-2">
        {#each $program.days as day, index (day.id+index)}
            <DayCard bind:day={day}
                     idx={index}
                     bind:contextCoordinates={contextCoordinates}
            />
        {/each}
        <div class="bg-gray-200 aspect-square flex justify-center items-center hover:scale-105 hover:cursor-pointer" on:click={addDay}>
            <div class="h-7 w-7 text-yellow">
                <FaPlus />
            </div>
        </div>
    </div>
</div>
<button class="absolute bottom-5 left-10 right-10 p-4 bg-yellow text-gray-300 rounded font-bold"
        on:click={() => showActionContext = !showActionContext}
>
    Options
</button>
{#if $selectedDay}
    <ExpandedDay />
{/if}
{#if showActionContext}
    <div class="absolute top-0 bottom-0 right-0 left-0 z-0" on:click={() => showActionContext = false}></div>
    <div class="absolute transition-all transform ease-in-out bottom-0 bg-gray-400 flex flex-col w-screen right-0 left-0 text-center z-10
        {showActionContext ? ' -translate-y-0' : ' translate-y-0'}">
        <button class="h-6 w-full pt-2" on:click={() => showActionContext = false}>
            <FaChevronDown />
        </button>
        {#if !$program.id}
            <button class="w-full p-4 text-lg" on:click={() => showCreateProgram = true}>
                Create Program
            </button>
        {/if}
        {#if !$program.athleteId}
            <button class="w-full p-4 text-lg" on:click={() => showAssignAthlete = true}>
                Assign To Athlete
            </button>
        {/if}
        <button class="w-full p-4 text-lg" on:click={generateCSV}>
            Export to CSV
        </button>
        {#if $program.id}
            <button class="w-full p-4 text-lg" on:click={updateProgram}>
                Save
            </button>
        {/if}
    </div>
{/if}
{#if showCreateProgram}
    <CreateProgramModal bind:show={showCreateProgram} />
{/if}
{#if showAssignAthlete}
    <AssignAthleteModal bind:show={showAssignAthlete} />
{/if}
{#if $programLoading}
    <div class="absolute z-[110] top-0 bottom-0 right-0 left-0 bg-gray-300 opacity-25">

    </div>
    <div class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center">
        <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
    </div>

{/if}
{#if $programSuccess}
    <div class="sticky bottom-5 left-10 z-10 text-green border-l-4 border-l-green bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
        {$programSuccess}
        <button class="h-8 w-8 hover:bg-gray-400 text-green-dark rounded-full hover:text-green p-1" on:click={() => $programSuccess = ''}>
            <MdClose />
        </button>
    </div>
{:else if $programError}
    <div class="sticky bottom-5 left-10 z-10 text-red border-l-4 border-l-red-shade bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
        {$programError}
        <button class="h-8 w-8 hover:bg-gray-400 rounded-full hover:text-red-shade p-1" on:click={() => $programError = ''}>
            <MdClose />
        </button>
    </div>
{/if}


<style>
    li, button, h3 {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>
<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import DayCard from "$lib/components/WriteProgram/DayCard.svelte";
    import ProgramSearch from "$lib/components/WriteProgram/ProgramSearch.svelte";
    import {type Writable, writable} from "svelte/store";
    import {Program} from "$lib/classes/program";
    import {onMount, setContext} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import ExpandedDay from "$lib/components/WriteProgram/ExpandedDay.svelte";
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte'
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums";
    import CreateProgramModal from "$lib/components/WriteProgram/CreateProgramModal.svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import MdClose from 'svelte-icons/md/MdClose.svelte'
    import AssignAthleteModal from "$lib/components/WriteProgram/AssignAthleteModal.svelte";
    import FaCaretDown from 'svelte-icons/fa/FaCaretDown.svelte'
    import FaRegSave from 'svelte-icons/fa/FaRegSave.svelte'
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte'
    import FaFileExport from 'svelte-icons/fa/FaFileExport.svelte'
    import FaUserPlus from 'svelte-icons/fa/FaUserPlus.svelte'
    import MdAdd from 'svelte-icons/md/MdAdd.svelte'
    import dayjs from "dayjs";
    import {goto} from "$app/navigation";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import ProgramStats from "../Stats/ProgramStats.svelte";
    import {team} from "../../stores/teamStore";
    import {userDB} from "../../stores/authStore";

    export let selectedProgram = new Program()

    let showActionContext: boolean = false
    let contextCoordinates: { x: number, y: number } = { x: -1, y: -1 }
    let showCreateProgram: boolean = false
    let showAssignAthlete: boolean = false
    let showFileMenu: boolean = false
    let daysContainer: HTMLDivElement

    const program: Writable<Program> = writable(selectedProgram)
    const selectedDay: Writable<Day | undefined> = writable(undefined)
    const selectedDayIdx: Writable<number> = writable(-1)
    const selectedExerciseIdx: Writable<number> = writable(-1)
    const exerciseClipboard: Writable<Exercise[]> = writable([])
    const dayClipboard: Writable<Day[]> = writable([])
    const programError: Writable<string> = writable('')
    const programSuccess: Writable<string> = writable('')
    const programLoading: Writable<boolean> = writable(false)

    //$: selectedProgram ? $program = writable(selectedProgram) : null

    const formatProgramDates = () => {
        let currentDate = $program.startDate
        $program.days.forEach(d => {
            d.date = currentDate
            currentDate = currentDate.add(1, 'day')
            return d
        })
        $program.endDate = currentDate.subtract(1, 'day')
        $program = $program
        console.log('program after dates', $program)
    }

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

    const createProgram = async (newProgram: Program) => {
        $programError = ''
        $programSuccess = ''
        $programLoading = true
        try {
            const res = await ProgramService.createProgram(newProgram)
            if (!res.id) {
                throw new Error('Could not create program')
            }
            $programSuccess = 'Saved!'
            $program = res
            await goto(`/home/coach/program/${res.id}`)
        } catch (e) {
            console.log(e)
            $programError = 'Could not create this program at this time'
        } finally {
            $programLoading = false
        }
    }


    const insertDayLeft = (idx: number) => {
        const dayToInsert = new Day()
        let updatedDays = []
        $program.days.forEach((d, i) => {
            if (idx === 0) {
                updatedDays.push(dayToInsert)
            }
            updatedDays.push(d)
            if (i === idx-1 && idx > 0) {
                updatedDays.push(dayToInsert)
            }

        })
        $program.days = updatedDays
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
        formatProgramDates()
    }

    const insertDayRight = (idx: number) => {
        const dayToInsert = new Day()
        let updatedDays = []
        if (idx === $program.days.length - 1) {
            updatedDays = [...$program.days, dayToInsert]
        } else {
            $program.days.forEach((d, i) => {
                if (i === idx+1) {
                    updatedDays.push(dayToInsert)
                }
                updatedDays.push(d)
            })
        }
        $program.days = updatedDays
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
        formatProgramDates()
    }

    const clearExercises = (idx: number) => {
        $program.days[idx].exercises = []
        $selectedDay = $selectedDay
    }

    const copyDay = (idx: number) => {
        const day = $program.days[idx]
        const dayCopy = JSON.parse(JSON.stringify(day)) as Day
        dayCopy.id = ''
        dayCopy.exercises.forEach((e, i) => {
            e.id = ''
            e.dropSets.forEach(d => d.id = '')
        })
        $dayClipboard = [dayCopy]
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
    }

    const pasteDay = (idx: number) => {
        if ($dayClipboard.length < 1) {
            document.getElementById(`day-card-${idx}`).classList.remove('selected-day')

            return
        }
        program.update((prev) => {
            console.log(prev)
            let id = prev.days[idx].id
            prev.days[idx] = {...$dayClipboard[0], id}
            return prev
        })
        $program = $program
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
        $selectedDay = $selectedDay
        formatProgramDates()
    }

    const deleteExercise = async (exercise: Exercise) => {
        $programLoading = true
        $programSuccess = ''
        $programError = ''
        try {
            await ProgramService.deleteExercise(exercise)
        } catch (e) {
            console.log(e)
            $programError = 'There was an error trying to delete this exercise'
        } finally {
            $programLoading = false
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
        updateProgram,
        formatProgramDates,
        insertDayLeft,
        insertDayRight,
        clearExercises,
        copyDay,
        pasteDay,
        deleteExercise
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
        formatProgramDates()
    }

    const focusNameInput = () => {
        showActionContext = false
        const input = document.getElementById('program-name-input')
        input.focus()
        input.classList.add('error-focus')
        input.addEventListener('focusout', () => {
            input.classList.remove('error-focus')
        })
    }

    const makeACopy = async () => {
        if (!$program.id) return

        let programCopy = JSON.parse(JSON.stringify($program))
        programCopy.id = ''
        programCopy.name = 'Copy of ' + programCopy.name
        programCopy.startDate = null
        programCopy.endDate = null
        programCopy.athleteId = null
        programCopy.isCurrent = false
        programCopy.days.forEach(d => {
            d.id = ''
            d.date = null
            d.exercises.forEach(e => {
                e.id = ''
                e.weightCompleted = 0
                e.secondsPerSetCompleted = 0
                e.totalRepsCompleted = 0
                e.repCompletedArr = []
                e.distanceCompletedMeters = 0
                e.dropSets.forEach(ds => {
                    ds.id = ''
                    ds.weightCompleted = 0
                    ds.secondsPerSetCompleted = 0
                    ds.totalRepsCompleted = 0
                    ds.repCompletedArr = []
                    ds.distanceCompletedMeters = 0
                    return ds
                })
                return e
            })
            return d
        })
        console.log(programCopy)
        await createProgram(programCopy)
    }

    const deleteProgram = async () => {
        $programLoading = true
        $programSuccess = ''
        $programError = ''
        try {
            await ProgramService.deleteProgram($program.id)
            $programSuccess = 'Successfully deleted ' + $program.name + '. Redirecting you to home...'
            $userDB.coachData.programs = $userDB.coachData.programs.filter(p => p.id !== $program.id)
            if ($team) {
                 setTimeout( async () => {
                    await goto(`/home/coach/team/${$team.id}/programs`)
                 }, 2000)
            } else {
                 setTimeout( async () => {
                    await goto('/home')
                 }, 2000)
            }
        } catch (e) {
            console.log(e)
            $programError = 'Error, can\'t delete ' + $program.name + ' at this time.'
        } finally {
            $programLoading = false
        }
    }


    $: $programError ? setTimeout(() => {programError.set('')}, 5000) : null
    $: $programSuccess ? setTimeout(() => {programSuccess.set('')}, 5000) : null

</script>

<div bind:this={daysContainer} class="relative w-screen flex flex-col h-[83vh] overflow-y-auto pb-32">
    {#if !$isMobile}
        <nav class="relative bg-gray-100 flex flex-col p-2">
            <div>
                <input type="text" class="bg-gray-300 p-1 rounded w-3/12" bind:value={$program.name} placeholder="Program Name" id="program-name-input">
                <p><i></i></p>
            </div>
            <div class="py-2">
                <button class="bg-yellow rounded text-gray-300 text-md font-medium px-2 p-1 mx-2 disabled:bg-gray-400"
                        on:click={updateProgram}
                        disabled={!$program.id}
                >
                    Save
                </button>
                <div class="relative inline-block dropdown">
                    <button class="flex items-center hover:bg-gray-400 rounded p-1" on:click={() => showFileMenu = !showFileMenu}>
                        File
                        <span class="h-3 mx-1"><FaCaretDown /></span>
                    </button>
                    {#if showFileMenu}
                        <div class="fixed top-0 right-0 left-0 bottom-0 z-[114]" on:click={() => showFileMenu = !showFileMenu}></div>
                        <div class="bg-gray-400 shadow-lg absolute w-56 origin-top-right p-2 z-[115]" on:click={() => setTimeout(() => showFileMenu = false, 100)}>
<!--                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200 disabled:text-gray-100 disabled:hover:bg-gray-400">-->
<!--                                <span class="h-5 mr-2"><FaRegSave /></span>-->
<!--                                Save-->
<!--                            </button>-->
                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200" on:click={() => showCreateProgram = true}>
                                <span class="h-5 mr-2"><MdAdd /></span>
                                New
                            </button>
                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200" on:click={makeACopy}>
                                <span class="h-5 mr-2"><FaRegCopy /></span>
                                Make a copy
                            </button>
                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200" on:click={generateCSV}>
                                <span class="h-5 mr-2"><FaFileExport /></span>
                                Export to CSV
                            </button>
                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200" on:click={() => $program.name ? showAssignAthlete = true : focusNameInput()}>
                                <span class="h-5 mr-2"><FaUserPlus /></span>
                                Assign to athlete
                            </button>
                            <hr>
                            <button class="p-2 w-full text-left flex items-center hover:bg-gray-200 text-red-shade" on:click={deleteProgram}>
                                <span class="h-5 mr-2"><FaTrashAlt /></span>
                                Delete Program
                            </button>
                        </div>
                    {/if}
                </div>
                <a href="/home/coach/program/{$program.id}/stats">
                    <button>
                        Program Stats
                    </button>
                </a>
            </div>
        </nav>
    {/if}
    <header class="flex  {$isMobile ? 'flex-row' : 'flex-col'} p-3 items-center">
        {#if $isMobile}
            <input type="text"
                   class="bg-gray-300 p-1 w-11/12 mx-4 my-2 font-semibold"
                   placeholder="Program Name"
                   id="program-name-input"
                   bind:value={$program.name}
            >
        {/if}
        <ProgramSearch />
    </header>
    <div class="grid {$isMobile ? 'grid-cols-1 gap-10' : 'grid-cols-7 gap-4'} w-full p-3" bind:this={daysContainer}>
        {#each $program.days as day, index (day.id+index)}
            <DayCard bind:day={day}
                     idx={index}
                     container={daysContainer}
                     bind:contextCoordinates={contextCoordinates}
            />
        {/each}
        <div class="bg-gray-200 aspect-square flex justify-center {$isMobile ? 'mx-10' : ''} items-center hover:scale-105 hover:cursor-pointer" on:click={addDay}>
            <div class="h-7 w-7 text-yellow">
                <FaPlus />
            </div>
        </div>
    </div>

</div>
{#if $isMobile}
    <button class="absolute bottom-5 left-10 right-10 p-4 bg-yellow text-gray-300 rounded font-bold"
            on:click={() => showActionContext = !showActionContext}
    >
        Options
    </button>

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
                <button class="w-full p-4 text-lg" on:click={() => $program.name ? showAssignAthlete = true : focusNameInput()}>
                    Assign To Athlete
                </button>
            {/if}
            {#if $program.id}
                <button class="w-full p-4 text-lg">
                    Make a Copy
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
{/if}

{#if $selectedDay}
    <ExpandedDay />
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

    :global(.error-focus:focus) {
        outline: none;
        border: 2px solid #f62222;
        border-radius: 5px;
    }
</style>
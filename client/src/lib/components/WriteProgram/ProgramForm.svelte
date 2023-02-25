<script lang="ts">
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {Program} from "$lib/classes/program";
    import {afterUpdate, onDestroy, onMount} from "svelte";
    import {Day, WarmUp} from "$lib/classes/program/day";
    import type {Dayjs} from "dayjs";
    import dayjs from "dayjs";
    import {userDB} from "$lib/stores/authStore";
    import {Exercise} from "$lib/classes/program/exercise";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import WeekNav from "$lib/components/WriteProgram/WeekNav.svelte";
    import ExerciseForm from "$lib/components/WriteProgram/ExerciseForm.svelte";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import ComplexExerciseForm from "$lib/components/WriteProgram/ComplexExerciseForm.svelte";
    import FaFileDownload from 'svelte-icons/fa/FaFileDownload.svelte'
    import {isMobile} from "$lib/stores/authStore.js";
    import CompletedExercise from "$lib/components/WriteProgram/CompletedExercise.svelte";
    import {AthleteData} from "$lib/classes/user/athlete";

    export let handleSubmit
    export let initialIndex: number = -1
    export let athleteId: string = ''
    export let showOverview: boolean

    let selectedIndex: number = initialIndex
    let selectedDayId: string = ''
    let showDateDropdown: boolean = false
    let inputFocused: boolean = false
    let athlete: AthleteData

    $: athleteOptions = []


    const formatDateString = (date: Date) => {
        return `${date.getFullYear()}-${(date.getMonth().toString().length < 2 ? `0${date.getMonth()+1}` : (date.getMonth()+1))}-${date.getDate().toString().length < 2 ? '0' + (date.getDate()) : (date.getDate())}`
    }

    let startDateString = $program?.startDate ? formatDateString($program.startDate) : ''
    let endDateString = $program?.endDate ? formatDateString($program.endDate) : ''


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

    const addDay = (currentDate: Dayjs, index: number) => {
        if (index > $program.days.length - 1) {
            let day = new Day()
            day.date = currentDate

            program.update(p => {
                p.days.push(day)
                return p
            })
        } else {
            program.update(p => {
                p.days[index].date = currentDate
                return p
            })
        }
    }

    const addExercise = () => {
        program.update(p => {
            const newExercise: Exercise = new Exercise()
            newExercise.order = p.days[selectedIndex].exercises.length

            p.days[selectedIndex].exercises.push(newExercise)
            return p
        })
    }

    const removeLastExercise = () => {
        program.update(p => {
            p.days[selectedIndex].exercises.pop()
            return p
        })
    }

    const incrementSelectedIndex = () => {
        selectedIndex === $program.days.length - 1 ? null : selectedIndex++
        selectedDayId = $program.days[selectedIndex].id
    }
    const decrementSelectedIndex = () => {
        selectedIndex === 0 ? null : selectedIndex--
        selectedDayId = $program.days[selectedIndex].id
    }

    const handleHotKeys = (e: KeyboardEvent) => {
        if (inputFocused) return
        e.stopPropagation()

        switch (e.key) {
            case '+':
                if (selectedIndex > -1)
                    addExercise()
                break;
            case 'ArrowRight':
                if (selectedIndex > -1)
                    incrementSelectedIndex()
                break;
            case 'ArrowLeft':
                if(selectedIndex > -1)
                    decrementSelectedIndex()
                break;
        }
    }

    const handleDateChange = (startDate: Dayjs, endDate: Dayjs) => {
        if ((!startDate || !endDate) || (endDate.toDate() < startDate.toDate())) {
            return
        }

        startDateString = formatDateString(startDate.toDate())
        endDateString = formatDateString(endDate.toDate())

        program.update(p => {
            p.endDate = endDate.toDate()
            p.startDate = startDate.toDate()
            return p
        })

        const daysDiff = endDate.diff(startDate, 'days')

        let currentDate = startDate
        for (let i = 0; i <= daysDiff; i++) {
            addDay(currentDate, i)
            currentDate = currentDate.add(1, 'day')
        }
        selectedIndex = selectedIndex === -1 ? 0 : selectedIndex
        selectedDayId = $program.days[selectedIndex].id
        $programSuccess = 'Success'
    }

    const handleChangeAthlete = (athleteId: string) => {
        const athlete = $userDB!.coachData!.athletes!.find(a => a.id === athleteId)
        program.update(p => {
            p.athleteId = athleteId
            p.teamId = athlete?.team?.id ? athlete.team.id : null
            return p
        })
        console.log('program after athlete-stats change', $program)
    }

    const toggleRestDay = () => {
        if ($program) {
            if (!$program.days[selectedIndex].isRestDay) {
                $program.days[selectedIndex].exercises = []
            }
            $program.days[selectedIndex].isRestDay = !$program.days[selectedIndex].isRestDay
        }
    }

     const toggleIsMax = (exerciseIdx: string) => {

         program.update(prev => {
             const val = prev.days[selectedIndex].exercises[exerciseIdx].isMax
             prev.days[selectedIndex].exercises[exerciseIdx] = {
                 ...prev.days[selectedIndex].exercises[exerciseIdx],
                 isMax: !val,
             }
             return prev
         })

        console.log($program.days[selectedIndex].exercises[exerciseIdx])
    }

    onMount(() => {
        if (document)
            document.addEventListener('keydown', handleHotKeys)
        if ($userDB?.coachData?.athletes && !athleteId && !$program.id) {
            athleteOptions = $userDB.coachData.athletes.map(a => ({ name: a.name, id: a.id }))
            athleteOptions = [...athleteOptions, { name: $userDB.username, id: $userDB.athleteData!.id }]
            program.update(prev => ({ ...prev, coachId: $userDB!.coachData!.id! }))
        } else if (athleteId && $userDB?.coachData?.athletes) {
            $program.athleteId = athleteId
            $program.coachId = $userDB.coachData.id
            const athleteObj = $userDB.coachData.athletes.find(a => a.id === athleteId)
            if (athleteObj) {
                athleteOptions = [{ name: athleteObj.name, id: athleteId }]
                $program.name = `New Program for ${athleteObj.name}`
                athlete = athleteObj
            }
        } else if (selectedIndex > -1 && $userDB?.coachData?.athletes && $userDB?.athleteData) {
            selectedDayId = $program.days[selectedIndex].id
            const athleteName = $userDB.coachData.athletes.find(a => a.id === $program.athleteId)
            if (!athleteName) {
                athleteOptions = [{ name: $userDB.username, id: $userDB.athleteData.id}]
                athlete = $userDB.athleteData
            } else {
                athleteOptions = [{ name: athleteName.name, id: $program.athleteId }]
                athlete = athleteName
            }
        }
        if (selectedIndex > -1 && $program?.days[selectedIndex]?.exercises.length > 0) {
            $program.days[selectedIndex].exercises = $program.days[selectedIndex].exercises.sort((a, b) => a.order - b.order)
        }
    })

    const addWarmup = () => {
        $program.days[selectedIndex].warmUp = new WarmUp()
    }

    afterUpdate(() => {
        if ($userDB && athleteOptions.length < 1 && selectedIndex > -1) {
            program.update(p => {
                p.coachId = $userDB?.coachData?.id!
                return p
            })
        }

        if ($programError) {
            setTimeout(() => {
                programError.set('')
            }, 5000)
        }

        if ($programSuccess) {
            setTimeout(() => {
                programSuccess.set('')
            }, 5000)
        }
    })

</script>

<button on:click={() => showOverview = true} class="text-lg font-semibold text-textblue p-2 lg:ml-24">
    Back to overview
</button>
<div class="flex w-screen justify-center">
    <WeekNav bind:selectedDayIndex={selectedIndex} bind:dayId={selectedDayId} />

    <div class="flex flex-col self-start w-9/12 h-screen overflow-scroll">
        <div class="flex flex-col sticky lg:sticky bg-gray-200 top-0 mb-10 p-2 z-50 shadow-md shadow-black lg:mb-4">
            <div class="flex flex-col lg:flex-row justify-start">
                <div class="flex items-center p-1 lg:p-2">
                    <input type="text"
                           placeholder="Program Name"
                           name="name"
                           on:focus={() => inputFocused = true}
                           on:blur={() => inputFocused = false}
                           bind:value={$program.name}
                           class="p-2 bg-gray-300 text-md lg:text-xl font-bold">
                </div>

                <div class="p-0 lg:p-2 flex justify-start w-9/12 items-center">
                    <div class="flex flex-col mr-4 w-7/12">
                        <label class="text-sm">Start Date</label>
                        <input type="date"
                               class="p-1 bg-gray-300 text-md text-textgray decoration-color-textgray"
                               name="startDate"
                               bind:value={startDateString}
                               on:change={(e) => handleDateChange(dayjs(e.target.value), dayjs($program.endDate))}>
                    </div>
                    <div class="flex flex-col w-7/12">
                        <label class="text-sm">End Date</label>
                        <input type="date"
                               name="endDate"
                               class= "bg-gray-300 text-md p-1"
                               bind:value={endDateString}
                               on:change={(e) => handleDateChange(dayjs($program.startDate), dayjs(e.target.value))}>
                    </div>
                </div>
            </div>
            <div>
                <div class="flex self-start">
                    <div class="flex flex-col w-6/12">
                        <label>Athlete</label>
                        <select class="bg-gray-300 text-textgray p-1"
                                disabled={$program.id}
                                on:change={(e) => handleChangeAthlete(e.target.value)}
                        >
                            <option disabled selected>No Athlete Selected</option>
                            {#each athleteOptions as athlete}
                                <option selected={$program.athleteId === athlete.id} value={athlete.id}>
                                    {athlete.name}
                                </option>
                            {/each}
                        </select>
                    </div>

                    <div class="flex flex-col-reverse lg:flex-row lg:p-4 items-center justify-center">
                        <input checked={$program.isCurrent}
                               type="checkbox"
                               id="current_program"
                               on:change={(e) => $program.isCurrent = e.target.checked}>
                        <label for="current_program">Current Program</label>
                    </div>
                </div>
                {#if !$isMobile}
                    <div class="flex justify-between">
                        <div>
                            <button type="button" class="bg-gray-200 text-yellow hover:bg-gray-300 p-2" on:click={addWarmup}>
                                Add Warmup
                            </button>
                            {#if $program?.days.length > 0}
                                <button type="button" class="bg-gray-200 text-yellow hover:bg-gray-300 p-2" on:click={addExercise}>
                                    Add exercise (Shift +)
                                </button>
                                <button type="button" class="bg-gray-200 text-yellow hover:bg-gray-300 p-2 mx-2" on:click={toggleRestDay}>
                                    {$program.days[selectedIndex].isRestDay ? 'Undo rest day (Shift *)' : 'Make rest day (Shift *)'}
                                </button>
                            {/if}
                        </div>
                        <div class="flex items-center">
                            <button class=" h-6 mx-4 text-yellow-lt" title="Download CSV" on:click={generateCSV}>
                                <FaFileDownload />
                            </button>
                            <button type="button"
                                    on:click={(e) => handleSubmit(e, $program)}
                                    class="text-gray-300 bg-yellow hover:bg-yellow-shade p-2 px-4 rounded-md">
                                Save
                            </button>
                        </div>
                    </div>
                {/if}
            </div>
        </div>

        {#if $program && $program?.days?.length > 0 && selectedIndex > -1}
            <div class="flex justify-center align-middle">
                <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                     on:click={decrementSelectedIndex}>
                    <FaAngleLeft />
                </div>
                <div class="m-0 mx-2 self-center text-xl relative z-30" on:click={() => showDateDropdown = !showDateDropdown}>
                    <h4>{$program.days[selectedIndex].date.format('ddd MMM DD \'YY')}</h4>
                    {#if showDateDropdown}
                        <div class="absolute bg-gray-400 h-56 max-h-56 overflow-scroll">
                            {#each $program.days as day}
                                <p class="z-20 text-base hover:bg-gray-200 hover:cursor-pointer p-2"
                                   on:click={() => { selectedIndex = $program.days.findIndex(d => d.id === day.id); setTimeout(() => showDateDropdown = false, 100) }}
                                >
                                    {day.date.format('ddd MMM DD')}
                                </p>
                            {/each}
                        </div>
                    {/if}
                </div>
                <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                     on:click={incrementSelectedIndex}>
                    <FaAngleRight />
                </div>
            </div>
        {/if}
        {#if selectedIndex > -1 && $program}
            <div class="mb-24">
            {#if $program?.days[selectedIndex]?.warmUp}
                <div class="w-full p-2">
                    <textarea class="bg-gray-300 w-full" bind:value={$program.days[selectedIndex].warmUp.instructions}></textarea>
                </div>
            {/if}
            {#if $program?.days[selectedIndex]?.isRestDay === false && $program?.days[selectedIndex]?.exercises.length > 0}
                {#each $program?.days[selectedIndex]?.exercises as exercise, idx (idx)}
                    {#if !exercise.isComplete}
                        {#if exercise.type === ExerciseType.EXERCISE || exercise.type === ExerciseType.DURATION}
                            <ExerciseForm
                                    bind:exercise={exercise}
                                    bind:selectedDayIndex={selectedIndex}
                                    bind:inputFocused={inputFocused}
                                    exerciseIndex={idx}
                            />
                        {:else}
                            <ComplexExerciseForm
                                    bind:exercise={exercise}
                                    bind:selectedDayIndex={selectedIndex}
                                    bind:inputFocused={inputFocused}
                                    exerciseIndex={idx}
                            />
                        {/if}
                    {:else}
                        <CompletedExercise bind:exercise={exercise} athlete={athlete} />
                    {/if}

                {/each}
            {:else if $program?.days[selectedIndex]?.isRestDay === true}
                <div class="flex justify-center m-8 font-bold text-2xl">
                    Rest Day
                </div>
            {:else}
                <div class="flex justify-center m-8 font-bold text-xl">
                    <i>You haven't added any exercises to this day yet</i>
                </div>
            {/if}
            </div>
        {/if}
    </div>
    {#if $isMobile}
        <footer class="fixed bottom-0 left-0 right-0 flex justify-between bg-gray-200 p-2">
            <div>
                {#if $program?.days.length > 0}
                    <button type="button" class="bg-gray-400 rounded-md text-white hover:bg-gray-300 p-2" on:click={addExercise}>
                        Add exercise
                    </button>
                {/if}
            </div>
            <div>
                <button type="button" class="bg-gray-400 rounded-md text-white hover:bg-gray-300 p-2 mx-2" on:click={toggleRestDay}>
                    {$program.days[selectedIndex]?.isRestDay ? 'Undo rest day' : 'Make rest day'}
                </button>
            </div>
            <button type="button"
                    on:click={(e) => handleSubmit(e, $program)}
                    class="text-gray-300 bg-yellow hover:bg-yellow-shade mx-2 px-5 rounded-md">
                Save
            </button>
        </footer>
    {/if}
</div>


<style>
    .exercise-input {
        background: transparent;
        padding: .5em;
        font-weight: bold;
    }

    .exercise-input:focus {
        outline: none;
        background: lightgray;
    }

    .switch {
        position: relative;
        display: inline-block;
        width: 60px;
        height: 34px;
    }

    /* Hide default HTML checkbox */
    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .slider:before {
        position: absolute;
        content: "";
        height: 26px;
        width: 26px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked + .slider {
        background-color: #2196F3;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #2196F3;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }
</style>
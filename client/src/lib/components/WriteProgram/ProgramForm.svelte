<script lang="ts">
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {Program} from "$lib/classes/program";
    import {afterUpdate, onDestroy, onMount} from "svelte";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import type {Dayjs} from "dayjs";
    import {userDB} from "$lib/stores/authStore";
    import {Exercise} from "$lib/classes/program/exercise";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import WeekNav from "$lib/components/WriteProgram/WeekNav.svelte";
    import ExerciseForm from "$lib/components/WriteProgram/ExerciseForm.svelte";

    export let handleSubmit
    export let initialIndex: number = -1
    let selectedIndex: number = initialIndex
    let selectedDayId: string = ''
    $: athleteOptions = []

    const formatDateString = (date: Date) => {
        return `${date.getFullYear()}-${(date.getMonth().toString().length < 2 ? `0${date.getMonth()+1}` : (date.getMonth()+1))}-${date.getDate().toString().length < 2 ? '0' + (date.getDate()) : (date.getDate())}`
    }

    let startDateString = $program?.startDate ? formatDateString($program.startDate) : ''
    let endDateString = $program?.endDate ? formatDateString($program.endDate) : ''


    const generateCSV = () => {
        let str = "Day 1\nExercise,Weight,Sets,Reps,Notes\n";
        // for(let i=0;i< $program.days.length; i++) {
        //     if($program.days[i].exercises.length < 1) continue
        //     if (i > 0) str += "Day " + (i+1) + "\n"
        //     const d = $program.days[i]
        //     for(let j = 0; j < d.exercises.length; j++) {
        //         str += d.exercises[j].name + "," +
        //             d.exercises[j].weight + "," +
        //             d.exercises[j].repsPerSet + "," +
        //             d.exercises[j].sets + "," +
        //             d.exercises[j].notes + "\n"
        //     }
        // }

        //const downloadLink = document.getElementById('download-btn');
        const csv = str
        const blob = new Blob(["\ufeff", csv], {type: 'text/csv'});
        const url = URL.createObjectURL(blob);
        // const exportFilename = programInput.value;
        // downloadLink.href = url;
        // downloadLink.setAttribute('download', exportFilename);
        // downloadLink.hidden = false;
    }

    const addDay = (currentDate: Dayjs, index: number) => {
        if (index > $program.days.length - 1) {
            let day = new Day()
            day.date = currentDate.toDate()

            program.update(p => {
                p.days.push(day)
                return p
            })
        } else {
            program.update(p => {
                p.days[index].date = currentDate.toDate()
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
        e.stopPropagation()
        console.log(e.key)
        switch (e.key) {
            case '+':
                if (selectedIndex > -1)
                    addExercise()
                break;
            case '-':
                removeLastExercise()
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
        console.log(daysDiff)

        let currentDate = startDate
        for (let i = 0; i <= daysDiff; i++) {
            addDay(currentDate, i)
            currentDate = currentDate.add(1, 'day')
        }
        selectedIndex = selectedIndex === -1 ? 0 : selectedIndex
        selectedDayId = $program.days[selectedIndex].id
        console.log(selectedDayId)
    }

    const handleChangeAthlete = (athleteId: string) => {
        const athlete = $userDB!.coachData!.athletes!.find(a => a.id === athleteId)
        program.update(p => {
            p.athleteId = athleteId
            p.teamId = athlete.team.id
            return p
        })
        console.log('program after athlete change', $program)
    }

    const toggleRestDay = () => $program ? $program.days[selectedIndex].isRestDay = !$program.days[selectedIndex].isRestDay : null

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
            document.addEventListener('keyup', handleHotKeys)
        if ($userDB?.coachData?.athletes) {
            athleteOptions = $userDB.coachData.athletes.map(a => ({ name: a.name, id: a.id }))
            program.update(prev => ({ ...prev, coachId: $userDB!.coachData!.id! }))
        }
        if (selectedIndex > -1) {
            selectedDayId = $program.days[selectedIndex].id
        }
           // athleteOptions.push({ name: $userDB.username, id: $userDB.id })
        console.log(athleteOptions)
        console.log('program on mount', $program)
    })

    afterUpdate(() => {
        if ($userDB && athleteOptions.length < 1) {
            program.update(p => {
                p.coachId = $userDB?.coachData?.id!
                return p
            })
            athleteOptions = [...athleteOptions, { name: $userDB.username, id: $userDB.athleteData!.id }]
            console.log(athleteOptions)
            $userDB?.coachData?.athletes?.forEach(a => athleteOptions.push({ name: a.name, id: a.id }))
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

    onDestroy(() => {
        program.set(new Program())
    })
</script>

<div class="flex w-screen justify-center">
    <WeekNav bind:selectedDayIndex={selectedIndex} dayId={selectedDayId} />

    <div class="flex flex-col self-start w-9/12">
    <div class="flex flex-row justify-between">
        <div class="flex flex-col justify-start">
            <div class="flex justify-start">
                <input type="text"
                       placeholder="Program Name"
                       name="name"
                       bind:value={$program.name}
                       class="p-2 bg-gray-300 text-xl font-bold">
            </div>

            <div class="py-4 flex justify-start w-9/12">
                <div class="flex flex-col mr-4 w-1/2">
                    <label>Start Date</label>
                    <input type="date"
                           class="p-1 bg-gray-300 text-textgray decoration-color-textgray"
                           name="startDate"
                           bind:value={startDateString}
                           on:change={(e) => handleDateChange(dayjs(e.target.value), dayjs($program.endDate))}>
                </div>
                <div class="flex flex-col w-1/2">
                    <label>End Date</label>
                    <input type="date"
                           name="endDate"
                           class= "bg-gray-300 p-1"
                           bind:value={endDateString}
                           on:change={(e) => handleDateChange(dayjs($program.startDate), dayjs(e.target.value))}>
                </div>
            </div>
            <div class="flex flex-col self-start">
                <label></label>
                <select class="bg-gray-300 text-textgray p-1" on:change={(e) => handleChangeAthlete(e.target.value)}>
                    <option disabled selected>No Athlete Selected</option>
                    {#each athleteOptions as athlete}
                        <option value={athlete.id}>{athlete.name}</option>
                    {/each}
                </select>
                <div class="flex-row my-3">
                    <input checked={$program.isCurrent}
                           type="checkbox"
                           id="current_program"
                           on:change={(e) => $program.isCurrent = e.target.checked}>
                    <label for="current_program">Current Program</label>
                </div>
            </div>
        </div>
    </div>

    <hr>
        {#if $program && $program?.days?.length > 0 && selectedIndex > -1}
            <div class="flex justify-center align-middle">
                <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                     on:click={decrementSelectedIndex}>
                    <FaAngleLeft />
                </div>
                <h4 class="m-0 mx-2 self-center text-xl">{$program.days[selectedIndex].date.toDateString()}</h4>
                <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                     on:click={incrementSelectedIndex}>
                    <FaAngleRight />
                </div>
            </div>
            <div class="flex justify-between">
            <div>
                {#if $program?.days.length > 0}
                    <button type="button" class="bg-gray-200 text-white hover:bg-gray-300 p-2" on:click={addExercise}>
                        Add exercise (Shift +)
                    </button>
                {/if}
            </div>
            <div>
                <button type="button" class="bg-gray-200 text-white hover:bg-gray-300 p-2 mx-2" on:click={toggleRestDay}>
                    {$program.days[selectedIndex].isRestDay ? 'Undo rest day (Shift *)' : 'Make rest day (Shift *)'}
                </button>
            </div>
            </div>
        {/if}
    {#if selectedIndex > -1 && $program}
        {#if $program?.days[selectedIndex]?.isRestDay === false && $program?.days[selectedIndex]?.exercises.length > 0}
            {#each $program?.days[selectedIndex]?.exercises.sort((a, b) => a.order - b.order) as exercise, idx (idx)}
                <ExerciseForm bind:exercise={exercise} />
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
        <footer class="fixed bottom-10 right-5 md:static flex mt-4 justify-end md:w-full ">
            <button type="button"
                    on:click={(e) => handleSubmit(e, $program)}
                    class="text-gray-300 bg-yellow hover:bg-yellow-shade mx-2 p-2 rounded-md">
                Save (Ctrl s)
            </button>
            <button type="button" class="bg-blue-500 hover:text-yellow-lt mx-2 p-2 rounded-md hidden md:flex">
                Download CSV
            </button>
        </footer>
    {/if}
    </div>
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
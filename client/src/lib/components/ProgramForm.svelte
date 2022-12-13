<script lang="ts">
    import {program} from "../stores/programStore";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {Exercise, Program} from "../classes/program";
    import {afterUpdate, onDestroy, onMount} from "svelte";
    import {Day} from "../classes/day";
    import {DateInput, DatePicker} from "date-picker-svelte";
    import dayjs from "dayjs";
    import type {Dayjs} from "dayjs";
    import type {AthleteData} from "$lib/classes/user";
    import {user, userDB} from "$lib/stores/authStore";

    export let handleSubmit
    export let initialIndex = -1
    let selectedIndex = initialIndex
    $: athleteOptions = []

    const formatDateString = (date: Date) => {
        return `${date.getFullYear()}-${(date.getMonth().toString().length < 2 ? `0${date.getMonth()+1}` : (date.getMonth()+1))}-${date.getDate().toString().length < 2 ? '0' + (date.getDate()) : (date.getDate())}`
    }

    let startDateString = $program?.startDate ? formatDateString($program.startDate) : ''
    let endDateString = $program?.endDate ? formatDateString($program.endDate) : ''


    const generateCSV = () => {
        let str = "Day 1\nExercise,Weight,Sets,Reps,Notes\n";
        for(let i=0;i< $program.days.length; i++) {
            if($program.days[i].exercises.length < 1) continue
            if (i > 0) str += "Day " + (i+1) + "\n"
            const d = $program.days[i]
            for(let j = 0; j < d.exercises.length; j++) {
                str += d.exercises[j].name + "," +
                    d.exercises[j].weight + "," +
                    d.exercises[j].repsPerSet + "," +
                    d.exercises[j].sets + "," +
                    d.exercises[j].notes + "\n"
            }
        }

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
            p.days[selectedIndex].exercises.push(new Exercise())
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
        console.log(selectedIndex)
        console.log($program.days.length)
        selectedIndex === $program.days.length - 1 ? null : selectedIndex++
        console.log(selectedIndex)
    }
    const decrementSelectedIndex = () => selectedIndex === 0 ? null : selectedIndex--

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
        console.log('start', startDate)
        console.log('end', endDate)
        if ((!startDate || !endDate) || (endDate.toDate() < startDate.toDate())) {
            console.log('caught')
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
        console.log($program)
    }

    const handleChangeAthlete = (athleteId: string) => {
        console.log(athleteId)
        program.update(p => {
            p.athleteId = athleteId
            return p
        })
    }

    onMount(() => {
        if (document)
            document.addEventListener('keyup', handleHotKeys)
        if ($userDB?.coachData?.athletes) {
            athleteOptions = $userDB.coachData.athletes.map(a => ({ name: a.name, id: a.id }))
        }

           // athleteOptions.push({ name: $userDB.username, id: $userDB.id })
        console.log(athleteOptions)
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
        console.log($program)
    })

    onDestroy(() => {
        program.set(new Program())
    })
</script>

<div>
    <div class="p-4 flex flex-row justify-between">
        <div class="flex flex-col justify-start">
            <div class="flex justify-start">
                <input type="text"
                       placeholder="Program Name"
                       name="name"
                       bind:value={$program.name}
                       class="p-1">
            </div>

            <div class="py-4 flex justify-start w-9/12">
                <div class="flex flex-col mr-4">
                    <label>Start Date</label>
                    <input type="date"
                           name="startDate"
                           bind:value={startDateString}
                           on:change={(e) => handleDateChange(dayjs(e.target.value), dayjs($program.endDate))}>
                </div>
                <div class="flex flex-col">
                    <label>End Date</label>
                    <input type="date"
                           name="endDate"
                           bind:value={endDateString}
                           on:change={(e) => handleDateChange(dayjs($program.startDate), dayjs(e.target.value))}>
                </div>
            </div>
        </div>

        <div class="flex flex-col self-start">
            <label>Athlete</label>
            <select class="text-gray-shade p-2" on:change={(e) => handleChangeAthlete(e.target.value)}>
                <option disabled selected>No Athlete Selected</option>
                {#each athleteOptions as athlete}
                    <option value={athlete.id}>{athlete.name}</option>
                {/each}
            </select>
            <div class="flex-row">
                <input checked={$program.isCurrent}
                       type="checkbox"
                       id="current_program"
                       on:change={(e) => $program.isCurrent = e.target.checked}>
                <label for="current_program">Current Program</label>
            </div>

        </div>
    </div>
    <hr>
    <div class="flex justify-between">
        <div>
            {#if $program?.days.length > 0}
                <button type="button" class="bg-blue-500 text-white hover:bg-blue-600 p-2" on:click={addExercise}>
                    Add exercise (Shift +)
                </button>
            {/if}
        </div>
        {#if $program && $program.days.length > 0 && selectedIndex > -1}
            <div class="flex justify-center align-middle">
                <div class="w-8 text-white hover:text-gray-200 hover:cursor-pointer"
                     on:click={decrementSelectedIndex}>
                    <FaAngleLeft />
                </div>
                <h4 class="m-0 mx-2 self-center text-xl">{$program.days[selectedIndex].date.toDateString()}</h4>
                <div class="w-8 text-white hover:text-gray-200 hover:cursor-pointer"
                     on:click={incrementSelectedIndex}>
                    <FaAngleRight />
                </div>
            </div>
            <div>
                <button type="button" class="bg-blue-500 text-white hover:bg-blue-600 p-2 mx-2">
                    Make rest day (Shift *)
                </button>
            </div>
        {/if}
    </div>
    {#if selectedIndex > -1}
        {#each $program.days[selectedIndex].exercises as exercise, idx}
            <div class="flex flex-col p-2 justify-items-center">
                <div class="flex flex-row p-2 justify-between">
                    <input type="text"
                           name="name"
                           placeholder="Exercise name"
                           class="exercise-input"
                           bind:value={exercise.name}>
                    <input type="number"
                           name="weight"
                           placeholder="Weight"
                           class="exercise-input"
                           bind:value={exercise.weight}>
                    <input type="number"
                           name="sets"
                           placeholder="Sets"
                           class="exercise-input"
                           bind:value={exercise.sets}>
                    <input type="number"
                           name="repsPerSet"
                           placeholder="Reps"
                           class="exercise-input"
                           bind:value={exercise.repsPerSet}>
                </div>
                <textarea placeholder="Notes" name="notes" bind:value={exercise.notes} class="exercise-input"></textarea>
            </div>
        {/each}
        <footer class="flex mt-2 justify-end w-full">
            <button type="button"
                    on:click={(e) => handleSubmit(e, $program)}
                    class="bg-emerald-500 hover:bg-emerald-700 mx-2 p-2 rounded-md">
                Save (Ctrl s)
            </button>
            <button type="button" class="bg-blue-500 hover:bg-blue-700 mx-2 p-2 rounded-md">
                Download CSV
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
</style>
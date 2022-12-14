<script lang="ts">
    import {program, programSuccess} from "$lib/stores/writeProgramStore";
    import {afterUpdate, onMount} from "svelte";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import FaAngleUp from 'svelte-icons/fa/FaAngleUp.svelte'
    import FaAngleDown from 'svelte-icons/fa/FaAngleDown.svelte'
    import {Exercise} from "$lib/classes/program/exercise";

    export let selectedDayIndex: number
    export let dayId: string
    let selectedWeekIndex = -1
    let weeks: Day[][] = []
    let showCopyWeekModal: boolean = false

    const incrementSelectedWeek = () => selectedWeekIndex === weeks.length - 1 ? null : selectedWeekIndex++
    const decrementSelectedWeek = () => selectedWeekIndex === 0 ? null : selectedWeekIndex--

    const selectDay = (id: string) => {
        dayId = id
        selectedDayIndex = $program.days.findIndex(d => d.id === dayId)
        console.log(selectedDayIndex)
    }

    const initializeWeeks = () => {
        let count = 1
        let currentWeek = []
        for (const day of $program.days) {
            if (count < 8) {
                currentWeek.push(day)
            } else {
                count = 1
                weeks.push(currentWeek)
                console.log(currentWeek)
                currentWeek = [day]

            }
            count++
        }
        if (currentWeek.length > 0) {
            weeks.push(currentWeek)
        }

        if (weeks.length > 0) {
            selectedWeekIndex = weeks.length - 1
        }
    }

    const copyWeek = async (startDate: string) => {
        let date = dayjs(startDate)
        let weekToCopy = JSON.parse(JSON.stringify(weeks[selectedWeekIndex]))

        for (let i = 0; i < weekToCopy.length; i++) {
            const existingDay = $program.days.findIndex(d => dayjs(d.date).isSame(date, 'day'))

            if (existingDay === -1) {
                let day = new Day()
                day.date = date.toDate()
                day.id = date.toString()
                day.exercises = weekToCopy[i].exercises.map(e => {
                    let newExercise = new Exercise()
                    newExercise.weightCompleted = 0
                    newExercise.repsPerSet = e.repsPerSet
                    newExercise.sets = e.sets
                    newExercise.order = e.order
                    newExercise.name = e.name
                    newExercise.isComplete = false
                    newExercise.isMax = e.isMax
                    newExercise.notes = e.notes
                    newExercise.comments = []
                    newExercise.totalRepsCompleted = 0
                    newExercise.weight = e.weight
                    newExercise.weightIntensity = e.weightIntensity
                    newExercise.id = ''
                    return newExercise
                })
                program.update(prev => {
                    prev.days.push(day)
                    return prev
                })
            } else {
                program.update(prev => {
                    prev.days[existingDay].isRestDay = weekToCopy[i].isRestDay
                    prev.days[existingDay].exercises = weekToCopy[i].exercises.map(e => {
                        let newExercise = new Exercise()
                        newExercise.weightCompleted = 0
                        newExercise.repsPerSet = e.repsPerSet
                        newExercise.sets = e.sets
                        newExercise.order = e.order
                        newExercise.name = e.name
                        newExercise.isComplete = false
                        newExercise.isMax = e.isMax
                        newExercise.notes = e.notes
                        newExercise.comments = []
                        newExercise.totalRepsCompleted = 0
                        newExercise.weight = e.weight
                        newExercise.weightIntensity = e.weightIntensity
                        newExercise.id = ''
                        return newExercise
                    })
                    return prev
                })
            }
            date = date.add(1, 'day')
        }
        showCopyWeekModal = false
        initializeWeeks()
    }

    onMount(() => {
        initializeWeeks()
    })

    afterUpdate(() => {
        if ($programSuccess) {
            initializeWeeks()
        }
    })

</script>

<div class="hidden md:flex flex-col mx-2">
    {#if selectedWeekIndex > -1 && weeks.length > 0}
        <div class="flex flex-row w-full justify-center items-center">
            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={decrementSelectedWeek}>
                <FaAngleUp />
            </div>
            <div class="mx-1 font-bold">
                {`${dayjs(weeks[selectedWeekIndex][0].date).format('ddd MMM D')} - ${dayjs(weeks[selectedWeekIndex][weeks[selectedWeekIndex].length-1].date).format('ddd MMM D')}`}
            </div>
            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={incrementSelectedWeek}>
                <FaAngleDown />
            </div>
        </div>
        {#each weeks[selectedWeekIndex] as day}
            <div class={`flex flex-col bg-white rounded text-black my-2 p-2 ${day.id === dayId ? 'border-link border-4' : 'border-black'} hover:bg-textgray hover:cursor-pointer`}
                 on:click={() => selectDay(day.id)}
            >
                <h3>{dayjs(day.date).format('dddd MMMM D')}</h3>
                <hr>
                {#each day.exercises.sort((a, b) => a.order - b.order) as exercise}
                    {#if !exercise.isMax}
                        <p class={`m-0 ${exercise.isComplete && 'underline decoration-green'}`}>
                            {`${exercise.name}: ${exercise.weight} - ${exercise.sets}x${exercise.repsPerSet}`}
                        </p>
                    {:else}
                        <p class={`m-0 ${exercise.isComplete && 'underline decoration-green'}`}>
                            {`${exercise.name}: ${exercise.repsPerSet}RM ${exercise.weightCompleted && '- ' + exercise.weightCompleted + 'kg'}`}
                        </p>
                    {/if}
                {/each}
                {#if day.exercises.length < 1 && !day.isRestDay}
                    <p class="m-0 text-red-shade font-bold self-center">Incomplete</p>
                {/if}
                {#if day.isRestDay}
                    <p class="m-0 font-bold self-center">Rest Day</p>
                {/if}
            </div>
        {/each}
        <div class="flex justify-center">
            <button on:click={() => showCopyWeekModal = !showCopyWeekModal} class="bg-yellow rounded p-2 text-black mt-2">
                Copy Week
            </button>
        </div>
    {/if}
</div>
{#if showCopyWeekModal}
    <div class="w-screen min-h-full bg-black bg-opacity-50 z-30 absolute bottom-0 right-0 left-0 top-0 flex justify-center items-center">
        <div class="z-40 bg-gray-200 rounded m-auto w-7/12 p-4">
            Copy Week
            <p>Select the start date to copy to</p>
            <input type="date" on:change={(e) => copyWeek(e.target.value)}>
        </div>
    </div>
{/if}

<style>
    /*div {*/
    /*    color: rgb(245, 209, 132)*/
    /*}*/
</style>
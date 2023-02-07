<script lang="ts">
    import {program, programSuccess} from "$lib/stores/writeProgramStore";
    import {afterUpdate, onMount} from "svelte";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import FaAngleUp from 'svelte-icons/fa/FaAngleUp.svelte'
    import FaAngleDown from 'svelte-icons/fa/FaAngleDown.svelte'
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";

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
                day.date = date
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
                    prev.endDate = date.toDate()
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

    const toggleCopyWeekModal = () => {
        if (showCopyWeekModal) {
            showCopyWeekModal = false
            document.body.style.overflowY = 'auto'
        } else {
            showCopyWeekModal = true
            window.scroll(0,0)
            document.body.style.overflowY = 'hidden'
        }
    }

    onMount(() => {
        initializeWeeks()
    })

    afterUpdate(() => {
        if ($programSuccess) {
            console.log('Initializing weeks')
            initializeWeeks()
        }
    })

</script>

<div class="hidden md:flex flex-col mx-2 h-screen overflow-scroll mb-8">
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
            <div class={`flex flex-col bg-gray-200 rounded text-textgray my-2 p-2 ${day.id === dayId ? 'border-2 border-textblue' : 'border-black'} hover:bg-gray-200 hover:cursor-pointer`}
                 on:click={() => selectDay(day.id)}
            >
                <h3 class="font-medium">{dayjs(day.date).format('dddd MMMM D')}</h3>
                <hr>
                {#each day.exercises as exercise}
                    {#if exercise.type === ExerciseType.EXERCISE}
                        {#if !exercise.isMax}
                            <p class={` ${(exercise.isComplete && exercise.weightCompleted > 0) && 'text-green'} ${exercise.isComplete && exercise.weightCompleted < 1 && 'text-red'}`}>
                                {`${exercise.name}:`}
                            </p>
                            <p class="mb-1">
                                {exercise.isComplete ? `${exercise.weightCompleted}kg - ${exercise.setsCompleted}x${exercise.setsCompleted > 0 ? (exercise.totalRepsCompleted/exercise.setsCompleted) : 0}` :
                                    `${exercise.weight}kg - ${exercise.sets}x${exercise.repsPerSet}`}
                            </p>
                        {:else}
                            <p class={`${exercise.isComplete && 'text-green'}`}>
                                {`${exercise.name}:`}
                            </p>
                            <p class="mb-1">
                                {exercise.repsPerSet}RM {exercise.weightCompleted && '- ' + exercise.weightCompleted + 'kg'}
                            </p>
                        {/if}
                    {:else}
                        {#if !exercise.isMax}
                            <p class={` ${(exercise.isComplete && exercise.weightCompleted > 0) && 'text-green'} ${exercise.isComplete && exercise.weightCompleted < 1 && 'text-red'}`}>
                                {`${exercise.nameArr.join(' + ')}:`}
                            </p>
                            <p class="mb-1">
                                {exercise.weight}kg - {exercise.sets}x{exercise.repArr.join(' + ')}
                            </p>
                        {:else}
                            <p class={`m-0 ${(exercise.isComplete && exercise.weightCompleted > 0) && 'text-green'} ${exercise.isComplete && exercise.weightCompleted < 1 && 'text-red'}`}>
                                {`${exercise.nameArr.join(' + ')}:`}
                            </p>
                            <p class="mb-1">
                                {exercise.repArr.join(' + ')} RM {exercise.weightCompleted ? '- ' + exercise.weightCompleted + 'kg' : ''}
                            </p>
                        {/if}
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
        <div class="flex justify-center mb-4">
            <button on:click={toggleCopyWeekModal} class="bg-yellow rounded p-2 text-black mt-2">
                Copy Week
            </button>
        </div>
    {/if}
</div>
{#if showCopyWeekModal}
    <div class="w-screen h-screen bg-black bg-opacity-50 z-30 absolute overflow-hidden bottom-0 right-0 left-0 top-0 flex justify-center items-center">
        <div class="z-40 bg-gray-200 rounded m-auto w-7/12 p-4">
            Copy Week
            <p>Select the start date to copy to</p>
            <input type="date" on:change={(e) => copyWeek(e.target.value)}>
            <button on:click={toggleCopyWeekModal}>
                Cancel
            </button>
        </div>
    </div>
{/if}

<style>
    /*div {*/
    /*    color: rgb(245, 209, 132)*/
    /*}*/
</style>
<script lang="ts">
    import {program} from "$lib/stores/writeProgramStore";
    import {onMount} from "svelte";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import FaAngleUp from 'svelte-icons/fa/FaAngleUp.svelte'
    import FaAngleDown from 'svelte-icons/fa/FaAngleDown.svelte'

    export let selectedDayIndex: number
    export let dayId: string
    let selectedWeekIndex = -1
    let weeks: Day[][] = []

    const incrementSelectedWeek = () => selectedWeekIndex === weeks.length - 1 ? null : selectedWeekIndex++
    const decrementSelectedWeek = () => selectedWeekIndex === 0 ? null : selectedWeekIndex--

    const selectDay = (id: string) => {
        dayId = id
        selectedDayIndex = $program.days.findIndex(d => d.id === dayId)
        console.log(selectedDayIndex)
    }



    onMount(() => {
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
                    <p class={`m-0 ${exercise.isComplete && 'underline decoration-green'}`}>{`${exercise.name} - ${exercise.weight} - ${exercise.sets}x${exercise.repsPerSet}`}</p>
                {/each}
                {#if day.exercises.length < 1 && !day.isRestDay}
                    <p class="m-0 text-red-shade font-bold self-center">Incomplete</p>
                {/if}
                {#if day.isRestDay}
                    <p class="m-0 font-bold self-center">Rest Day</p>
                {/if}
            </div>
        {/each}
    {/if}
</div>

<style>
    /*div {*/
    /*    color: rgb(245, 209, 132)*/
    /*}*/
</style>
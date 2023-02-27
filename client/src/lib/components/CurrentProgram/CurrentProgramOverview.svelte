<script lang="ts">
    import {currentProgram} from "$lib/stores/athleteProgramStore";
    import {Day} from "$lib/classes/program/day";
    import {onMount} from "svelte";
    import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
    import {currentDay} from "$lib/stores/athleteProgramStore";
    import dayjs from "dayjs";
    import {isMobile} from "$lib/stores/authStore.js";

    export let viewOverview: boolean

    let weeksOfProgram: Day[][] = []
    const daysOfWeek: string[] = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

    const initializeCalender = () => {
        if (!$currentProgram) return

        let weeks = []
        let currentWeek = Array(7).fill(undefined)
        console.log(currentWeek)
        for (const day of $currentProgram.days) {
            switch (day.date.format('dddd')) {
                case 'Sunday':
                    currentWeek[0] = day
                    break
                case 'Monday':
                    currentWeek[1] = day
                    break
                case 'Tuesday':
                    currentWeek[2] = day
                    break
                case 'Wednesday':
                    currentWeek[3] = day
                    break
                case 'Thursday':
                    currentWeek[4] = day
                    break
                case 'Friday':
                    currentWeek[5] = day
                    break
                case 'Saturday':
                    currentWeek[6] = day
                    weeks.push([...currentWeek])
                    currentWeek = Array(7).fill(undefined)
                    break
            }
        }
        if (currentWeek.length > 0) {
            weeks.push([...currentWeek])
        }
        weeksOfProgram = weeks
        console.log(weeksOfProgram)
    }

    onMount(() => {
        initializeCalender()
    })
</script>

<div>
    {#each weeksOfProgram as week, weekIdx}
        <div class="grid grid-cols-7 overflow-x-scroll calendar-row" style="">
            {#each week as day, idx}
                <div class="flex flex-col col-span-1 h-full lg:h-44 overflow-y-hidden bg-gray-200 hover:bg-gray-400 hover:cursor-pointer lg:m-2"
                     on:click={() => { viewOverview = false; $currentDay = day }}
                >
                    <div class="flex justify-between items-center bg-gray-300 text-textblue">
                        <h3 class="font-semibold text-lg bg-gray-300 text-textblue p-1
                        {day && day.date.isSame($currentProgram.startDate, 'day') ? 'text-green' : ''}
                        {day && day.date.isSame($currentProgram.endDate, 'day') ? 'text-orange' : ''}"
                        >
                            {day?.date ? day.date.format('MMM ddd DD') : daysOfWeek[idx]}
                        </h3>
                        {#if !$isMobile}
                            <div class="h-4 bg-gray-300 flex items-center">
                                <FaRegCommentAlt /> <p class="my-0 mx-1">({day ? day.exercises.reduce((a, b) => a + b.comments.length, 0) : 0})</p>
                            </div>
                        {/if}
                    </div>
                    {#if day !== undefined}
                        <div class="p-1 overflow-y-auto" >
                            {#if !day.isRestDay && day.exercises.length > 0}
                                {#each day.exercises as exercise}
                                    <div class="flex">
                                        <input class="mx-2" type="checkbox" disabled checked={exercise.isComplete}>
                                        <div class="{exercise.isComplete && (exercise.totalRepsCompleted > 0 || exercise.repCompletedArr.length > 0 || exercise.secondsPerSetCompleted > 0) ?
                                        'text-green' :
                                        (exercise.isComplete ? 'text-red' : '')}"
                                        >
                                            {exercise.nameArr.length > 0 ? exercise.nameArr.join(' + ') : exercise.name}: {exercise.weight}kg
                                        </div>
                                    </div>
                                {/each}
                            {:else if day.isRestDay}
                                <div class="text-center font-medium text-lg">
                                    Rest Day
                                </div>
                            {:else}
                                <div>No Entry</div>
                            {/if}
                        </div>
                    {:else}
                        <div class="bg-gray-100 h-full"></div>
                    {/if}
                </div>
            {/each}
        </div>
    {/each}
</div>

<style>
    .calendar-row {
        grid-template-columns: repeat(7, calc(18% - 40px));
        gap: 10px
    }

    @media screen and (max-width: 500px) {
        .calendar-row {
            grid-template-columns: repeat(7, calc(50% - 40px));
            gap: 5px;
            margin-bottom: 50px;
            height: 15em;
        }
    }
</style>
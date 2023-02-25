<script lang="ts">
    import {
        dayClipboard,
        exerciseClipboard,
        program,
        programError,
        programSuccess
    } from "$lib/stores/writeProgramStore.js";
import {afterUpdate, onMount} from "svelte";
import dayjs from "dayjs";
import {Day} from "$lib/classes/program/day";

export let showOverview: boolean
export let initialIndex: number

const daysOfWeek: string[] = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
let weeksOfProgram: Day | undefined[][] = []

let clicked: boolean = false

let contextMenuIndexes: {week: number, day: number} = {week: -1, day: -1}
let points: {x: number, y: number} = {x: 0, y: 0}

const handleContextMenu = (e: Event, week: number, day: number) => {
    e.preventDefault()
    if (contextMenuIndexes.week === week && contextMenuIndexes.day === day) {
        closeContextMenu()
        return
    }
    contextMenuIndexes = {week, day}
    points = {x: e.clientX, y: e.clientY-75}
    clicked = true
}

const closeContextMenu = () => {
    setTimeout(() => {
        contextMenuIndexes = {week: -1, day: -1}
        points = {x: 0, y: 0}
        clicked = false
    }, 50)
}

const copyDay = () => {
    const day = weeksOfProgram[contextMenuIndexes.week][contextMenuIndexes.day]
    // Only doing 1 day at a time for now
    day.exercises = day.exercises.map(e => ({ ...e, id: '' }))
    $dayClipboard = [day]
}

const pasteDay = () => {
    console.log($program.days)
    const dayToReplace = weeksOfProgram[contextMenuIndexes.week][contextMenuIndexes.day]
    console.log(dayToReplace)
    $program.days = $program.days.map(d => d.id === dayToReplace?.id ? { ...$dayClipboard[0], date: dayjs(dayToReplace.date) } : d)
    initializeCalender()
}

const initializeCalender = () => {
    let weeks = []
    let currentWeek = Array(7).fill(undefined)
    console.log(currentWeek)
    for (const day of $program.days) {
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
    console.log('mounted')

    document.addEventListener('click', () => {
        if (clicked) {
            closeContextMenu()
        }
    })
})

afterUpdate(() => {
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

<div class="overflow-x-hidden">
    <div>
        <h1 class="font-bold text-2xl my-2 px-2">{$program.name}</h1>
    </div>

        {#each weeksOfProgram as week, weekIdx}
            <div class="grid grid-cols-7 overflow-x-scroll calendar-row" style="">
            {#each week as day, idx}
                <div on:contextmenu={(e) => handleContextMenu(e, weekIdx, idx)}
                     class="flex flex-col col-span-1 h-44 overflow-y-hidden bg-gray-200 hover:bg-gray-400 hover:cursor-pointer lg:m-2"
                     on:click={() => {
                         initialIndex = $program.days.findIndex(d => d.id === weeksOfProgram[weekIdx][idx].id)
                         showOverview = false
                     }}
                >
                    <div>
                        <h3 class="font-semibold text-lg bg-gray-300 text-textblue p-1
                        {day && day.date.isSame($program.startDate, 'day') ? 'text-green' : ''}
                        {day && day.date.isSame($program.endDate, 'day') ? 'text-orange' : ''}"
                        >
                            {day?.date ? day.date.format('MMM ddd DD') : daysOfWeek[idx]}
                        </h3>

                    </div>

                    {#if day !== undefined}
                        <div class="p-1">
                        {#if !day.isRestDay && day.exercises.length > 0}
                            {#each day.exercises as exercise}
                                <div>{exercise.name}</div>
                            {/each}
                        {:else if day.isRestDay}
                            <div>Rest Day</div>
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
        {#if contextMenuIndexes.week > -1 && contextMenuIndexes.day > -1}
            <div class="bg-gray-200 w-32" style="position: absolute; top: {points.y}px; left: {points.x}px" on:click={closeContextMenu}>
                <ul>
                    <li on:click={copyDay} class="hover:bg-gray-400 hover:cursor-pointer p-2">Copy Day</li>
                    <li on:click={pasteDay} class="hover:bg-gray-400 hover:cursor-pointer p-2">Paste Day</li>
                </ul>
            </div>
        {/if}


</div>


<style>
    .calendar-row {
        grid-template-columns: repeat(7, calc(16% - 40px));
        gap: 10px
    }
    
    @media screen and (max-width: 600px) {
        .calendar-row {
            grid-template-columns: repeat(7, calc(50% - 40px));
            gap: 5px;
            margin-bottom: 50px;
        }
    }
</style>

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
import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
import {isMobile} from "$lib/stores/authStore.js";
import {Exercise} from "$lib/classes/program/exercise";


export let showOverview: boolean
export let initialIndex: number

const daysOfWeek: string[] = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
let weeksOfProgram: Day[][] | undefined[][] = []

let clicked: boolean = false

let contextMenuIndexes: {week: number, day: number} = {week: -1, day: -1}
let points: {x: number, y: number} = {x: 0, y: 0}

let noProgramMenuIndexes: {week: number, day: number} = {week: -1, day: -1}
let noProgramPoints: {x: number, y: number} = {x: 0, y: 0}

const handleContextMenu = (e: MouseEvent, week: number, day: number) => {
    e.preventDefault()
    if (contextMenuIndexes.week === week && contextMenuIndexes.day === day) {
        return
    }
    if (noProgramMenuIndexes.week > -1 || noProgramMenuIndexes.day > -1) {
        closeContextMenu()
    }
    contextMenuIndexes = {week, day}
    points = {x: e.pageX, y: e.pageY-75}
    clicked = true
}

const handleNoProgramContextMenu = (e: MouseEvent, week: number, day: number) => {
    e.preventDefault()
    if (noProgramMenuIndexes.week === week && noProgramMenuIndexes.day === day) {
        return
    }
    if (contextMenuIndexes.week > -1 || contextMenuIndexes.day > -1) {
        closeContextMenu()
    }
    noProgramMenuIndexes = {week, day}
    noProgramPoints = {x: e.pageX, y: e.pageY-75}
    clicked = true
}

const closeContextMenu = () => {
    //setTimeout(() => {
        contextMenuIndexes = {week: -1, day: -1}
        noProgramMenuIndexes = {week: -1, day: -1}
        noProgramPoints = {x: 0, y: 0}
        points = {x: 0, y: 0}
        clicked = false
    //}, 50)
}

const copyDay = () => {
    const day = JSON.parse(JSON.stringify(weeksOfProgram[contextMenuIndexes.week][contextMenuIndexes.day]))
    // Only doing 1 day at a time for now
    day.exercises = day.exercises.map(e => ({
        ...e,
        id: '',
        isComplete: false,
        weightCompleted: 0,
        totalRepsCompleted: 0,
        secondsPerSetCompleted: 0,
        repCompletedArr: [],
        comments: []
    }))
    $dayClipboard = [day]
}

const extendProgram = () => {
    const lastDay = $program.days.find(d => d.date.isSame($program.endDate, 'day'))
    // for (let i = noProgramMenuIndexes.week; i >= 0; i--) {
    //     const week = weeksOfProgram[i]
    //     for (let j = week.length - 1; j >= 0; j--) {
    //         let day = week[j]
    //         if (day !== undefined && day.id === lastDay.id) {
    //             return
    //         }
    //         if (i === 0 && j === 0) {
    //             day = $dayClipboard[0]
    //         } else {
    //             day = new Exercise()
    //         }
    //     }
    // }
}

const pasteDay = () => {
    const dayToReplace = weeksOfProgram[contextMenuIndexes.week][contextMenuIndexes.day]
    $program.days = $program.days.map(d => d.id === dayToReplace?.id ? { ...$dayClipboard[0], id: d.id ? d.id : '', date: dayjs(dayToReplace.date) } : d)
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

<div class="overflow-x-hidden mb-12">
    <div>
        <h1 class="font-bold text-2xl my-2 px-2">{$program.name}</h1>
    </div>

        {#each weeksOfProgram as week, weekIdx}
            <div class="grid overflow-x-scroll calendar-row" style="">
            {#each week as day, idx}
                <div class="flex flex-col col-span-1 h-full lg:h-44 overflow-y-hidden bg-gray-200 hover:bg-gray-400 hover:cursor-pointer lg:m-2"

                >
                    <div class="flex justify-between items-center bg-gray-300 text-textblue">
                        <h3 class="font-semibold text-lg bg-gray-300 text-textblue p-1
                        {day && day.date.isSame($program.startDate, 'day') ? 'text-green' : ''}
                        {day && day.date.isSame($program.endDate, 'day') ? 'text-orange' : ''}"
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
                        <div class="p-1 h-full overflow-y-auto"
                             on:click={() => {
                                 initialIndex = $program.days.findIndex(d => d.id === weeksOfProgram[weekIdx][idx].id)
                                 showOverview = false
                             }}
                             on:contextmenu={(e) => handleContextMenu(e, weekIdx, idx)}
                        >
                            {#if !day.isRestDay && day.exercises.length > 0}
                                {#each day.exercises as exercise}
                                    <div class="flex items-center">
                                        <input class="mx-2 h-4 w-4" type="checkbox" disabled checked={exercise.isComplete}>
                                        <div class="{exercise.isComplete && (exercise.totalRepsCompleted > 0 || exercise.repCompletedArr.length > 0 || exercise.secondsPerSetCompleted > 0) ?
                                            'text-green' :
                                            (exercise.isComplete ? 'text-red' : '')}"
                                        >
                                            {exercise.nameArr.length > 0 ? exercise.nameArr.join(' + ') : exercise.name}: {exercise.weight}kg
                                        </div>
                                    </div>
                                {/each}
                            {:else if day.isRestDay}
                                <div class="text-center font-medium text-lg h-full">
                                    Rest Day
                                </div>
                            {:else}
                                <div class="h-full">No Entry</div>
                            {/if}
                        </div>
                    {:else}
                        <div class="bg-gray-100 h-full w-full" on:contextmenu={(e) => handleNoProgramContextMenu(e, weekIdx, idx)}></div>
                    {/if}

                </div>
            {/each}
            </div>
        {/each}
        {#if contextMenuIndexes.week > -1 && contextMenuIndexes.day > -1}
            <div class="bg-gray-200 w-32"
                 style="position: absolute; top: {points.y}px; left: {points.x}px"
                 on:click={() => setTimeout(() => closeContextMenu(), 50)}
            >
                <ul>
                    <li on:click={copyDay} class="hover:bg-gray-400 hover:cursor-pointer p-2">Copy Day</li>
                    <li on:click={pasteDay} class="hover:bg-gray-400 hover:cursor-pointer p-2">Paste Day</li>
                </ul>
            </div>
        {/if}
    {#if noProgramMenuIndexes.week > -1 && noProgramMenuIndexes.day > -1}
        <div class="bg-gray-200 w-32"
             style="position: absolute; top: {noProgramPoints.y}px; left: {noProgramPoints.x}px"
             on:click={() => setTimeout(() => closeContextMenu(), 50)}
        >
            <ul>
                <li on:click={extendProgram} class="hover:bg-gray-400 hover:cursor-pointer p-2">Extend Program</li>
                <li class="hover:bg-gray-400 hover:cursor-pointer p-2">Extend and Paste Day</li>
            </ul>
        </div>
    {/if}


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

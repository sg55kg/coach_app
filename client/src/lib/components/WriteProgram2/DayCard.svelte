<script lang="ts">
    import {getContext, onMount} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {program} from "$lib/stores/writeProgramStore";
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte'
    import MdContentPaste from 'svelte-icons/md/MdContentPaste.svelte'

    export let day: Day = new Day()
    export let idx: number = 0
    let showContext: boolean = false
    export let contextCoordinates: { x: number, y: number }

    let isPressing: boolean = false
    let dayStatus: 'completed' | 'changed' | 'skipped' | 'unfinished' = 'unfinished'

    const {
        setSelectedDay,
        setSelectedDayIdx,
        getSelectedDay,
        getDayClipboard,
    } = getContext('program')

    const selectedDay = getSelectedDay()
    const dayClipboard = getDayClipboard()

    $: isPressing ?
        setTimeout(() => {
            if (isPressing) {
                showContext = true
            }
        }, 500) : null

    const handleBrowserContext = (e: PointerEvent) => {
        if (showContext) {
            showContext = false
            contextCoordinates = { x: -1, y: -1 }
            return
        }
        contextCoordinates = { x: e.clientX, y: e.clientY - 75 }
        showContext = true
    }
    const copyDay = () => {
        const dayCopy = JSON.parse(JSON.stringify(day)) as Day
        dayCopy.id = ''
        dayCopy.exercises.forEach((e, i) => e.id = i + '')
        $dayClipboard = [dayCopy]
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
        showContext = false
    }

    const pasteDay = () => {
        if ($dayClipboard.length < 1) {
            document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
            showContext = false

            return
        }
        program.update((prev) => {
            let id = prev.days[idx].id
            prev.days[idx] = {...$dayClipboard[0], id}
            return prev
        })
        $program = $program
        document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
        showContext = false
    }

    onMount(() => {
        document.getElementById(`day-card-${idx}`).addEventListener('touchstart', (e) => {
            if (showContext) {
                showContext = false
                document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
                return
            }
            showContext = false
            isPressing = true
            document.getElementById(`day-card-${idx}`)?.classList.add('selected-day')
        })
        document.getElementById(`day-card-${idx}`).addEventListener('touchend', () => {
            isPressing = false
            if (!showContext) {
                document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
                setSelectedDay($program.days[idx])
                setSelectedDayIdx(idx)
                console.log($selectedDay)
            }
        })

        let allComplete: boolean = true
        let unchanged: boolean = true
        for (const exercise of day.exercises) {
            switch (exercise.type) {
                case ExerciseType.EXERCISE:
                    if (exercise.isMax) {
                        allComplete = allComplete && exercise.weight > 0
                    } else if (exercise.isMaxReps) {
                        allComplete = exercise.totalRepsCompleted > 0
                    } else {
                        allComplete = allComplete &&
                            (exercise.weightCompleted > 0 || exercise.weightCompleted === exercise.weight) &&
                            (exercise.totalRepsCompleted > 0 || exercise.totalRepsCompleted === (exercise.repsPerSet * exercise.sets))
                        unchanged = unchanged &&
                            (exercise.weightCompleted === exercise.weight) &&
                            (exercise.totalRepsCompleted === (exercise.sets * exercise.repsPerSet))
                    }
                    break
                case ExerciseType.COMPLEX:
                    for (let i = 0; i < exercise.repArr.length; i++) {
                        allComplete = allComplete &&
                            (exercise.repArr[i] > 0 || exercise.repArr[i] === exercise.repCompletedArr[i])
                        unchanged = unchanged && exercise.weight === exercise.weightCompleted &&
                            exercise.repArr[i] === exercise.repCompletedArr[i]
                    }
                    break
                case ExerciseType.DURATION:
                    break
                case ExerciseType.ACCESSORY:
                    break
                case ExerciseType.CARRY:
                    break
            }
            if (unchanged && allComplete) {
                dayStatus = 'completed'
            } else if (!unchanged) {
                dayStatus = 'changed'
            }

        }

    })

</script>

<svelte:window on:click={() => {
    if (!$isMobile && showContext) {
        showContext = false
        contextCoordinates = { x: -1, y: -1 }
    }
}} />

<div on:dragstart|preventDefault
     on:contextmenu|preventDefault={handleBrowserContext}
     on:click={() => {
         if(!$isMobile) {
             setSelectedDay(day)
             setSelectedDayIdx(idx)
         }
     }}
     id="day-card-{idx}"
     class="bg-gray-200 border-l-2 border-green aspect-square hover:cursor-pointer hover:scale-105 overflow-y-auto"
>
    <div class="w-full flex justify-between py-1 px-2">
        <h3 class="font-semibold text-lg">{idx+1}</h3>
    </div>
    <div class="px-2 py-1">
        <ul class="text-sm">
            {#if $program.days[idx]}
                {#each $program.days[idx]?.exercises as exercise, idx (exercise?.id+idx)}
                    {#if exercise.type === ExerciseType.EXERCISE}
                        <h4>
                            {exercise.name ? exercise.name : 'No Name'}: {exercise.weight}kg {exercise.sets}sets {exercise.repsPerSet}reps
                        </h4>
                    {:else if exercise.type === ExerciseType.COMPLEX}
                        <h4>
                            {exercise.nameArr.join(' + ')}: {exercise.weight}kg {exercise.sets}sets {exercise.repArr.join(' + ')}reps
                        </h4>
                    {:else if exercise.type === ExerciseType.DURATION}
                        <h4>Test</h4>
                    {:else}
                        <h4>Test</h4>
                    {/if}
                {/each}
            {/if}

        </ul>

    </div>

</div>
{#if showContext && $isMobile}
    <div on:click={(e) => {
        if (!isPressing && showContext) {
            e.preventDefault()
            document.getElementById(`day-card-${idx}`).classList.remove('selected-day')
            showContext = false
        }
    }}
         class="top-0 bottom-0 left-0 right-0 absolute z-20 bg-gray-300 opacity-50">
    </div>
    <div class="absolute bottom-0 right-0 left-0 grid grid-cols-2 bg-gray-400 z-30">
        <button class="p-4 py-6 text-lg font-semibold" on:click={copyDay}>
            Copy Day
        </button>
        <button class="p-4 py-6 text-lg font-semibold" on:click={pasteDay}>
            Paste Day
        </button>
        <button class="p-4 py-6 text-lg font-semibold">
            Insert Day Left
        </button>
        <button class="p-4 py-6 text-lg font-semibold">
            Insert Day Right
        </button>
    </div>

{/if}
{#if showContext && !$isMobile}
    <div class="p-2 flex flex-col z-40 bg-red shadow-lg w-44" on:click={() => console.log(idx)} style="position: absolute; top: {contextCoordinates.y}px; left: {contextCoordinates.x}px">
        <button class="px-4 py-2 flex items-center justify-around my-px hover:bg-gray-200" on:click={copyDay}>
            <span class="h-6 w-6 px-1 flex items-center"><FaRegCopy /></span>
            Copy Day
        </button>
        <button class="px-4 py-2 flex items-center justify-around my-px hover:bg-gray-200" on:click={pasteDay}>
            <span class="h-6 w-6 flex items-center"><MdContentPaste /></span>
            Paste Day
        </button>
    </div>
{/if}


<style>
    :global(.selected-day) {
        transform: scale(1.05);
        z-index: 30;
    }

    html, body, div, li, button, h3 {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>
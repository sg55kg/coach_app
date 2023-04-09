<script lang="ts">
    import {getContext, onMount} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import FaRegCopy from 'svelte-icons/fa/FaRegCopy.svelte'
    import MdContentPaste from 'svelte-icons/md/MdContentPaste.svelte'
    import FaLongArrowAltLeft from 'svelte-icons/fa/FaLongArrowAltLeft.svelte'
    import FaLongArrowAltRight from 'svelte-icons/fa/FaLongArrowAltRight.svelte'
    import MdClearAll from 'svelte-icons/md/MdClearAll.svelte'

    export let day: Day = new Day()
    export let idx: number = 0
    let showContext: boolean = false
    export let contextCoordinates: { x: number, y: number }

    let isPressing: boolean = false
    let dayStatus: 'completed' | 'changed' | 'skipped' | 'unfinished' = 'unfinished'
    export let container: HTMLDivElement

    const {
        setSelectedDay,
        setSelectedDayIdx,
        getSelectedDay,
        getDayClipboard,
        getProgram,
        insertDayRight,
        insertDayLeft,
        clearExercises,
        copyDay,
        pasteDay
    } = getContext('program')

    const selectedDay = getSelectedDay()
    const dayClipboard = getDayClipboard()
    const program = getProgram()

    $: isPressing ?
        setTimeout(() => {
            if (isPressing) {
                showContext = true
            }
        }, 750) : null

    const handleBrowserContext = (e: PointerEvent) => {
        if (showContext) {
            showContext = false
            contextCoordinates = { x: -1, y: -1 }
            return
        }
        let rect = container.getBoundingClientRect()
        console.log(rect)
        console.log(e.clientY + container.scrollTop + rect.top - document.body.getBoundingClientRect().top)
        contextCoordinates = { x: e.clientX, y: e.clientY + container.clientHeight + rect.top - document.body.getBoundingClientRect().top - 100 }
        showContext = true
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
            }
        })
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
     class="bg-gray-200 border-l-2 {$isMobile ? 'mx-8' : ''} border-green aspect-square hover:cursor-pointer hover:scale-105 overflow-y-auto"
>
    <div class="w-full flex justify-between py-1 px-2">
        <h3 class="font-semibold text-lg">{idx+1}</h3>
        {#if $program?.days[idx]?.date && $program?.athleteId}
            <h3>{$program.days[idx].date.format('ddd MMM DD')}</h3>
        {/if}
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
            setTimeout(() => showContext = false, 100)
        }
    }}
         class="top-0 bottom-0 left-0 right-0 fixed z-20 bg-gray-300 opacity-50">
    </div>
    <div class="fixed bottom-0 right-0 left-0 grid grid-cols-2 bg-gray-400 z-30" on:click={() => setTimeout(() => showContext = false, 100)}>
        <button class="p-4 py-6 text-lg font-semibold" on:click={() => {copyDay(idx); showContext = false}}>
            Copy Day
        </button>
        <button class="p-4 py-6 text-lg font-semibold" on:click={() => {pasteDay(idx); showContext = false}}>
            Paste Day
        </button>
        <button class="p-4 py-6 text-lg font-semibold" on:click={() => {insertDayLeft(idx); showContext = false}}>
            Insert Day Left
        </button>
        <button class="p-4 py-6 text-lg font-semibold" on:click={() => {insertDayRight(idx); showContext = false}}>
            Insert Day Right
        </button>
    </div>

{/if}
{#if showContext && !$isMobile}
    <div class="p-2 flex flex-col z-40 bg-gray-100 shadow-lg w-56 text-left"
         style="position: absolute; top: {contextCoordinates.y}px; left: {contextCoordinates.x}px"
         on:click={() => setTimeout(() => showContext = false, 100)}
    >
        <button class="px-4 py-1 flex items-center my-px hover:bg-gray-200" on:click={() => {copyDay(idx)}}>
            <span class="h-6 w-6 mr-2 px-1 flex items-center"><FaRegCopy /></span>
            Copy Day
        </button>
        <button class="px-4 py-1 flex items-center my-px hover:bg-gray-200" on:click={() => {pasteDay(idx)}}>
            <span class="h-5 w-6 mr-2 flex items-center"><MdContentPaste /></span>
            Paste Day
        </button>
        <button class="px-4 py-1 flex items-center my-px hover:bg-gray-200" on:click={() => {insertDayLeft(idx)}}>
            <span class="h-6 w-6 mr-2 flex items-center"><FaLongArrowAltLeft /></span>
            Insert Day Left
        </button>
        <button class="px-4 py-1 flex items-center my-px hover:bg-gray-200" on:click={() => {insertDayRight(idx)}}>
            <span class="h-6 w-6 mr-2 flex items-center"><FaLongArrowAltRight /></span>
            Insert Day Right
        </button>
        <button class="px-4 py-1 flex items-center my-px hover:bg-gray-200" on:click={() => {clearExercises(idx)}}>
            <span class="h-6 w-6 mr-2 flex items-center"><MdClearAll /></span>
            Clear Exercises
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
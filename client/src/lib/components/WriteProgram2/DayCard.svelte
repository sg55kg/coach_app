<script lang="ts">
    import {getContext, onMount} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import dayjs from "dayjs";
    import {program} from "$lib/stores/writeProgramStore";

    export let day: Day = new Day()
    export let index: number

    let isPressing: boolean = false
    let showContext: boolean = false
    let points: { x: number, y: number } = { x: -1, y: -1 }

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
        console.log('fired')
        points = { x: e.clientX, y: e.clientY }
        showContext = true
    }

    const copyDay = () => {
        const dayCopy = JSON.parse(JSON.stringify(day)) as Day
        dayCopy.id = ''
        $dayClipboard = [dayCopy]
        showContext = false
    }

    const pasteDay = () => {
        if ($dayClipboard.length < 1) return
        $program.days[index] = $dayClipboard[0]
        day = $dayClipboard[0]
        showContext = false
    }

    onMount(() => {
        document.getElementById(`day-card-${index}`).addEventListener('touchstart', (e) => {
            showContext = false
            isPressing = true
            document.getElementById(`day-card-${index}`)?.classList.add('selected-day')
        })
        document.getElementById(`day-card-${index}`).addEventListener('touchend', () => {
            isPressing = false
            if (!showContext) {
                document.getElementById(`day-card-${index}`).classList.remove('selected-day')
                setSelectedDay(day)
                setSelectedDayIdx(index)
                console.log($selectedDay)
            }
        })
    })

</script>

<svelte:window on:click={() => {
    if (!$isMobile && showContext) {
        showContext = false
    }
}} />

<div on:dragstart|preventDefault
     on:contextmenu|preventDefault={handleBrowserContext}
     on:click={() => {
         if(!$isMobile) {
             setSelectedDay(new Day())
             setSelectedDayIdx(index)
         }
     }}
     id="day-card-{index}"
     class="bg-gray-200 relative border-l-2 border-green aspect-square hover:cursor-pointer hover:scale-105 overflow-y-auto"
>
    <div class="w-full flex justify-between py-1 px-2">
        <h3 class="font-semibold text-lg">{index+1}</h3>
    </div>
    <div class="px-2 py-1">
        <ul class="text-sm">
            {#each day.exercises as exercise}
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
        </ul>
    </div>
    {#if showContext && !$isMobile}
        <div class="bg-gray-200 p-2 flex flex-col" style="position: absolute; top: {points.y}px; left: ${points.x}px">
            <button on:click={copyDay}>
                Copy Day
            </button>
            <button on:click={pasteDay}>
                Paste Day
            </button>
        </div>
    {/if}
</div>
{#if showContext && $isMobile}
    <div on:click={() => {
        if (!isPressing && showContext) {
            showContext = false
            document.getElementById(`day-card-${index}`).classList.remove('selected-day')
        }
    }}
         class="top-0 bottom-0 left-0 right-0 absolute z-20 bg-gray-300 opacity-50">
    </div>
    <div class="absolute bottom-0 right-0 left-0 grid grid-cols-2 bg-gray-400 z-20">
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
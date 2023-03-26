<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import DayCard from "$lib/components/WriteProgram2/DayCard.svelte";
    import ProgramSearch from "$lib/components/WriteProgram2/ProgramSearch.svelte";
    import {type Writable, writable} from "svelte/store";
    import {Program} from "$lib/classes/program";
    import {setContext} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import {Day} from "$lib/classes/program/day";
    import ExpandedDay from "$lib/components/WriteProgram2/ExpandedDay.svelte";
    import FaChevronDown from 'svelte-icons/fa/FaChevronDown.svelte'
    import {Exercise} from "$lib/classes/program/exercise";

    export let selectedProgram = new Program()

    let showActionContext: boolean = false

    const program: writable<Program> = writable(selectedProgram)
    const selectedDay: writable<Day | undefined> = writable(undefined)
    const selectedDayIdx: writable<number> = writable(-1)
    export const exerciseClipboard: Writable<Exercise[]> = writable([])
    export const dayClipboard: Writable<Day[]> = writable([])

    setContext('program', {
        getProgram: () => program,
        getSelectedDay: () => selectedDay,
        setSelectedDay: (day: Day) => $selectedDay = day,
        getSelectedDayIdx: () => selectedDayIdx,
        setSelectedDayIdx: (num: number) => $selectedDayIdx = num,
        getExerciseClipboard: () => exerciseClipboard,
        setExerciseClipboard: (arr: Exercise[]) => $exerciseClipboard = arr,
        getDayClipboard: () => dayClipboard,
        setDayClipboard: (arr: Day[]) => $dayClipboard = arr
    })
</script>

<div class="w-screen flex flex-col lg:p-3 h-[80vh] overflow-y-auto pb-32">
    <header class="flex  {$isMobile ? 'flex-row' : 'flex-col'} p-2 items-center">
        <input type="text"
               class="bg-gray-300 p-1 {$isMobile ? 'w-11/12' : 'w-full'} mx-4 my-2 font-semibold"
               placeholder="Program Name"
               bind:value={$program.name}
        >
        <ProgramSearch />
    </header>
    <div class="grid {$isMobile ? 'grid-cols-2 gap-2' : 'grid-cols-7 gap-4'} w-full px-2">
        {#each $program.days as day, idx}
            <DayCard day={day} index={idx} />
        {/each}

<!--        <div class="bg-gray-200 border-l-2 border-red aspect-square">-->
<!--            <div class="w-full flex justify-between py-1 px-2">-->
<!--                <h3 class="font-semibold text-lg">2</h3>-->
<!--            </div>-->
<!--            <div class="px-2 py-1">-->
<!--                <ul class="text-sm">-->
<!--                    <li>Wide Grip Deadlift: 3RM</li>-->
<!--                    <li>Reverse Grip Bent Over Row: 62kg</li>-->
<!--                    <li>DB Shoulder Press: 5kg</li>-->
<!--                    <li>1 Arm Farmer Carry: 50kg</li>-->
<!--                </ul>-->
<!--            </div>-->
<!--        </div>-->
<!--        <div class="bg-gray-200 border-l-2 border-red aspect-square">-->
<!--            <div class="w-full flex justify-between py-1 px-2">-->
<!--                <h3 class="font-semibold text-lg">2</h3>-->
<!--            </div>-->
<!--            <div class="px-2 py-1">-->
<!--                <ul class="text-sm">-->
<!--                    <li>Wide Grip Deadlift: 3RM</li>-->
<!--                    <li>Reverse Grip Bent Over Row: 62kg</li>-->
<!--                    <li>DB Shoulder Press: 5kg</li>-->
<!--                    <li>1 Arm Farmer Carry: 50kg</li>-->
<!--                </ul>-->
<!--            </div>-->
<!--        </div>-->
<!--        <div class="bg-gray-200 border-l-2 border-red aspect-square">-->
<!--            <div class="w-full flex justify-between py-1 px-2">-->
<!--                <h3 class="font-semibold text-lg">2</h3>-->
<!--            </div>-->
<!--            <div class="px-2 py-1">-->
<!--                <ul class="text-sm">-->
<!--                    <li>Wide Grip Deadlift: 3RM</li>-->
<!--                    <li>Reverse Grip Bent Over Row: 62kg</li>-->
<!--                    <li>DB Shoulder Press: 5kg</li>-->
<!--                    <li>1 Arm Farmer Carry: 50kg</li>-->
<!--                </ul>-->
<!--            </div>-->
<!--        </div>-->
        <div class="bg-gray-200 aspect-square flex justify-center items-center hover:scale-105 hover:cursor-pointer">
            <div class="h-7 w-7 text-yellow">
                <FaPlus />
            </div>
        </div>
    </div>
    <button class="absolute bottom-5 left-10 right-10 p-4 bg-yellow text-gray-300 rounded font-bold"
            on:click={() => showActionContext = !showActionContext}
    >
        Action Button
    </button>
</div>
{#if $selectedDay}
    <ExpandedDay />
{/if}
{#if showActionContext}
    <div class="absolute top-0 bottom-0 right-0 left-0 z-0" on:click={() => showActionContext = false}></div>
    <div class="absolute transition-all transform ease-in-out bottom-0 bg-gray-400 flex flex-col w-screen right-0 left-0 text-center z-10
        {showActionContext ? ' -translate-y-0' : ' translate-y-0'}">
        <button class="h-6 w-full pt-2" on:click={() => showActionContext = false}>
            <FaChevronDown />
        </button>
        <button class="w-full p-4 text-lg">
            Create Program
        </button>
        <button class="w-full p-4 text-lg">
            Export to CSV
        </button>
        <button class="w-full p-4 text-lg">
            Save
        </button>
    </div>
{/if}


<style>
    li, button, h3 {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>
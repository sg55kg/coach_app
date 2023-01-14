<script lang="ts">
    import dayjs, {Dayjs} from "dayjs";
    import {onDestroy, onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {Day} from "$lib/classes/program/day";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import IncompleteExercise from "$lib/components/CurrentProgram/IncompleteExercise.svelte";
    import {currentDay, currentProgram, incompleteExercises, completedExercises} from "$lib/stores/athleteProgramStore";

    export let currentProgramId: string
    const today: Dayjs = dayjs()

    const setCurrentDay = (day: Dayjs) => {
        if (!$currentProgram) return

        let programDay: Day | undefined = $currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))

        if (programDay) {
            const arr = [...programDay.exercises]
            incompleteExercises.set(arr.sort((a, b) => a.order - b.order))
            return programDay
        } else if ($currentProgram.days.length > 0) {
            programDay = $currentProgram.days[$currentProgram.days.length - 1]
            incompleteExercises.set([...$currentProgram.days[$currentProgram.days.length - 1].exercises].sort((a, b) => a.order - b.order))
            return programDay
        } else {
            return null
        }
    }

    const decrementDay = () => {
        if ($currentDay === null || $currentProgram === null) return
        let day = dayjs($currentDay.date)
        day = day.subtract(1, 'day')

        let programDay: Day | undefined = $currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))
        if (programDay !== undefined) {
            incompleteExercises.set([...programDay.exercises].sort((a, b) => a.order - b.order))
            currentDay.set(programDay)
        }
    }

    const incrementDay = () => {
        if ($currentDay === null || $currentProgram === null) return
        let day = dayjs($currentDay.date)
        day = day.add(1, 'day')

        const programDay: Day | undefined = $currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))
        if (programDay !== undefined) {
            incompleteExercises.set([...programDay.exercises])
            currentDay.set(programDay ? programDay : new Day())
        }
    }


    onMount(async () => {
        if (!$auth0Client) return
        const p = await ProgramService.getProgram($auth0Client, currentProgramId)
        currentProgram.set(p)
        currentDay.set(setCurrentDay(today))
    })

    onDestroy(() => {
        currentProgram.set(null)
        currentDay.set(null)
        incompleteExercises.set([])
        completedExercises.set([])
    })

</script>

{#if $currentProgram}
    <div>
        <h1 class="text-lg font-semibold tracking-wide text-center mx-2">{$currentProgram.name}</h1>
        <div class="flex flex-row items-center place-content-center text-lg font-semibold tracking-wide">
            <div class="w-6 mx-4 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={decrementDay}>
                <FaAngleLeft />
            </div>
            {#if $currentDay}
                <h2 class={today.isSame(dayjs($currentDay.date), 'days') && 'text-lg font-semibold tracking-wide text-green'}>
                    {dayjs($currentDay.date).format('dddd MMMM D, YYYY')}
                </h2>
            {:else}
                <h2 class="">{today.format('dddd MMMM D, YYYY')}</h2>
            {/if}

            <div class="w-6 mx-4 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={incrementDay}>
                <FaAngleRight />
            </div>
        </div>

        {#if $currentDay && !$currentDay.isRestDay && $currentDay.exercises.length > 0}
            {#each $incompleteExercises.sort((a, b) => a.order - b.order) as exercise}
                <IncompleteExercise bind:exercise={exercise} />
            {/each}
            <div class="mt-4 flex justify-center">
<!--                <button class="bg-yellow text-black font-bold p-2 mx-2 rounded hover:bg-yellow-shade">-->
<!--                    Mark Day Complete As Written-->
<!--                </button>-->
            </div>
        {:else if $currentDay.isRestDay}
            <div class="m-4">
                Rest Day
            </div>
        {:else}
            <div class="m-4">
                Your coach has not entered any information for this day yet
            </div>
        {/if}
    </div>
{/if}


<style>

</style>
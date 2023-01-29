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
    import {program} from "$lib/stores/writeProgramStore";
    import {Program} from "$lib/classes/program";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import AthleteComplexExercise from "$lib/components/CurrentProgram/AthleteComplexExercise.svelte";


    const today: Dayjs = dayjs()

    $: totalExercises = $currentDay?.exercises ? $currentDay.exercises.length : 0
    $: finishedExercises = $currentDay?.exercises ? $currentDay.exercises.filter(e => e.isComplete).length : 0
    $: daySkipped = $currentDay?.exercises ? $currentDay.exercises.filter(e => !e.isComplete || e.weightCompleted > 0).length < 1 : false

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

    const skipDay = async () => {
        try {
            const currentDayIndex = $currentProgram!.days.findIndex(d => d.id === $currentDay!.id)
            let programCopy: Program = JSON.parse(JSON.stringify($currentProgram))
            let dayCopy: Day = JSON.parse(JSON.stringify($currentDay))
            dayCopy.exercises = dayCopy.exercises.map(e => {
                return {
                    ...e, weightCompleted: 0, setsComplete: 0, totalRepsComplete: 0, isComplete: true
                }
            })
            programCopy.days[currentDayIndex] = dayCopy
            const updatedProgram: Program = await ProgramService.updateProgram(programCopy)
            $currentProgram = updatedProgram
            $currentDay = $currentProgram.days[currentDayIndex]
            $incompleteExercises = [...$currentDay!.exercises]
        } catch (e) {
            console.log(e)
        }
    }

    const completeDay = async () => {
        try {
            const currentDayIndex = $currentProgram!.days.findIndex(d => d.id === $currentDay!.id)
            let programCopy: Program = JSON.parse(JSON.stringify($currentProgram))
            let dayCopy: Day = JSON.parse(JSON.stringify($currentDay))
            // NOTE: This is a big issue because if exercises are set to lbs/kg, this isn't taking that into account
            // TODO: Move lb/kg control to this level
            dayCopy.exercises = dayCopy.exercises.map(e => {
                return {
                    ...e, weightCompleted: e.weight, setsComplete: e.sets, totalRepsComplete: (e.repsPerSet * e.sets), isComplete: true
                }
            })
            programCopy.days[currentDayIndex] = dayCopy
            const updatedProgram: Program = await ProgramService.updateProgram(programCopy)
            $currentProgram = updatedProgram
            $currentDay = $currentProgram.days[currentDayIndex]
            $incompleteExercises = [...$currentDay!.exercises]
        } catch (e) {

        }
    }


    onMount(async () => {
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
        {#if $currentDay?.exercises?.length > 0}
            <div class="text-md text-center font-semibold bg-gray-200 m-4 flex flex-col p-2">
                {#if daySkipped}
                    <p class="text-orange-shade">Skipped</p>
                {:else}
                    <p>
                        {`${finishedExercises}/${totalExercises} Exercises Complete`}
                    </p>
                {/if}

                {#if finishedExercises !== totalExercises}
                    <button on:click={completeDay} class="bg-gray-200 hover:bg-gray-300 border-2 border-green text-green p-2 my-1 mx-2 lg:m-auto lg:my-2">
                        Mark Day Finished
                    </button>
                    <button on:click={skipDay} class="bg-gray-200 hover:bg-gray-300 border-2 border-red text-red p-2 my-1 mx-2 lg:m-auto lg:my-2">
                        Skip Day
                    </button>
                {/if}
            </div>
        {/if}

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

        {#if $currentDay && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
            {#each $incompleteExercises.sort((a, b) => a.order - b.order) as exercise}
                {#if exercise.type === ExerciseType.EXERCISE}
                    <IncompleteExercise bind:exercise={exercise} />
                {:else}
                    <AthleteComplexExercise bind:exercise={exercise} />
                {/if}
            {/each}
            <div class="mt-4 flex justify-center">
<!--                <button class="bg-yellow text-black font-bold p-2 mx-2 rounded hover:bg-yellow-shade">-->
<!--                    Mark Day Complete As Written-->
<!--                </button>-->
            </div>
        {:else if $currentDay?.isRestDay}
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
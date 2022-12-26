<script lang="ts">
    import {Exercise, ExerciseComment, Program} from "$lib/classes/program";
    import dayjs, {Dayjs} from "dayjs";
    import {onDestroy, onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {Day} from "$lib/classes/day";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import IncompleteExercise from "$lib/components/CurrentProgram/IncompleteExercise.svelte";
    import {currentDay, currentProgram, incompleteExercises, completedExercises} from "$lib/stores/athleteProgramStore";

    export let currentProgramId: string
    const today: Dayjs = dayjs()

    const setCurrentDay = (day: Dayjs) => {
        if ($currentProgram === null) return

        let programDay: Day | undefined = $currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))

        if (programDay instanceof Day) {
            const arr = [...programDay.exercises]
            incompleteExercises.set(arr.sort((a, b) => a.order - b.order))
            return programDay
        } else if ($currentProgram.days.length > 0) {
            programDay = $currentProgram.days[$currentProgram.days.length - 1]
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
        console.log(p)
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
        <h1>{$currentProgram.name}</h1>
        <div class="flex flex-row">
            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={decrementDay}>
                <FaAngleLeft />
            </div>
            {#if $currentDay}
                <h2 class={today.isSame(dayjs($currentDay.date), 'days') && 'text-green'}>
                    {dayjs($currentDay.date).format('dddd MMMM D, YYYY')}
                </h2>
            {:else}
                <h2>{today.format('dddd MMMM D, YYYY')}</h2>
            {/if}

            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={incrementDay}>
                <FaAngleRight />
            </div>
        </div>

        {#if $currentDay}
            {#each $incompleteExercises as exercise}
                <IncompleteExercise exercise={exercise} />
            {/each}
            <!--{#each $completedExercises as exercise}-->
            <!--    <div class="flex-col border-b-green">-->
            <!--        <div>-->
            <!--            <p class="w-fit m-0">{exercise.name}</p>-->
            <!--            <p class="w-fit m-0">{exercise.weight}</p>-->
            <!--            <p class="w-fit m-0">{exercise.sets}</p>-->
            <!--            <p class="w-fit m-0">{exercise.repsPerSet}</p>-->
            <!--        </div>-->
            <!--        <div>-->
            <!--            <button>-->
            <!--                Edit-->
            <!--            </button>-->
            <!--        </div>-->
            <!--    </div>-->
            <!--{/each}-->
        {/if}
    </div>
{/if}


<style>

</style>
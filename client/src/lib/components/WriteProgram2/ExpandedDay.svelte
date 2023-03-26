<script lang="ts">
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import ExerciseCard from "$lib/components/WriteProgram2/ExerciseCard.svelte";
    import {getContext} from "svelte";
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte'
    import FaChevronRight from 'svelte-icons/fa/FaChevronRight.svelte'
    import ExpandedExercise from "$lib/components/WriteProgram2/ExpandedExercise.svelte";
    import {Exercise} from "$lib/classes/program/exercise";
    import {userDB} from "$lib/stores/authStore";

    const { getProgram, getSelectedDay, getSelectedDayIdx, setSelectedDay } = getContext('program')
    const program = getProgram()
    const selectedDay = getSelectedDay()
    const index = getSelectedDayIdx()

    let expandedExerciseId: string = ' ' //change to exercise id
    let athlete = $userDB?.coachData ? $userDB.coachData.athletes.find(a => a.id === $program.athleteId) : null

    const decrementDay = () => {
        if ($index < 1) return
        $index = $index - 1
        $selectedDay = $program.days[$index]
    }

    const incrementDay = () => {
        if ($index === $program.days.length - 1) return
        $index = $index + 1
        $selectedDay = $program.days[$index]
    }

    const addExercise = () => {
        $selectedDay.exercises = [...$selectedDay.exercises, new Exercise()]
    }
</script>

<div class="absolute top-12 left-0 right-0 bottom-0 w-screen flex">
    <div class="relative m-auto flex flex-col items-center bg-gray-300 w-11/12 h-[88vh]">
        <button class="absolute top-2 right-3 text-2xl font-bold" on:click={() => setSelectedDay(undefined)}>
            X
        </button>
        <small class="self-start text-sm p-0.5 w-fit">{$program.name}{athlete ? ' - ' + athlete.name : ''}</small>
        <div class="flex items-center justify-center p-2 text-yellow text-2xl font-bold">
            <button class="h-6 w-3 mx-8" on:click={decrementDay}>
                <FaChevronLeft />
            </button>
            <h1>Day {$index+1}</h1>
            <button class="h-6 w-3 mx-8" on:click={incrementDay}>
                <FaChevronRight />
            </button>
        </div>
        <div class="flex flex-col self-start justify-start items-start w-full pr-2">
            {#each $selectedDay.exercises as exercise, idx}
                {#if expandedExerciseId === exercise.id}
                    <ExpandedExercise bind:exercise={exercise} bind:expandedExerciseId={expandedExerciseId} />
                {:else}
                    <ExerciseCard bind:exercise={exercise} bind:expandedExerciseId={expandedExerciseId} />
                {/if}
            {/each}

        </div>
        <div class="p-4 flex flex-col items-center">
            <button class="h-6 w-6 text-yellow my-2" on:click={addExercise}>
                <FaPlus />
            </button>
            <button class="my-2 text-md p-1 border border-textyellow rounded">
                Make Rest Day
            </button>
        </div>
    </div>
</div>
<div class="absolute bg-gray-100 bottom-0 left-0 right-0 flex justify-around">
    <button class="bg-yellow text-gray-300 text-lg font-bold rounded px-4 py-2 my-2">
        Copy
    </button>
    <button class="bg-yellow text-gray-300 text-lg font-bold rounded px-4 py-2 my-2">
        Paste
    </button>
    <button class="bg-yellow text-gray-300 text-lg font-bold rounded px-4 py-2 my-2">
        Save
    </button>
</div>

<style></style>
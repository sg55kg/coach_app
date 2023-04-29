<script lang="ts">
    import {getContext} from "svelte";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'
    import {isMobile, userDB} from "$lib/stores/authStore.js";
    import AthleteExerciseCard from "$lib/components/AthleteProgram/AthleteExerciseCard.svelte";
    import AthleteExpandedExercise from "$lib/components/AthleteProgram/AthleteExpandedExercise.svelte";
    import Toggle from "$lib/components/shared/layout/Toggle.svelte";
    import UserService from "../../service/userService";
    import RichTextEditor from "$lib/components/shared/texteditor/RichTextEditor.svelte";

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getAthleteProgramLoading,
        getCurrentDayIdx,
        getAthleteId
    } = getContext('athlete-program')
    const currentProgram = getCurrentProgram()
    const currentDay = getCurrentDay()
    const loading = getAthleteProgramLoading()
    const idx = getCurrentDayIdx()
    const athleteId = getAthleteId()

    let selectedExerciseIdx: number = -1
    $: exercisesComplete = () => {
        let count = 0
        for (const e of $currentDay.exercises) {
            if (e.isComplete) {
                if (e.dropSets.length > 0 && e.dropSets.reduce((a = e.isComplete, b) => a && b.isComplete)) {
                    count++
                } else {
                    count++
                }
            }
        }
        return count
    }

    const incrementDay = () => {
        if ($idx < $currentProgram.days.length - 1) {
            $idx = $idx + 1
            $currentDay = $currentProgram.days[$idx]
        }
    }

    const decrementDay = () => {
        if ($idx > 0) {
            $idx = $idx - 1
            $currentDay = $currentProgram.days[$idx]
        }
    }

    const toggleWeightPreference = async () => {
        $loading = true
        if ($userDB.preferences.weight === 'kg') {
            $userDB.preferences.weight = 'lb'
        } else {
            $userDB.preferences.weight = 'kg'
        }
        try {
            await UserService.updateUserData($userDB)
        } catch (e) {
            console.log(e)
        } finally {
            $loading = false
        }
    }
</script>

<div class="bg-gray-300 w-full">
    <header class="flex flex-col p-2 w-full">
        <h2 class="text-3xl font-semibold text-center">{$currentProgram.name}</h2>
        <div class="flex items-center justify-center w-full">
            <div class="relative flex items-center justify-center {$isMobile ? 'w-8/12' : 'w-4/12'} p-2">
                <button class="h-10 absolute left-0 text-yellow w-6" on:click={decrementDay}>
                    <FaAngleLeft />
                </button>
                <h3 class="text-yellow {$isMobile ? 'text-md' : 'text-2xl'} font-medium">{$currentDay.date.format('dddd, MMMM DD')}</h3>
                <button class="h-10 absolute right-0 text-yellow w-6" on:click={incrementDay}>
                    <FaAngleRight />
                </button>
            </div>
        </div>

        <div class="flex justify-center">
            <p class="px-1 text-textblue">Pounds</p>
            <Toggle checked={$userDB.preferences.weight === 'kg'}
                    onChange={toggleWeightPreference}
            />
            <p class="px-1 text-textblue">Kilograms</p>
        </div>
    </header>
    {#if $currentDay.isRestDay}
        <h4 class="text-textblue text-center text-lg font-semibold">Rest Day</h4>
    {:else}
        <div class="flex flex-col w-full">
            <h4 class="p-1 text-lg">Warm Up</h4>
            {#if $currentDay.warmUp}
                <RichTextEditor editable={false} content={$currentDay.warmUp.instructions} />
            {:else}
                <div class="mr-2 bg-gray-200 border-l-2 border-l-textblue p-2">
                    No warm up entered for today
                </div>
            {/if}
        </div>
        <div class="flex flex-col w-full">
            <h4 class="p-1 text-lg">Exercises</h4>
            {#each $currentDay.exercises as exercise, idx}
                {#if selectedExerciseIdx === idx}
                    <AthleteExpandedExercise bind:exercise={exercise}
                                             bind:selectedExerciseIdx={selectedExerciseIdx}
                    />
                {:else}
                    <AthleteExerciseCard bind:exercise={exercise}
                                         exerciseIndex={idx}
                                         bind:selectedExerciseIdx={selectedExerciseIdx}
                    />
                {/if}
            {/each}
        </div>
        <div class="flex flex-col w-full">
            <h4 class="p-1 text-lg">Notes from your coach:</h4>
            <p class="text-textblue p-2"><i>No notes entered for today</i></p>
        </div>
        <div class="flex flex-col items-center">
            <h3 class="p-2 text-xl font-semibold {exercisesComplete() ? 'text-green' : ''}">
                {exercisesComplete()}/{$currentDay.exercises.length} Exercises Complete
            </h3>
            {#if exercisesComplete() !== $currentDay.exercises.length}
                <div class="flex justify-center items-center p-2 w-full">
                    <button class="mx-2 p-2 border-red-shade border-2 text-red-shade rounded-sm">
                        Skip Day
                    </button>
                    <button class="mx-2 p-2 border-yellow border-2 text-yellow-shade rounded-sm"
                            on:click={() => markDayCompleteAsWritten($athleteId, $currentDay, $currentProgram)}
                    >
                        Mark Day Finished
                    </button>
                </div>
            {/if}
        </div>
    {/if}
</div>

<style></style>
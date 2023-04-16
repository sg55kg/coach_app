<script lang="ts">
    import {Exercise} from "../../classes/program/exercise";
    import {EffortIntensity, ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {getContext} from "svelte";

    export let exercise: Exercise

    let repsPerSetCompleted: number = 0
    exercise.repsPerSetComplete = exercise.isComplete ? Math.floor(exercise.totalRepsCompleted / exercise.setsCompleted) : 0

    $: repMaxesPerSetCompleted = exercise.type === ExerciseType.EXERCISE && exercise.isMaxReps ? Array(exercise.sets).fill(0) : []

    const setActualIntensity = (str: 'Easy' | 'Moderate' | 'Difficult' | 'Max') => {
        switch (str) {
            case 'Easy':
                return exercise.actualIntesity = EffortIntensity.EASY
            case 'Moderate':
                return exercise.actualIntesity = EffortIntensity.MODERATE
            case 'Difficult':
                return exercise.actualIntesity = EffortIntensity.DIFFICULT
            case 'Max':
                return exercise.actualIntesity = EffortIntensity.MAX
            default:
                return exercise.actualIntesity = null
        }
    }
</script>

{#if exercise.type === ExerciseType.EXERCISE}
    <div class="flex justify-around items-center p-1 pb-6 w-full">
        {#if exercise.isMax}
            <div class="flex flex-col">
                <p class="text-sm">Weight (kg) for rep max</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
        {:else if exercise.isMaxReps}
            <div class="flex flex-col">
                <p class="text-sm">Weight (kg)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
            <div class="flex flex-col">
                <p class="text-sm">Sets</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.setsCompleted}>
            </div>
            <div class="flex flex-col">
                <p class="text-sm">Reps (AMRAP)</p>
                {#each repMaxesPerSetCompleted as set, i}
                    <input type="number" class="bg-gray-300 p-1 rounded" bind:value={repMaxesPerSetCompleted[i]}>
                {/each}
            </div>
        {:else}
            <div class="flex flex-col w-24">
                <p class="text-sm">Weight (kg)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
            <div class="flex flex-col w-24">
                <p class="text-sm">Sets</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.setsCompleted}>
            </div>
            <div class="flex flex-col w-24">
                <p class="text-sm">Reps</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.repsPerSetComplete}>
            </div>
        {/if}
    </div>
{:else if exercise.type === ExerciseType.COMPLEX}
    <div class="flex justify-around items-center p-1 pb-6 w-full">
        {#if exercise.isMax}
            <div class="flex flex-col w-24">
                <p class="text-sm">Weight (kg) for rep max</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
        {:else}
            <div class="flex flex-col w-24">
                <p class="text-sm">Weight (kg)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
            <div class="flex flex-col w-24">
                <p class="text-sm">Sets</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.setsCompleted}>
            </div>
            <div>
                <p class="text-sm">Reps</p>
                {#each exercise.repArr as complexRep, i}
                    <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.repCompletedArr[i]}>
                {/each}
            </div>
        {/if}
    </div>
{:else if exercise.type === ExerciseType.DURATION}
    <div>Test</div>
{:else if exercise.type === ExerciseType.ACCESSORY}
    <div class="flex justify-around items-center p-1 pb-6 w-full">
        {#if exercise.weight}
            <div class="flex flex-col w-24">
                <p class="text-sm">Weight (kg)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.weightCompleted}>
            </div>
        {:else}
            <div class="flex flex-col w-32">
                <p class="text-sm">Effort</p>
                <select class="bg-gray-300 p-1 rounded" on:change={(e) => setActualIntensity(e.target.value)}>
                    <option value="" selected={exercise.actualIntesity === null}>Select effort</option>
                    <option selected={exercise.actualIntesity === EffortIntensity.EASY}>Easy</option>
                    <option selected={exercise.actualIntesity === EffortIntensity.MODERATE}>Moderate</option>
                    <option selected={exercise.actualIntesity === EffortIntensity.DIFFICULT}>Difficult</option>
                    <option selected={exercise.actualIntesity === EffortIntensity.MAX}>Max Effort</option>
                </select>
            </div>
        {/if}

        <div class="flex flex-col w-24">
            <p class="text-sm">Sets</p>
            <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.setsCompleted}>
        </div>
        <div class="flex flex-col w-24">
            <p class="text-sm">Reps</p>
            <input type="number" class="bg-gray-300 p-1 rounded" bind:value={exercise.repsPerSetComplete}>
        </div>
    </div>
{/if}

<style></style>
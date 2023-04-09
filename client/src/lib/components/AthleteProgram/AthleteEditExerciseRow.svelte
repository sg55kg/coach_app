<script lang="ts">
    import {Exercise} from "../../classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";

    export let exercise: Exercise

    let repsPerSetCompleted: number = 0

    $: repMaxesPerSetCompleted = exercise.type === ExerciseType.EXERCISE && exercise.isMaxReps ? Array(exercise.sets) : []
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
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={repsPerSetCompleted}>
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
    <div>Test</div>
{/if}

<style></style>
<script lang="ts">
    import {athleteRecordFields} from "$lib/classes/user";
    import {Exercise} from "$lib/classes/program/exercise";
    import {onDestroy, onMount} from "svelte";

    export let exercise: Exercise
    let options: string[] = []
    let nameInput: HTMLInputElement
    let showOptions: boolean = false

    let handleAutoComplete

    onMount(() => {
        console.log('hello world')
        options = [...athleteRecordFields]
        handleAutoComplete = () => {
            options = athleteRecordFields.filter((x) => x.includes(exercise.name))
            console.log(options)
        }

        //nameInput.addEventListener('keydown', handleAutoComplete)

    })

    onDestroy(() => {
        //nameInput.removeEventListener('keydown', handleAutoComplete)
    })
</script>

<div class="flex flex-col p-2 justify-items-center border-0 pt-5 bg-gray-200 my-2">
    <div class="flex flex-col md:flex-row p-2 justify-between">
        <div class="flex flex-col m-1">
            <label class="text-sm m-0">Name</label>
            <input type="text"
                   name="name"
                   placeholder="Exercise name"
                   class="bg-gray-300 p-2"
                   on:focus={showOptions = true}
                   on:blur={showOptions = false}
                   bind:this={nameInput}
                   bind:value={exercise.name}>
            {#if showOptions}
                <div>
                    Test
                </div>
            {/if}
        </div>

        {#if !exercise.isMax}
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Weight</label>
                <input type="number"
                       name="weight"
                       placeholder="Weight"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.weight}>
            </div>

            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Sets</label>
                <input type="number"
                       name="sets"
                       placeholder="Sets"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.sets}>
            </div>

            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Reps</label>
                <input type="number"
                       name="repsPerSet"
                       placeholder="Reps"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.repsPerSet}>
            </div>

        {:else}
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">RM</label>
                <input type="number"
                       name="repsPerSet"
                       placeholder="Sets"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.repsPerSet}>
            </div>
        {/if}
        <div class="flex justify-center items-center m-2">
            <label>{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}&nbsp;</label>
            <label class="switch">
                <input type="checkbox" bind:value={exercise.isMax} on:change={(e) => exercise.isMax = e.target.checked}>
                <span class="slider round"></span>
            </label>
        </div>

    </div>
    <div class="flex flex-col m-2">
        <label class="text-sm m-0">Notes</label>
        <textarea placeholder="Add notes for your athlete" name="notes" bind:value={exercise.notes} class="bg-gray-300 p-2"></textarea>
    </div>

</div>

<style>

</style>
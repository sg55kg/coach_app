<script lang="ts">

   import {Exercise} from "../../classes/program/exercise";
   import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
   import MdClose from 'svelte-icons/md/MdClose.svelte'
   import FaPlus from 'svelte-icons/fa/FaPlus.svelte'

   export let exercise: Exercise
   export let isDropSet: boolean = false

   const setComplexType = (ex: Exercise) => {
       ex.type = ExerciseType.COMPLEX
       ex.nameArr = [exercise.name, '']
       ex.repArr = [exercise.repsPerSet, 0]
       ex.name = ''
       ex.repsPerSet = 0
       ex.dropSets.forEach((d) => setComplexType(d))
       return ex
   }

   const addToComplex = (ex: Exercise, nameIndex: number) => {
       ex.nameArr.splice(nameIndex+1, 0, '')
       ex.repArr.splice(nameIndex+1, 0, 0)
       ex.dropSets.forEach((d) => addToComplex(d, nameIndex))
       return ex
   }

   const removeComplexPart = (ex: Exercise, compIndex: number) => {
       ex.nameArr.splice(compIndex, 1)
       ex.repArr.splice(compIndex, 1)
       if (ex.nameArr.length === 1) {
           ex.type = ExerciseType.EXERCISE
           ex.name = ex.nameArr[0]
           ex.repsPerSet = ex.repArr[0]
           ex.nameArr = []
           ex.repArr = []
       }
       ex.dropSets.forEach((e) => removeComplexPart(e, compIndex))
       return ex
   }

   const toggleMaxWeight = (isMax: boolean) => {
       exercise.isMax = isMax
   }

   const toggleMaxReps = (isMaxReps: boolean) => {
       exercise.isMaxReps = isMaxReps
   }



</script>

{#if exercise.type === ExerciseType.EXERCISE}
    <div class="grid grid-cols-6 p-2 w-full gap-2">
        {#if !isDropSet}
            <input type="text"
                   placeholder="Exercise Name"
                   class="bg-gray-300 p-1 col-span-5"
                   bind:value={exercise.name}
            >
            <button class="col-span-1 flex items-center justify-center">
                <span class="w-5 flex justify-center" on:click={() => exercise = setComplexType(exercise)}>
                    <FaPlus />
                </span>
            </button>
        {/if}
        {#if exercise.isMax}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Rep Max</label>
                <input type="text" placeholder="Reps" bind:value={exercise.repsPerSet} class="bg-gray-300 p-1 w-full text-right">
            </div>
        {:else}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Weight (kg)</label>
                <input type="text" placeholder="" bind:value={exercise.weight} class="bg-gray-300 p-1 w-full text-right">
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Sets</label>
                <input type="text" placeholder="" bind:value={exercise.sets} class="bg-gray-300 p-1 w-full text-right">
            </div>
            {#if exercise.isMaxReps}
                <div class="col-span-2 flex items-end text-center">
                    <p class="w-full text-lg font-medium text-yellow-lt tracking-wide">AMRAP</p>
                </div>
            {:else}
                <div class="col-span-2 flex flex-col items-end">
                    <label class="text-sm">Reps</label>
                    <input type="text" placeholder="" bind:value={exercise.repsPerSet} class="bg-gray-300 p-1 w-full text-right">
                </div>
            {/if}
        {/if}
        <div class="{(!exercise.isMax && !exercise.isMaxReps) || exercise.isMaxReps ? 'col-span-2' : 'col-span-4'}"></div>
        {#if !exercise.isMax}
            <div class="col-span-2 flex flex-col items-end justify-center">
                <label class="switch">
                    <input type="checkbox" checked={exercise.isMaxReps} on:change={(e) => toggleMaxReps(e.target.checked)}>
                    <span class="slider round"></span>
                </label>
                <label class="text-sm">{exercise.isMaxReps ? 'As many reps as possible' : '# of reps'}</label>
            </div>
        {/if}
        {#if !exercise.isMaxReps}
            <div class="col-span-2 flex flex-col items-end justify-center">
                <label class="switch">
                    <input type="checkbox" checked={exercise.isMax} on:change={(e) => toggleMaxWeight(e.target.checked)}>
                    <span class="slider round"></span>
                </label>
                <label class="text-sm">{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}</label>
            </div>
        {/if}
    </div>
{:else if exercise.type === ExerciseType.COMPLEX}
    <div class="grid grid-cols-8 p-2 w-full gap-2">
        {#if !isDropSet}
            {#each exercise.nameArr as name, idx}
                <button class="col-span-1 flex items-center justify-center">
                        <span class="w-5 flex justify-center" on:click={() => exercise = removeComplexPart(exercise, idx)}>
                            <MdClose />
                        </span>
                </button>
                <input type="text"
                       placeholder="Exercise Name"
                       class="bg-gray-300 p-1 col-span-6"
                       bind:value={exercise.nameArr[idx]}
                >
                <button class="col-span-1 flex items-center justify-center">
                        <span class="w-5 flex justify-center" on:click={() => exercise = addToComplex(exercise, idx)}>
                            <FaPlus />
                        </span>
                </button>
            {/each}
        {/if}
        <div class="col-span-1"></div>
        {#if !exercise.isMax}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Weight (kg)</label>
                <input type="number" placeholder="" bind:value={exercise.weight} class="bg-gray-300 p-1 w-full">
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Sets</label>
                <input type="number" placeholder="" bind:value={exercise.sets} class="bg-gray-300 p-1 w-full">
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Reps</label>
                {#each exercise.repArr as repGroup}
                    <input type="number" placeholder="" bind:value={repGroup} class="bg-gray-300 p-1 w-full mb-2">
                {/each}
            </div>
        {:else}
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Rep Max</label>
                {#each exercise.repArr as repGroup}
                    <input type="number" placeholder="" bind:value={repGroup} class="bg-gray-300 p-1 w-full mb-2">
                {/each}
            </div>
        {/if}

        <div class="col-span-4"></div>
        <div class="col-span-3 flex flex-col items-end">
            <label class="switch">
                <input type="checkbox" checked={exercise.isMax} on:change={(e) => toggleMaxWeight(e.target.checked)}>
                <span class="slider round"></span>
            </label>
            <label class="text-sm">{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}</label>
        </div>
    </div>
{:else if exercise.type === ExerciseType.DURATION}
{:else if exercise.type === ExerciseType.ACCESSORY}
{:else}
{/if}

<style>
    .switch {
        position: relative;
        display: inline-block;
        width: 55px;
        height: 28px;
    }

    /* Hide default HTML checkbox */
    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .slider:before {
        position: absolute;
        content: "";
        height: 20px;
        width: 20px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked + .slider {
        background-color: #fde577
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #fde577
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }
</style>
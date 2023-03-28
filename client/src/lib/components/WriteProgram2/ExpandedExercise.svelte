<script lang="ts">
    import FaChevronUp from 'svelte-icons/fa/FaChevronUp.svelte'
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte'
    import {Exercise} from "$lib/classes/program/exercise";
    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {getContext, onDestroy} from "svelte";
    import MdClose from 'svelte-icons/md/MdClose.svelte'

    export let expandedExerciseId: string = ' '
    export let exercise: Exercise

    const { getProgram, getSelectedDay, getSelectedExerciseIdx } = getContext('program')
    const program = getProgram()
    const selectedDay = getSelectedDay()
    const selectedExerciseIdx = getSelectedExerciseIdx()

    const setComplexType = () => {
        exercise.type = ExerciseType.COMPLEX
        exercise.nameArr = [exercise.name, '']
        exercise.repArr = [exercise.repsPerSet, 0]
        exercise.name = ''
        exercise.repsPerSet = 0
    }

    const removeComplexPart = (compIndex: number) => {
        exercise.nameArr.splice(compIndex, 1)
        exercise.repArr.splice(compIndex, 1)
        if (exercise.nameArr.length === 1) {
            exercise.type = ExerciseType.EXERCISE
            exercise.name = exercise.nameArr[0]
            exercise.repsPerSet = exercise.repArr[0]
            exercise.nameArr = []
            exercise.repArr = []
        }
    }

    onDestroy(() => {
        // sync the program with any potential changes that might have been made to this day
        const day = $program.days.find(d => d.id === $selectedDay.id)
        let ex: Exercise = day.exercises[$selectedExerciseIdx]
        ex = exercise
        $program = $program
        $selectedExerciseIdx = -1
    })
</script>

<div class="absolute right-1 left-1 bg-gray-200 flex flex-col items-center lg:p-8">
    {#if exercise.type === ExerciseType.EXERCISE}
        <div class="grid grid-cols-6 p-2 w-full gap-2">
            <input type="text"
                   placeholder="Exercise Name"
                   class="bg-gray-300 p-1 col-span-5"
                   bind:value={exercise.name}
            >
            <button class="col-span-1 flex items-center justify-center">
            <span class="w-5 flex justify-center" on:click={setComplexType}>
                <FaPlus />
            </span>
            </button>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Weight (kg)</label>
                <input type="text" placeholder="" bind:value={exercise.weight} class="bg-gray-300 p-1 w-full text-right">
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Sets</label>
                <input type="text" placeholder="" bind:value={exercise.sets} class="bg-gray-300 p-1 w-full text-right">
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="text-sm">Reps</label>
                <input type="text" placeholder="" bind:value={exercise.repsPerSet} class="bg-gray-300 p-1 w-full text-right">
            </div>
            <div class="col-span-2"></div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="switch">
                    <input type="checkbox">
                    <span class="slider round"></span>
                </label>
                <label class="text-sm">{exercise.isMaxReps ? 'As many reps as possible' : '# of reps'}</label>
            </div>
            <div class="col-span-2 flex flex-col items-end">
                <label class="switch">
                    <input type="checkbox">
                    <span class="slider round"></span>
                </label>
                <label class="text-sm">{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}</label>
            </div>
        </div>
    {:else if exercise.type === ExerciseType.COMPLEX}
        <div class="grid grid-cols-8 p-2 w-full gap-2">
            {#each exercise.nameArr as name, idx}
                <button class="col-span-1 flex items-center justify-center">
                    <span class="w-5 flex justify-center" on:click={() => removeComplexPart(idx)}>
                        <MdClose />
                    </span>
                </button>
                <input type="text"
                       placeholder="Exercise Name"
                       class="bg-gray-300 p-1 col-span-6"
                       bind:value={exercise.nameArr[idx]}
                >
                <button class="col-span-1 flex items-center justify-center">
                    <span class="w-5 flex justify-center">
                        <FaPlus />
                    </span>
                </button>
            {/each}
            <div class="col-span-1"></div>
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
            <div class="col-span-4"></div>
            <div class="col-span-3 flex flex-col items-end">
                <label class="switch">
                    <input type="checkbox">
                    <span class="slider round"></span>
                </label>
                <label class="text-sm">Sets x Reps</label>
            </div>
        </div>
    {:else if exercise.type === ExerciseType.DURATION}
    {:else if exercise.type === ExerciseType.ACCESSORY}
    {:else}
    {/if}
    <button class="text-yellow h-7 w-7 my-2">
        <FaPlus />
    </button>
    <div class="flex flex-col items-start w-full p-2">
        <label>Exercise Notes</label>
        <textarea class="bg-gray-300 w-full p-px" bind:value={exercise.notes}></textarea>
    </div>
    <button class="text-yellow w-6 h-6 mx-4 mt-6 mb-2" on:click={() => $selectedExerciseIdx = -1}>
        <FaChevronUp />
    </button>
</div>

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
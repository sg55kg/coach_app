<script lang="ts">
    import {athleteRecordFields} from "$lib/classes/user";
    import {Exercise} from "$lib/classes/program/exercise";
    import {onDestroy, onMount} from "svelte";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
    import {program, programError} from "$lib/stores/writeProgramStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";


    export let exercise: Exercise = new Exercise()
    export let selectedDayIndex: number = 0
    export let exerciseIndex: number = 0

    let options: string[] = []
    let nameInput: HTMLInputElement
    let showOptions: boolean = false

    let handleAutoComplete = (e: KeyboardEvent) => {
        if (e.key === 'Enter') {
            selectOption(athleteRecordFields[0])
        } else if (e.key === '+') {
            // have ability to make a complex here like pull + snatch
        } else {
            options = athleteRecordFields.filter((x) => x.includes(exercise.name.toLowerCase()))
        }
    }

    const selectOption = (nameOption: string) => {
        exercise.name = nameOption
        exercise = exercise
        showOptions = false
    }

    const handleRemoveExercise = async () => {
        if (exercise.id) {
            programError.set('')
            try {
                await ProgramService.deleteExercise($auth0Client!, exercise)
            } catch (e) {
                return programError.set(e.message)
            }
        }

        for (let i = exerciseIndex + 1; i < $program.days[selectedDayIndex].exercises.length; i++) {
            $program.days[selectedDayIndex].exercises[i].order = $program.days[selectedDayIndex].exercises[i].order - 1
        }
        program.update(prev => {
            prev.days[selectedDayIndex].exercises.splice(exerciseIndex, 1)
            return prev
        })

    }

    onMount(() => {
        options = [...athleteRecordFields]
        nameInput.addEventListener('keyup', (e) => handleAutoComplete(e))
    })

    onDestroy(() => {
        nameInput.removeEventListener('keyup', (e) => handleAutoComplete(e))
    })
</script>

<div class="flex flex-col-reverse justify-center lg:items-center lg:flex-row">
<div class="lg:w-16 w-6/12 flex lg:flex-col lg:items-center w-full flex-row justify-around mb-4 lg:mb-0">
    <div class="text-red hover:cursor-pointer hover:text-red-shade lg:p-4 h-8 lg:h-20" on:click={handleRemoveExercise}>
        <FaTrashAlt />
    </div>
    <div class="lg:p-4 hover:cursor-pointer hover:text-textblue h-8 lg:h-20">
        <FaRegCommentAlt />
    </div>
</div>
<div class="flex flex-col p-2 justify-items-center border-0 pt-5 bg-gray-200 my-2 lg:flex-auto lg:w-11/12">
    <div class="flex flex-col md:flex-row p-2 justify-between">
        <div class="flex flex-col m-1">
            <label class="text-sm m-0">Name</label>
            <div class="relative">
                <input type="text"
                       name="name"
                       placeholder="Exercise name"
                       class="bg-gray-300 p-2"
                       autocomplete="off"
                       on:focus={() => showOptions = true}
                       on:blur={() => setTimeout(() => showOptions = false, 100)}
                       bind:this={nameInput}
                       bind:value={exercise.name}>
                {#if showOptions}
                    <div class="absolute right-0 left-0 z-40 max-h-44 bg-gray-300 overflow-scroll min-h-fit">
                        {#each options as option}
                            <div on:click={() => selectOption(option)} class="p-2 hover:bg-gray-200 cursor-pointer">{option}</div>
                        {/each}
                    </div>
                {/if}
            </div>
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
                <input type="checkbox" bind:checked={exercise.isMax} on:change={(e) => exercise.isMax = e.target.checked}>
                <span class="slider round"></span>
            </label>
        </div>

    </div>
    <div class="flex flex-col m-2">
        <label class="text-sm m-0">Notes</label>
        <textarea placeholder="Add notes for your athlete" name="notes" bind:value={exercise.notes} class="bg-gray-300 p-2"></textarea>
    </div>

</div>
</div>

<style>
    .switch {
        position: relative;
        display: inline-block;
        width: 60px;
        height: 34px;
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
        height: 26px;
        width: 26px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked + .slider {
        background-color: #2196F3;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #2196F3;
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
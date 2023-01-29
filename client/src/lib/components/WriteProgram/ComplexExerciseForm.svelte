<script lang="ts">
    import {Exercise} from "$lib/classes/program/exercise";
    import {athleteRecordFields} from "$lib/classes/user/athlete";
    import {program, programError} from "$lib/stores/writeProgramStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {onMount} from "svelte";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
    import {ExerciseType} from "$lib/classes/program/exercise/enums";
    import dayjs from "dayjs";

    export let exercise: Exercise
    export let inputFocused: boolean = false
    export let selectedDayIndex: number = 0
    export let exerciseIndex: number = 0

    let options: string[] = []
    let nameArrInput: HTMLInputElement[] = []
    let showComplexNameOptions: number = -1
    let showComments: boolean = false

    let handleAutoComplete = (e: KeyboardEvent) => {
        inputFocused = true
        if (e.key === 'Enter') {
            selectOption(options[0])
        } else if (e.key === '+') {
            e.preventDefault()
            const split = exercise.nameArr[exercise.nameArr.length-1].split('+')
            exercise.nameArr[exercise.nameArr.length-1] = split[0]
            exercise.nameArr.push('')
            exercise.repArr.push(0)
            exercise.name = ''
            setTimeout(() => {
                nameArrInput[exercise.nameArr.length-1].focus()
            }, 25)
        } else {
            options = athleteRecordFields.filter((x) => x.includes(exercise.nameArr[exercise.nameArr.length-1].toLowerCase()))
        }
    }

    const selectOption = (nameOption: string) => {
        if (exercise.type === ExerciseType.COMPLEX) {
            exercise.nameArr[exercise.nameArr.length-1] = nameOption
            showComplexNameOptions = -1
            options = [...athleteRecordFields]
        }
    }

    const handleRemoveExercise = async () => {
        if (exercise.id) {
            programError.set('')
            try {
                await ProgramService.deleteExercise(exercise)
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

    const handleComplexNameFocus = (idx: number) => {
        showComplexNameOptions = idx
        inputFocused = true
        options = athleteRecordFields.filter((x) => x.includes(exercise.nameArr[idx].toLowerCase()))
    }

    onMount(() => {
        options = [...athleteRecordFields]
        //nameInput.addEventListener('keyup', (e) => handleAutoComplete(e))
    })
</script>

<div class="flex flex-col-reverse justify-center lg:items-center lg:flex-row">
    <div class="lg:w-16 w-6/12 flex lg:flex-col lg:items-center w-full flex-row justify-around mb-4 lg:mb-0">
        <div class="text-red hover:cursor-pointer hover:text-red-shade lg:p-4 h-8 lg:h-20" on:click={handleRemoveExercise}>
            <FaTrashAlt />
        </div>
        <div class="lg:p-4 hover:cursor-pointer hover:text-textblue h-8 lg:h-20" on:click={() => showComments = !showComments}>
            <FaRegCommentAlt />
        </div>
    </div>
    <div class="flex flex-col lg:p-2 justify-items-center border-0 lg:pt-5 bg-gray-200 my-2 lg:flex-auto lg:w-11/12">
        <div class="flex flex-col p-2 justify-between">
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Complex Name</label>
                <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-4 gap-1 w-11/12 lg:w-7/12">
                    {#each exercise.nameArr as complexName, idx}
                        <div class="w-30 lg:w-36 flex items-center">
                            <div class="relative z-0 mx-1">
                                <input type="text"
                                       class="bg-gray-300 p-2 w-11/12"
                                       autocomplete="off"
                                       on:focus={() => handleComplexNameFocus(idx)}
                                       on:blur={() => { setTimeout(() => showComplexNameOptions = -1, 100); inputFocused = false}}
                                       on:keydown={(e) => handleAutoComplete(e)}
                                       bind:this={nameArrInput[idx]}
                                       bind:value={complexName}>
                                {#if showComplexNameOptions === idx}
                                    <div class="absolute right-0 left-0 z-10 max-h-44 bg-gray-300 overflow-scroll min-h-fit">
                                        {#each options as option}
                                            <div on:click={() => selectOption(option)} class="p-2 hover:bg-gray-200 cursor-pointer">{option}</div>
                                        {/each}
                                    </div>
                                {/if}
                            </div>
                            {#if idx < exercise.nameArr.length - 1}
                                <p class="m-0 w-1/12">+</p>
                            {/if}
                        </div>
                    {/each}
                </div>
            </div>
        </div>
        {#if !exercise.isMax}
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Weight</label>
                <input type="number"
                       name="weight"
                       on:focus={() => inputFocused = true}
                       on:blur={() => inputFocused = false}
                       placeholder="Weight"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.weight}>
            </div>

            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Sets</label>
                <input type="number"
                       name="sets"
                       on:focus={() => inputFocused = true}
                       on:blur={() => inputFocused = false}
                       placeholder="Sets"
                       class="bg-gray-300 p-2"
                       bind:value={exercise.sets}>
            </div>

            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Reps</label>
                <div class="grid grid-cols-2 lg:grid-cols-4 gap-1 w-11/12 lg:w-6/12">
                    {#each exercise.repArr as complexRep, idx}
                        <div class="w-30 lg:w-36 flex items-center text-center">
                            <input type="number"
                                   on:focus={() => inputFocused = true}
                                   on:blur={() => inputFocused = false}
                                   class="bg-gray-300 p-2 w-8/12"
                                   bind:value={exercise.repArr[idx]}>
                            {#if idx < exercise.repArr.length - 1}
                                <p class="m-0 w-4/12 text-center">+</p>
                            {/if}
                        </div>
                    {/each}
                </div>
            </div>
        {:else}
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">RM</label>
                <div class="grid grid-cols-2 lg:grid-cols-4 gap-1 w-11/12 lg:w-6/12">
                    {#each exercise.repArr as complexRep, idx}
                        <div class="w-30 lg:w-36 flex items-center text-center">
                            <input type="number"
                                   on:focus={() => inputFocused = true}
                                   on:blur={() => inputFocused = false}
                                   class="bg-gray-300 p-2 w-8/12"
                                   bind:value={exercise.repArr[idx]}>
                            {#if idx < exercise.repArr.length - 1}
                                <p class="m-0 w-4/12 text-center">+</p>
                            {/if}
                        </div>
                    {/each}
                </div>
            </div>
        {/if}
        <div class="flex justify-center items-center m-2">
            <label>{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}&nbsp;</label>
            <label class="switch">
                <input type="checkbox" bind:checked={exercise.isMax} on:change={(e) => exercise.isMax = e.target.checked}>
                <span class="slider round"></span>
            </label>
        </div>
        <div class="flex flex-col m-2">
            <label class="text-sm m-0">Notes</label>
            <textarea
                    on:focus={() => inputFocused = true}
                    on:blur={() => inputFocused = false}
                    placeholder="Add notes for your athlete"
                    name="notes"
                    bind:value={exercise.notes}
                    class="bg-gray-300 p-2"></textarea>
        </div>
        {#if exercise.isComplete && exercise.weightCompleted > 0 && exercise.totalRepsCompleted > 0}
            <div class="bg-green w-full h-1">
            </div>
        {:else if exercise.isComplete}
            <div class="bg-red w-full h-1"></div>
        {/if}
    </div>
</div>
<div class="lg:ml-16">
    Comments ({exercise.comments.length})
</div>
{#if showComments}
    {#if exercise?.comments.length < 1}
        <p class="m-0">No Comments</p>
    {/if}
    {#each exercise?.comments as comment (comment.id)}
        <div class="flex flex-col p-4 bg-gray-300 rounded-xl lg:w-6/12 text-textblue my-2 lg:ml-16">
            <div class="flex flex-row justify-between mb-2">
                <h5 class="text-lg font-bold">{comment.commenterName}</h5>
                <h5><i>{dayjs(comment.createdAt).format('MMMM D h:mm A')}</i></h5>
            </div>
            <p>{comment.content}</p>
        </div>
    {/each}
{/if}

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
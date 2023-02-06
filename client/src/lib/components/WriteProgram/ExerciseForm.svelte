<script lang="ts">
    import {Exercise} from "$lib/classes/program/exercise";
    import {onDestroy, onMount} from "svelte";
    import FaTrashAlt from 'svelte-icons/fa/FaTrashAlt.svelte'
    import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {athleteRecordFields} from "$lib/classes/user/athlete";
    import {ExerciseType} from "$lib/classes/program/exercise/enums";
    import dayjs from "dayjs";


    export let exercise: Exercise = new Exercise()
    export let selectedDayIndex: number = 0
    export let exerciseIndex: number = 0
    export let inputFocused: boolean = false

    let options: string[] = []
    let nameInput: HTMLInputElement
    let nameArrInput: HTMLInputElement[] = []
    let showOptions: boolean = false
    let showComplexNameOptions: number = -1
    let showComments: boolean = false
    let minutesPerSet: number
    let secondsPerSet: number

    let handleAutoComplete = (e: KeyboardEvent) => {
        inputFocused = true
        if (e.key === 'Enter') {
            selectOption(options[0])
        } else if (e.key === '+') {
            if (exercise.type !== ExerciseType.COMPLEX) {
                exercise.nameArr = exercise.name.split('+')
                exercise.nameArr[0] = exercise.nameArr[0].trim()
                exercise.nameArr.push('')
                exercise.repArr.push(0,0)
                exercise.name = ''
                // nested timeouts so the inputs have time to render and fill nameArrInput
                setTimeout(() => {
                    exercise.type = ExerciseType.COMPLEX
                }, 25)
            } else {
                e.preventDefault()
                const split = exercise.nameArr[exercise.nameArr.length-1].split('+')
                exercise.nameArr[exercise.nameArr.length-1] = split[0]
                exercise.nameArr.push('')
                exercise.repArr.push(0)
                exercise.name = ''
                setTimeout(() => {
                    nameArrInput[exercise.nameArr.length-1].focus()
                }, 25)
            }
        } else {
            if (exercise.type === ExerciseType.COMPLEX) {
                options = athleteRecordFields.filter((x) => x.includes(exercise.nameArr[exercise.nameArr.length-1].toLowerCase()))
            } else {
                if (!exercise.name) return
                options = athleteRecordFields.filter((x) => x.includes(exercise.name.toLowerCase()))
            }
        }
    }

    const selectOption = (nameOption: string) => {
        if (exercise.type === ExerciseType.COMPLEX) {
            exercise.nameArr[exercise.nameArr.length-1] = nameOption
            showComplexNameOptions = -1
            options = [...athleteRecordFields]
        } else {
            exercise.name = nameOption
            exercise = exercise
            showOptions = false
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
        programSuccess.set('Success')
    }

    const handleRemoveDropSet = async (dropSet, idx) => {
        if (dropSet.id) {
            programError.set('')
            try {
                await ProgramService.deleteExercise(dropSet)
            } catch (e) {
                return programError.set(e.message)
            }
        }

        exercise.dropSets = exercise.dropSets.filter((e, i) => i !== idx)
    }

    const handleNameFormFocus = () => {
        if (!exercise.name) return
        showOptions = true
        inputFocused = true
        options = athleteRecordFields.filter((x) => x.includes(exercise.name.toLowerCase()))
    }

    const validateDropSetPercent = (e: InputEvent, dropSet: Exercise) => {
        if (dropSet.dropSetPercent === null || dropSet.dropSetPercent === undefined) {
            dropSet.dropSetPercent = 0
        } else if (dropSet.dropSetPercent > 100) {
            dropSet.dropSetPercent = 100
            e.currentTarget.value = dropSet.dropSetPercent
        } else if (dropSet.dropSetPercent < 0) {
            dropSet.dropSetPercent = 0
            e.currentTarget.value = dropSet.dropSetPercent
        }
    }

    onMount(() => {
        options = [...athleteRecordFields]
        if (exercise.type === ExerciseType.DURATION && exercise.secondsPerSet > 0) {
            minutesPerSet = Math.floor(exercise.secondsPerSet / 60)
            secondsPerSet = exercise.secondsPerSet - (minutesPerSet * 60)
        }
    })

    onDestroy(() => {

    })

    const handleChangeDuration = (value: string) => {
        console.log(value)
        if (value === 'Reps') {
            exercise.type = ExerciseType.EXERCISE
        } else if (value === 'Time') {
            exercise.type = ExerciseType.DURATION
        }
        console.log(exercise.type)
    }

    const handleSetDurationTime = () => {
        if (secondsPerSet === undefined) {
            secondsPerSet = 0
        } if (minutesPerSet === undefined) {
            minutesPerSet = 0
        }
        exercise.secondsPerSet = secondsPerSet + (minutesPerSet * 60)
        console.log(exercise.secondsPerSet)
    }

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
        <div class="flex flex-col p-2 justify-between lg:grid lg:grid-cols-6">
            <div class="flex flex-col m-1">
                <label class="text-sm m-0">Name</label>
                    <div class="relative z-0">
                        <input type="text"
                               name="name"
                               placeholder="Exercise name"
                               class="bg-gray-300 p-2"
                               autocomplete="off"
                               on:focus={handleNameFormFocus}
                               on:blur={() => { setTimeout(() => showOptions = false, 100); inputFocused = false }}
                               on:keydown={(e) => handleAutoComplete(e)}
                               bind:this={nameInput}
                               bind:value={exercise.name}>
                        {#if showOptions}
                            <div class="absolute right-0 left-0 z-10 max-h-44 bg-gray-300 overflow-scroll min-h-fit">
                                {#each options as option}
                                    <div on:click={() => selectOption(option)} class="p-2 hover:bg-gray-200 cursor-pointer">{option}</div>
                                {/each}
                            </div>
                        {/if}
                    </div>
            </div>

            {#if !exercise.isMax}
                <div class="flex flex-col m-1">
                    <label class="text-sm m-0">Weight (kg)</label>
                    <input type="number"
                           name="weight"
                           on:focus={() => inputFocused = true}
                           on:blur={() => inputFocused = false}
                           placeholder="Weight"
                           class="bg-gray-300 p-2"
                           bind:value={exercise.weight}
                    >
                </div>

                <div class="flex flex-col m-1">
                    <label class="text-sm m-0">Sets</label>
                    <input type="number"
                           name="sets"
                           on:focus={() => inputFocused = true}
                           on:blur={() => inputFocused = false}
                           placeholder="Sets"
                           class="bg-gray-300 p-2"
                           bind:value={exercise.sets}
                    >
                </div>


                <div class="flex flex-col m-1">
                    <select class="text-sm bg-gray-200 mb-[.1em]" on:change={(e) => handleChangeDuration(e.target.value)}>
                        <option selected={exercise.type === ExerciseType.EXERCISE}>Reps</option>
                        <option selected={exercise.type === ExerciseType.DURATION}>Time</option>
                    </select>
<!--                    <label class="text-sm m-0">Reps</label>-->
                    {#if exercise.type === ExerciseType.EXERCISE}
                        <input type="number"
                               name="repsperset"
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               placeholder="reps"
                               class="bg-gray-300 p-2"
                               bind:value={exercise.repsPerSet}
                        >
                    {:else if exercise.type === ExerciseType.DURATION}
                        <div class="flex">
                            <input type="number"
                                   name="minutes"
                                   on:focus={() => inputFocused = true}
                                   on:blur={() => inputFocused = false}
                                   bind:value={minutesPerSet}
                                   on:input={handleSetDurationTime}
                                   placeholder="min"
                                   class="bg-gray-300 p-2 w-16">
                            <p class="mx-2 font-semibold pt-1">:</p>
                            <input type="number"
                                   name="seconds"
                                   bind:value={secondsPerSet}
                                   on:focus={() => inputFocused = true}
                                   on:blur={() => inputFocused = false}
                                   on:input={handleSetDurationTime}
                                   placeholder="sec"
                                   class="bg-gray-300 p-2 w-16">
                        </div>
                    {/if}
                </div>
                {#if exercise.type === ExerciseType.DURATION}
                    <div class="flex flex-col m-1">
                        <label class="text-sm m-0">Distance (meters)</label>
                        <input type="number"
                               name="distance"
                               bind:value={exercise.distanceMeters}
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               class="bg-gray-300 p-2">
                    </div>
                {/if}
            {:else}
                <div class="flex flex-col m-1">
                    <label class="text-sm m-0">RM</label>
                        <input type="number"
                               name="repsPerSet"
                               placeholder="Sets"
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               class="bg-gray-300 p-2"
                               bind:value={exercise.repsPerSet}
                        >
                </div>
            {/if}
            <div class="flex justify-end items-center m-2 {exercise.type === ExerciseType.EXERCISE ? 'col-span-2' : ''}">
                <label>{exercise.isMax ? 'Rep Max' : 'Sets x Reps'}&nbsp;</label>
                <label class="switch">
                    <input type="checkbox" bind:checked={exercise.isMax} on:change={(e) => exercise.isMax = e.target.checked}>
                    <span class="slider round"></span>
                </label>
            </div>
        </div>
        {#if exercise.isMax}
            {#each exercise.dropSets as dropSet, idx (idx)}
                <div class="flex flex-col lg:flex-row lg:justify-around lg:ml-48 text-sm">
                    <div class="flex flex-col m-1">
                        <label class="text-sm m-0">% of top set</label>
                        <input class="p-2 bg-gray-300"
                               bind:value={dropSet.dropSetPercent}
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               on:input={(e) => validateDropSetPercent(e, dropSet)}
                               type="number"
                        >
                    </div>
                    <div class="flex flex-col m-1">
                        <label class="text-sm m-0">Sets</label>
                        <input class="p-2 bg-gray-300"
                               bind:value={dropSet.sets}
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               type="number"
                        >
                    </div>
                    <div class="flex flex-col m-1">
                        <label class="text-sm m-0">Reps</label>
                        <input class="p-2 bg-gray-300"
                               bind:value={dropSet.repsPerSet}
                               on:focus={() => inputFocused = true}
                               on:blur={() => inputFocused = false}
                               type="number"
                        >
                    </div>
                    <div class="my-1">
                        <div class="text-red hover:cursor-pointer hover:text-red-shade lg:p-4 h-6 lg:h-16"
                             on:click={() => handleRemoveDropSet(dropSet, idx)}
                        >
                            <FaTrashAlt />
                        </div>
                    </div>
                </div>
            {/each}
            <div class="m-1 mt-2 lg:mt-4 flex justify-center">
                <button class="p-2 bg-link text-white font-medium" on:click={() => exercise.dropSets = [...exercise.dropSets, new Exercise()]}>
                    Add Drop Set
                </button>
            </div>
        {/if}
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
            <div class="bg-red w-full h-1">
            </div>
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
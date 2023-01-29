<script lang="ts">
    import {Exercise, ExerciseComment} from "$lib/classes/program/exercise";
    import {onMount} from "svelte";
    import {loadingAthleteProgram} from "$lib/stores/athleteProgramStore.js";
    import {userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import FaPlusCircle from 'svelte-icons/fa/FaPlusCircle.svelte'
    import FaMinusCircle from 'svelte-icons/fa/FaMinusCircle.svelte'
    import {handleChangeWeightUnits} from "$lib/components/CurrentProgram/util/index.js";
    import dayjs from "dayjs";

    export let exercise: Exercise
    $: exerciseName = exercise.nameArr.join(' + ')

    let newCommentContent: string = ''
    let showComments: boolean = false
    let setsComplete: number = exercise.setsComplete ? exercise.setsComplete : 0
    let repsComplete: number[] = exercise.repArr.map(_ => 0)

    const toggleShowComments = () => showComments = !showComments

    const addComment = async (ex: Exercise) => {
        loadingAthleteProgram.set(true)
        let comment: ExerciseComment = {
            content: newCommentContent,
            athleteId: $userDB!.athleteData.id,
            exerciseId: ex.id,
            id: '',
            commenterName: $userDB!.username
        }
        console.log(comment)
        let updatedExercise: Exercise = { ...ex, comments: [...ex.comments, comment]}
        try {
            const savedComment: ExerciseComment = await ProgramService.addExerciseComment(comment)
            console.log(savedComment)
            ex.comments.push(savedComment)
            exercise = ex
            newCommentContent = ''
        } catch (e) {
            console.log(e)
        }
        loadingAthleteProgram.set(false)
    }

    const handleEditSetsComplete = (incOrDec: 'plus' | 'minus') => {
        if (incOrDec === 'plus') {
            setsComplete = setsComplete + 1
            if (repsComplete[0] === 0) {
                repsComplete.forEach((r, i) => r = exercise.repArr[i])
            }
        } else if (setsComplete > 0) {
            setsComplete = setsComplete - 1
        }
    }

    const completeExercise = async () => {
        let repsCompleteAreZero = false
        for (const r of repsComplete)  {
            if (r !== 0) {
                repsCompleteAreZero = true
                break
            }
        }
        const updatedExercise: Exercise = {
            ...exercise,
            setsComplete: setsComplete > 0 ? setsComplete : exercise.sets,
            repCompletedArr: repsCompleteAreZero ? exercise.repArr : repsComplete,
            isComplete: true,
            weightCompleted: exercise.weight
        }
        try {
            const dbExercise: Exercise = await ProgramService.updateExercise(updatedExercise)
            exercise = dbExercise
        } catch (e) {
            console.log(e)
        }
    }

    const markExerciseIncomplete = async () => {

    }

    const skipExercise = async () => {
        const updatedExercise: Exercise = {
            ...exercise,
            setsComplete: 0,
            repCompletedArr: exercise.repArr.map(_ => 0),
            isComplete: true,
            weightCompleted: 0
        }
        try {
            const dbExercise: Exercise = await ProgramService.updateExercise(updatedExercise)
            exercise = dbExercise
        } catch (e) {
            console.log(e)
        }
    }

</script>

<div class="bg-gray-200 lg:p-4 my-2 rounded relative m-4 lg:mx-1 p-2 md:p-2 flex flex-col">
    <p class="lg:w-fit w-fit m-0 text-lg p-1 font-bold text-textblue self-center lg:self-start lg:ml-4">
        {exerciseName}
    </p>
    <div class="lg:flex lg:flex-row">
        {#if !exercise.isMax}
            <div class="m-0 p-1 text-base text-md lg:text-lg lg:font-semibold text-textblue flex flex-col lg:flex-row items-center lg:mx-4">
                <div class="flex mb-2">
                    <input type="number"
                           class={`bg-gray-200 w-12 lg:w-12 text-center ${exercise.isComplete ? 'text-green' : 'opacity-60'}`}
                           bind:value={exercise.weightCompleted}
                    >
                    <p class="m-0 w-fit">&nbsp;/&nbsp;&nbsp;&nbsp;{exercise.weight} &nbsp;</p>
                    <select on:change={(e) => handleChangeWeightUnits(e.target.value, exercise)} class="bg-gray-200 w-fit mx-0">
                        <option selected>kg</option>
                        <option>lb</option>
                    </select>
                </div>
                <div class="m-0 mb-2 p-1 text-base lg:text-lg font-semibold bg-gray-200 text-textblue flex lg:justify-center items-center lg:mx-4">
                    <div class="w-7 mr-3 lg:w-5 lg:ml-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditSetsComplete('minus')}>
                        <FaMinusCircle />
                    </div>
                    <p class="bg-gray-200 w-18">{setsComplete} &nbsp;/&nbsp; {exercise?.sets} &nbsp;Sets</p>
                    <div class="w-7 ml-3 lg:w-5 lg:ml-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditSetsComplete('plus')}>
                        <FaPlusCircle />
                    </div>
                </div>
                <div class="flex items-center mb-2">
                    {#each exercise.repArr as rep, idx}
                        <input type="number" class="bg-gray-200 text-center w-6" bind:value={rep}>
                        {#if idx < exercise.repArr.length-1}
                            +&nbsp;&nbsp;&nbsp;
                        {/if}
                    {/each}
                    Reps
                </div>
            </div>
        {:else}
            <div class="flex flex-col items-center">
                <div class="flex flex-row justify-center lg:justify-start text-lg font-semibold text-textblue p-2">
                    <p class="mx-2">{exercise.repArr.join(' + ')} RM</p>
                </div>
                <div class="flex flex-row justify-around text-lg font-semibold text-textblue">
                    <input type="number" class="bg-gray-200 text-center w-14" bind:value={exercise.weightCompleted}>
                    <p class="w-6/12">&nbsp;kg</p>
                </div>
                <div class="flex flex-row justify-center lg:justify-start text-lg font-semibold text-textblue p-2">
                    {exercise.dropSets.length < 1 ? 'No Drop Sets' : ''}
                </div>
                {#each exercise.dropSets as dropSet, idx (dropSet.id)}
                    <div class="flex flex-col justify-center lg:justify-start text-lg font-semibold text-textblue p-2">
                        <p>Drop Set {idx+1}:</p>
                        <p>{dropSet.weight} kg {dropSet.repsPerSet}x{dropSet.sets}</p>
                    </div>
                {/each}
            </div>
        {/if}
    </div>
    <div>
        <div class="flex flex-col lg:flex-row justify-center md:justify-center lg:justify-start mt-1 lg:m-2 p-2">
            {#if (!exercise?.isComplete && exercise.weightCompleted > 0 && exercise.isMax)}
                <button class="bg-gray-200 text-yellow border-yellow border-2 p-2 mx-2 rounded hover:bg-gray-400"
                        disabled={$loadingAthleteProgram}
                        on:click={completeExercise}>
                    Mark Complete
                </button>
            {:else if !exercise.isComplete}
                <button class="bg-gray-200 text-yellow border-yellow border-2 p-2 mx-2 rounded hover:bg-gray-400"
                        disabled={$loadingAthleteProgram}
                        on:click={completeExercise}>
                    Mark Complete
                </button>
            {:else if exercise?.isComplete}
                <button class="bg-gray-200 text-red border-red border-2 p-2 mx-2 rounded hover:bg-gray-400">
                    Mark Incomplete
                </button>
            {/if}
            {#if !exercise.isComplete}
                <button class="bg-gray-200 p-2 mx-2 rounded hover:bg-gray-400 text-red"
                        on:click={skipExercise}>
                    Skip Exercise
                </button>
            {/if}
        </div>
        {#if exercise?.isComplete && exercise?.weightCompleted > 0}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-green"></div>
        {:else if exercise?.isComplete}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-red-shade"></div>
        {:else}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-orange"></div>
        {/if}
    </div>
</div>
    <h4 class="font-bold hover:cursor-pointer" on:click={toggleShowComments}>{!showComments ? `Comments (${exercise.comments.length})` : `Hide Comments`}</h4>
    {#if showComments}
        {#if exercise?.comments.length < 1}
            <p class="m-0">No Comments</p>
        {/if}
        {#each exercise?.comments as comment (comment.id)}
            <div class="flex flex-col p-2 bg-gray-300 rounded-xl lg:w-6/12 text-textblue my-2">
                <div class="flex flex-row justify-between mb-2">
                    <h5>{comment.commenterName}</h5>
                    <h5>{dayjs(comment.createdAt).format('MMMM D h:mm A')}</h5>
                </div>
                <hr>
                <p>{comment.content}</p>
            </div>
        {/each}
        <div>
            <textarea class="w-full break-words text-left h-16 text-black caret-black px-1 bg-textblue"
                      bind:value={newCommentContent}
                      type="text"></textarea>
        </div>
        <button class="bg-yellow text-black p-2 mx-2 rounded hover:bg-yellow-shade"
                disabled={$loadingAthleteProgram}
                on:click={() => addComment(exercise)}>{!$loadingAthleteProgram ? 'Comment' : 'Loading'}
        </button>
    {/if}


<style>

</style>
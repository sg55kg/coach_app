<script lang="ts">

    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {AthleteData} from "$lib/classes/user/athlete";
    import {Exercise} from "$lib/classes/program/exercise";
    import FaRegCommentAlt from 'svelte-icons/fa/FaRegCommentAlt.svelte'
    import dayjs from "dayjs";

    export let exercise: Exercise
    export let athlete: AthleteData

    let showComments: boolean = false
</script>

<div class="bg-gray-200 my-4 p-2 relative">
    {#if exercise.type === ExerciseType.EXERCISE}
        <h4 class="text-xl font-medium mb-2">{exercise.name}</h4>
        <div>
            <p class="text-textblue">{athlete.name}'s Performance</p>
            <hr class="text-textblue">
            <div class="py-2">
                <p>{exercise.weightCompleted} / {exercise.weight} kg</p>
                <p>{exercise.setsCompleted} / {exercise.sets} sets</p>
                <p>
                    {exercise.setsCompleted > 0 ?
                        exercise.totalRepsCompleted/exercise.setsCompleted :
                        (exercise.totalRepsCompleted > 0 ? exercise.totalRepsCompleted : 0)
                    } / {exercise.isMaxReps ? 'max' : exercise.repsPerSet} reps</p>
            </div>
        </div>
        {#if exercise.weightCompleted > 0 && exercise.totalRepsCompleted > 0}
            <div class="bg-green w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {:else}
            <div class="bg-red w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {/if}
    {:else if exercise.type === ExerciseType.DURATION}
        <h4 class="text-xl font-medium mb-2">{exercise.name}</h4>
        <div>
            <p class="text-textblue">{athlete.name}'s Performance</p>
            <hr class="text-textblue">
            <div class="py-2">
                <p>{exercise.weightCompleted} / {exercise.weight} kg</p>
                <p>{exercise.distanceCompletedMeters} / {exercise.distanceMeters} meters</p>
                <p>{exercise.secondsPerSetCompleted} / {exercise.secondsPerSet} seconds</p>
            </div>
        </div>
        {#if exercise.weightCompleted > 0 && exercise.totalRepsCompleted > 0}
            <div class="bg-green w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {:else}
            <div class="bg-red w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {/if}
    {:else if exercise.type === ExerciseType.COMPLEX}
        <h4 class="text-xl font-medium mb-2">{exercise.nameArr.join(' + ')}</h4>
        <div>
            <p class="text-textblue">{athlete.name}'s Performance</p>
            <hr class="text-textblue">
            <div class="py-2">
                <p>{exercise.weightCompleted} / {exercise.weight} kg</p>
                <p>{exercise.setsCompleted} / {exercise.sets} sets</p>
                <p>{exercise.repCompletedArr.join(' + ')} / {exercise.repArr.join(' + ')} reps</p>
            </div>
        </div>
        {#if exercise.weightCompleted > 0 && exercise.repCompletedArr.length > 0}
            <div class="bg-green w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {:else}
            <div class="bg-red w-full h-1 absolute bottom-0 right-0 left-0">
            </div>
        {/if}
    {/if}
</div>
<div class="h-6 flex hover:cursor-pointer" on:click={() => showComments = !showComments}>
    <span class="mr-2"><FaRegCommentAlt /> </span>
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

<style></style>
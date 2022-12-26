<script lang="ts">
    import {Exercise, ExerciseComment, Program} from "$lib/classes/program";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {completedExercises, currentDay, currentProgram, incompleteExercises} from "$lib/stores/athleteProgramStore";
    import {Day} from "$lib/classes/day";
    import dayjs from "dayjs";

    export let exercise: Exercise

    let newCommentContent: string = ''
    let showComments: boolean = false

    const toggleShowComments = () => showComments = !showComments

    const completeExercise = async (exercise: Exercise) => {
        if (!$auth0Client) return

        let updatedExercise: Exercise = !exercise.isComplete ? {
            ...exercise,
            isComplete: true,
            weightCompleted: exercise.weight,
            totalRepsCompleted: (exercise.sets * exercise.repsPerSet)
        } : {
            ...exercise,
            isComplete: false,
            weightCompleted: 0,
            totalRepsCompleted: 0
        }

        try {
            const updatedProgram: Program = await ProgramService.updateExercise($auth0Client, updatedExercise, $currentProgram!.id)
            completedExercises.update(prev => {
                prev.push(updatedExercise)
                return prev
            })
            const updatedDay = updatedProgram.days.find(d => d.id === $currentDay!.id)
            currentProgram.set(updatedProgram)
            currentDay.set(updatedDay)
            incompleteExercises.set(updatedDay.exercises.sort((a, b) => a.order - b.order))
        } catch (e) {
            console.log(e)
        }
    }

    // There is a bug here where all inputs will share the same text
    const addComment = async (exercise: Exercise) => {
        let comment: ExerciseComment = {
            content: newCommentContent,
            athleteId: $userDB!.athleteData.id,
            exerciseId: exercise.id,
            id: '',
            commenterName: $userDB!.username
        }
        let updatedExercise: Exercise = { ...exercise, comments: [...exercise.comments, comment]}
        try {
            const updatedProgram: Program = await ProgramService.updateExercise($auth0Client!, updatedExercise, $currentProgram!.id)
            const updatedDay: Day = updatedProgram.days.find(d => d.id === $currentDay!.id)
            currentDay.set(updatedDay)
            incompleteExercises.set(updatedDay.exercises.sort((a, b) => a.order - b.order))
            currentProgram.set(updatedProgram)
            newCommentContent = ''
        } catch (e) {
            console.log(e)
        }
    }

</script>


<div class="flex-col bg-gray-300 p-4 my-2 rounded relative">
    <div class="flex flex-row justify-around">
        <p class="w-fit m-0">{exercise.name}</p>
        <p class="w-fit m-0">{exercise.weight}</p>
        <p class="w-fit m-0">{exercise.sets}</p>
        <p class="w-fit m-0">{exercise.repsPerSet}</p>
    </div>
    <div>
        <p>{exercise.notes}</p>
    </div>
    <div>
        <textarea class="w-full break-words text-left h-16 text-black caret-black px-1 bg-textblue" bind:value={newCommentContent} type="text"></textarea>
    </div>
    <div>
        <button class="bg-yellow text-black p-2 rounded hover:bg-yellow-shade" on:click={() => addComment(exercise)}>Comment</button>
        <button class="bg-yellow text-black p-2 rounded hover:bg-yellow-shade" on:click={() => completeExercise(exercise)}>
            {exercise.isComplete ? 'Mark Incomplete' : 'Mark Complete'}
        </button>
        <div class={(exercise.isComplete ? 'bg-green' : 'bg-red-shade') + ' h-1 absolute bottom-0 w-full left-0'}></div>
    </div>
</div>
<div>
    <h4 class="font-bold hover:cursor-pointer" on:click={toggleShowComments}>{!showComments ? `Comments (${exercise.comments.length})` : `Hide Comments`}</h4>
    {#if showComments}
        {#if exercise.comments.length < 1}
            <p class="m-0">No Comments</p>
        {/if}
        {#each exercise.comments as comment (comment.id)}
            <div class="flex flex-col p-2 bg-textblue rounded-xl text-black my-2">
                <div class="flex flex-row justify-between mb-2">
                    <h5>{comment.commenterName}</h5>
                    <h5>{dayjs(comment.createdAt).format('MMMM D h:mm A')}</h5>
                </div>
                <hr>
                <p>{comment.content}</p>
            </div>
        {/each}
    {/if}
</div>

<style>

</style>
<script lang="ts">
    import {Exercise, ExerciseComment, Program} from "$lib/classes/program";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {completedExercises, currentDay} from "$lib/stores/athleteProgramStore";

    export let exercise: Exercise

    let newCommentContent: string = ''

    const completeExercise = async (exercise: Exercise) => {
        if (!$auth0Client) return
        let updated = {
            ...exercise,
            weightCompleted: exercise.weight,
            totalRepsCompleted: (exercise.sets * exercise.repsPerSet)
        }
        try {
            updated = await ProgramService.updateExercise($auth0Client, updated)
            completedExercises.update(prev => {
                prev.push(updated)
                return prev
            })
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
            id: ''
        }
        exercise.comments.push(comment)
        try {
            const exerciseRes = await ProgramService.updateExercise($auth0Client!, exercise)
            currentDay.update(prev => {
                if (prev === null || $currentDay === null) return prev
                prev.exercises = $currentDay.exercises!.map(e => e.id === exercise.id ? exerciseRes : e)
                return prev
            })
            newCommentContent = ''
        } catch (e) {
            console.log(e)
        }
    }

</script>

<div class="flex-col bg-gray-300 p-4 my-2 rounded">
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
        <input bind:value={newCommentContent} type="text">
    </div>
    <div>
        <button class="bg-green" on:click={() => addComment(exercise)}>Comment</button>
        <button class="bg-green" on:click={() => completeExercise(exercise)}>
            Mark Complete
        </button>
    </div>
</div>

<style>

</style>
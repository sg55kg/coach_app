<script lang="ts">

    import {ExerciseType} from "$lib/classes/program/exercise/enums.js";
    import {AthleteData} from "$lib/classes/user/athlete";
    import {Exercise} from "$lib/classes/program/exercise";

    export let exercise: Exercise
    export let athlete: AthleteData
    console.log(exercise.name, exercise.setsCompleted)
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

<style></style>
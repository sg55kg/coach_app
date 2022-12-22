<script lang="ts">
    import {Exercise, ExerciseComment, Program} from "$lib/classes/program";
    import dayjs, {Dayjs} from "dayjs";
    import {onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {Day} from "$lib/classes/day";
    import FaAngleLeft from 'svelte-icons/fa/FaAngleLeft.svelte'
    import FaAngleRight from 'svelte-icons/fa/FaAngleRight.svelte'

    export let currentProgramId: string
    let currentProgram: Program
    const today: Dayjs = dayjs()

    const setCurrentDay = (day: Dayjs) => {
        if (!currentProgram) return

        let programDay: Day | undefined = currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))

        if (programDay !== undefined) {
            incompleteExercises = [...programDay.exercises]
            return programDay
        } else if (currentProgram.days.length > 0) {
            programDay = currentProgram.days[currentProgram.days.length - 1]
            return programDay
        } else {
            return null
        }
    }

    const decrementDay = () => {
        let day = dayjs(currentDay.date)
        day = day.subtract(1, 'day')

        let programDay: Day | undefined = currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))
        if (programDay !== undefined) {
            incompleteExercises = [...programDay.exercises]
            currentDay = programDay
        }
    }

    const incrementDay = () => {
        let day = dayjs(currentDay.date)
        day = day.add(1, 'day')

        const programDay: Day | undefined = currentProgram.days.find(d => dayjs(d.date).isSame(day, 'day'))
        if (programDay !== undefined) {
            incompleteExercises = [...programDay.exercises]
            currentDay = programDay ? programDay : new Day()
        }
    }

    let currentDay: Day
    let incompleteExercises: Exercise[] = []
    let completedExercises: Exercise[] = []
    let newComment: string = ''


    const completeExercise = async (exercise: Exercise) => {
        if (!$auth0Client) return
        let updated = {
            ...exercise,
            weightCompleted: exercise.weight,
            totalRepsCompleted: (exercise.sets * exercise.repsPerSet)
        }
        try {
            updated = await ProgramService.updateExercise($auth0Client, updated)
            completedExercises.push(updated)
        } catch (e) {
            console.log(e)
        }
    }

    // There is a bug here where all inputs will share the same text
    const addComment = async (exercise: Exercise) => {
        let comment: ExerciseComment = {
            content: newComment,
            athleteId: $userDB!.id,
            exerciseId: exercise.id,
            id: ''
        }
        exercise.comments.push(comment)
        try {
            const exerciseRes = await ProgramService.updateExercise($auth0Client!, exercise)
            currentDay.exercises = currentDay.exercises.map(e => e.id === exercise.id ? exerciseRes : e)
            newComment = ''
        } catch (e) {
            console.log(e)
        }
    }

    onMount(async () => {
        if (!$auth0Client) return
        const p = await ProgramService.getProgram($auth0Client, currentProgramId)
        console.log(p)
        currentProgram = p
        currentDay = setCurrentDay(today)
    })


</script>

{#if currentProgram}
    <div>
        <h1>{currentProgram.name}</h1>
        <div class="flex flex-row">
            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={decrementDay}>
                <FaAngleLeft />
            </div>
            {#if currentDay}
                <h2 class={today.isSame(dayjs(currentDay.date), 'days') && 'text-green'}>
                    {dayjs(currentDay.date).format('dddd MMMM D, YYYY')}
                </h2>
            {:else}
                <h2>{today.format('dddd MMMM D, YYYY')}</h2>
            {/if}

            <div class="w-6 text-textgray hover:text-gray-300 hover:cursor-pointer"
                 on:click={incrementDay}>
                <FaAngleRight />
            </div>
        </div>

        {#if currentDay}
            {#each incompleteExercises as exercise}
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
                        <input bind:value={newComment} type="text">
                    </div>
                    <div>
                        <button class="bg-green" on:click={() => addComment(exercise)}>Comment</button>
                        <button class="bg-green" on:click={() => completeExercise(exercise)}>
                            Mark Complete
                        </button>
                    </div>
                </div>
            {/each}
            {#each completedExercises as exercise}
                <div class="flex-col border-b-green">
                    <div>
                        <p class="w-fit m-0">{exercise.name}</p>
                        <p class="w-fit m-0">{exercise.weight}</p>
                        <p class="w-fit m-0">{exercise.sets}</p>
                        <p class="w-fit m-0">{exercise.repsPerSet}</p>
                    </div>
                    <div>
                        <button>
                            Edit
                        </button>
                    </div>
                </div>
            {/each}
        {/if}
    </div>
{/if}


<style>

</style>
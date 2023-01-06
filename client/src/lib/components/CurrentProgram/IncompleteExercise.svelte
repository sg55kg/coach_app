<script lang="ts">
    import {Program} from "$lib/classes/program";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {
        completedExercises,
        currentDay,
        currentProgram,
        incompleteExercises,
        loadingAthleteProgram
    } from "$lib/stores/athleteProgramStore";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import {AthleteData, AthleteRecord, type AthleteRecordDTO} from "$lib/classes/user";
    import UserService from "$lib/service/userService";
    import {onMount} from "svelte";
    import {Exercise, ExerciseComment} from "$lib/classes/program/exercise";

    export let exercise: Exercise

    let newCommentContent: string = ''
    let showComments: boolean = false

    const toggleShowComments = () => showComments = !showComments

    const weightIsNewPersonalBest = (exercise: Exercise, records: AthleteRecord) => {
        console.log('isPR',records)
        const formattedName = exercise.name.trim().toLowerCase().split(' ').join('_')
        console.log('formattedName', formattedName)
        console.log(records?.records?.get(formattedName))
        if (!records) {
            records = new AthleteRecord({ id: '', [formattedName]: exercise.weightCompleted } as AthleteRecordDTO);
        }
        if (records.records.get(formattedName) === undefined) {
            console.log('notFound')
            return ''
        }
        const weight = records.records.get(formattedName)
        if (weight < exercise.weightCompleted && exercise.weightCompleted !== 0) {
            console.log('found',formattedName)
            return formattedName
        }
        return ''
    }

    const weightIsTiedPersonalBest = (exercise: Exercise, records: AthleteRecord) => {
        if (!exercise || !records) return
        const formattedName = exercise.name.trim().toLowerCase().split(' ').join('_')

        if (!records.records.get(formattedName)) {
            return ''
        }
        const weight = records.records.get(formattedName)
        if (weight === exercise.weightCompleted && exercise.weightCompleted !== 0) {
            return formattedName
        }
        return ''
    }

    let isPersonalBest: boolean = false
    let isNewPersonalBest: boolean = false

    const completeExercise = async (exercise: Exercise) => {
        if (!$auth0Client || !$userDB?.athleteData) return
        loadingAthleteProgram.set(true)

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
        console.log('updatedExercise',updatedExercise)

        if (updatedExercise.weightCompleted > 0) {
            const recordKey = weightIsNewPersonalBest(updatedExercise, $userDB.athleteData.records[$userDB.athleteData.records.length - 1])

            if (recordKey !== '') {
                const updatedAthleteData = {
                    ...$userDB.athleteData,
                    records: $userDB.athleteData.records.map(r => {
                        return { ...r, records: Object.fromEntries(r.records)}
                    })
                } as AthleteData
                console.log('beforeRecords', updatedAthleteData)
                const newRecord = updatedAthleteData.records[updatedAthleteData.records.length-1]

                updatedAthleteData.records.push({
                    ...newRecord,
                    id: null,
                    lastUpdated: $currentDay!.date,
                    ...newRecord.records,
                    [recordKey]: updatedExercise.weightCompleted

                } as AthleteRecord)
                console.log('updatedRecords',updatedAthleteData.records)

                try {
                    const res = await UserService.updateAthleteRecords(
                        $auth0Client,
                        updatedAthleteData.records[updatedAthleteData.records.length-1],
                        $userDB.athleteData.id
                    )
                    userDB.update(prev => {
                        prev!.athleteData!.records.push(res)
                        return prev
                    })
                    isNewPersonalBest = true
                    isPersonalBest = false
                    console.log(res)
                } catch (e) {
                    console.log(e)
                }
            }
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
        loadingAthleteProgram.set(false)
    }

    const addComment = async (exercise: Exercise) => {
        loadingAthleteProgram.set(true)
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
        loadingAthleteProgram.set(false)
    }

    onMount(() => {
        // this may need tweaking. If they tie a PR, it probably should show it was a tie vs saying PERSONAL RECORD!!!
        isPersonalBest = weightIsTiedPersonalBest(exercise, $userDB!.athleteData!.records[$userDB!.athleteData!.records.length-1]) !== ''
    })

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
        <button class="bg-yellow text-black p-2 rounded hover:bg-yellow-shade"
                disabled={$loadingAthleteProgram}
                on:click={() => addComment(exercise)}>{!$loadingAthleteProgram ? 'Comment' : 'Loading'}</button>
        <button class="bg-yellow text-black p-2 rounded hover:bg-yellow-shade"
                disabled={$loadingAthleteProgram}
                on:click={() => completeExercise(exercise)}>
            {exercise.isComplete ? 'Mark Incomplete' : 'Mark Complete'}
        </button>
        {#if isPersonalBest}
            <div class="m-2 p-2 flex justify-center">
                <p class="text-yellow">Current Record</p>
            </div>
        {/if}
        {#if isNewPersonalBest}
            <div class="m-2 p-2 flex justify-center">
                <p class="text-green">New Record!</p>
            </div>
        {/if}
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
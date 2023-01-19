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
    import FaPlusCircle from 'svelte-icons/fa/FaPlusCircle.svelte'
    import FaMinusCircle from 'svelte-icons/fa/FaMinusCircle.svelte'
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import {AthleteData, AthleteRecord, type AthleteRecordDTO} from "$lib/classes/user";
    import UserService from "$lib/service/userService";
    import {onMount} from "svelte";
    import {Exercise, ExerciseComment} from "$lib/classes/program/exercise";
    import ProgramStats from "$lib/components/Stats/ProgramStats.svelte";

    export let exercise: Exercise

    let newCommentContent: string = ''
    let showComments: boolean = false
    let setsComplete: number = 0
    let repsPerSetComplete: number = 0
    let weightUnitsSelected: 'kg' | 'lb' = 'kg'

    const toggleShowComments = () => showComments = !showComments

    const weightIsNewPersonalBest = (exercise: Exercise, records: AthleteRecord) => {
        if (!exercise.isComplete) return ''
        const formattedName = exercise.name.trim().toLowerCase().split(' ').join('_')

        if (records.records.get(formattedName) === undefined) {
            console.log('could not find')
            return ''
        }
        const weight = records.records.get(formattedName)
        console.log('weight', weight)
        if (weight < exercise.weightCompleted && exercise.weightCompleted !== 0) {
            console.log('found new record')
            return formattedName
        }
        return ''
    }

    const weightIsTiedPersonalBest = (exercise: Exercise, records: AthleteRecord) => {
        if (!exercise || !records || !exercise.isComplete) return ''
        const formattedName = exercise.name.trim().toLowerCase().split(' ').join('_')
        console.log('checking tied')
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
        if (!$userDB?.athleteData) return

        let sets = 0
        let reps = 0
        let weight = 0

        // determine if user manually entered reps/sets/weight, or if we should just use the coaches prescribed values
        // ...provided that the athlete is marking the exercise completed
        console.log(exercise)
        if (!exercise.isComplete) {
            if (exercise.isMax) {
                sets = setsComplete > 0 ? setsComplete : 1
            } else {
                sets = setsComplete > 0 ? setsComplete : exercise.sets
            }
            reps = repsPerSetComplete > 0 ? repsPerSetComplete : exercise.repsPerSet
            weight = exercise.weightCompleted > 0 ? exercise.weightCompleted : exercise.weight
        }
        console.log('sets', sets)
        console.log('reps', reps)
        console.log('weight', weight)

        let updatedExercise: Exercise = {
            ...exercise,
            isComplete: !exercise.isComplete,
            weightCompleted: weight,
            totalRepsCompleted: (sets * reps),
            setsComplete: sets
        }
        repsPerSetComplete = reps
        setsComplete = sets
        console.log('updatedExercise',updatedExercise)
        await saveExerciseChanges(updatedExercise)
    }

    const saveExerciseChanges = async (updatedExercise: Exercise) => {
        loadingAthleteProgram.set(true)
        if (updatedExercise.weightCompleted > 0) {
            let recordKey = weightIsNewPersonalBest(updatedExercise, $userDB.athleteData.records[$userDB.athleteData.records.length - 1])

            if (recordKey !== '') {
                // set the records back to DTO format so the backend can read the contents
                const updatedAthleteData = {
                    ...$userDB.athleteData,
                    records: $userDB.athleteData.records.map(r => {
                        return { ...r, records: Object.fromEntries(r.records)}
                    })
                } as AthleteData

                console.log('beforeRecords', updatedAthleteData)
                // grab the most recent record
                const newRecord = updatedAthleteData.records[updatedAthleteData.records.length-1]

                // use existing record for this day if it already exists
                if (!newRecord.lastUpdated.isSame(dayjs($currentDay?.date))) {
                    updatedAthleteData.records.push({
                        ...newRecord,
                        id: null,
                        lastUpdated: dayjs($currentDay!.date),
                        ...newRecord.records,
                        [recordKey]: updatedExercise.weightCompleted

                    } as AthleteRecord)
                    console.log('updatedRecords',updatedAthleteData.records)
                }

                try {
                    const res = await UserService.updateAthleteRecords(
                        updatedAthleteData.records[updatedAthleteData.records.length-1],
                        $userDB.athleteData.id
                    )
                    userDB.update(prev => {
                        prev!.athleteData!.records = res
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
            const dbExercise: Exercise = await ProgramService.updateExercise(updatedExercise)
            completedExercises.update(prev => {
                prev.push(updatedExercise)
                return prev
            })
            currentDay.update(prev => {
                prev!.exercises = prev!.exercises.map(e => e.id === dbExercise.id ? dbExercise : e)
                return prev
            })
            incompleteExercises.set($currentDay!.exercises.sort((a, b) => a.order - b.order))
        } catch (e) {
            console.log(e)
        }
        loadingAthleteProgram.set(false)
    }

    const skipExercise = async (exercise: Exercise) => {

        loadingAthleteProgram.set(true)
        const updatedExercise = {
            ...exercise,
            isComplete: true,
            weightCompleted: 0,
            totalRepsCompleted: 0
        }
        try {
            const dbExercise: Exercise = await ProgramService.updateExercise(updatedExercise)
            currentDay.update(prev => {
                prev!.exercises = prev!.exercises.map(e => e.id === dbExercise.id ? dbExercise : e)
                return prev
            })
            incompleteExercises.set($currentDay!.exercises.sort((a, b) => a.order - b.order))
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
        console.log(comment)
        let updatedExercise: Exercise = { ...exercise, comments: [...exercise.comments, comment]}
        try {
            const savedComment: ExerciseComment = await ProgramService.addExerciseComment(comment)
            console.log(savedComment)
            exercise.comments.push(savedComment)
            exercise = exercise
            newCommentContent = ''
        } catch (e) {
            console.log(e)
        }
        loadingAthleteProgram.set(false)
    }

    const handleEditRepsComplete = (incOrDec: 'plus' | 'minus') => {
        if (incOrDec === 'plus') {
            repsPerSetComplete = repsPerSetComplete + 1
        } else if (repsPerSetComplete > 0) {
            repsPerSetComplete = repsPerSetComplete - 1
        }
    }

    const handleEditSetsComplete = (incOrDec: 'plus' | 'minus') => {

        if (incOrDec === 'plus') {
            setsComplete = setsComplete + 1
            if (repsPerSetComplete < 1) {
                repsPerSetComplete = exercise.repsPerSet
            }
        } else if (setsComplete > 0) {
            setsComplete = setsComplete - 1
        }
    }

    const handleChangeWeightUnits = (units: 'kg' | 'lb') => {
        if (units === 'kg' && weightUnitsSelected === 'lb') {
            weightUnitsSelected = 'kg'
            exercise.weight = Math.round(exercise.weight / 2.2042)
            exercise.weightCompleted = exercise.weightCompleted > 0 ? Math.round(exercise.weightCompleted / 2.2042) : 0
        } else if (units === 'lb' && weightUnitsSelected === 'kg') {
            weightUnitsSelected = 'lb'
            exercise.weight = Math.round(exercise.weight * 2.2042)
            exercise.weightCompleted = exercise.weightCompleted > 0 ? Math.round(exercise.weightCompleted * 2.2042) : 0
        }
    }

    onMount(() => {
        isPersonalBest = weightIsTiedPersonalBest(exercise, $userDB!.athleteData!.records[$userDB!.athleteData!.records.length-1]) !== ''
        repsPerSetComplete = exercise.totalRepsCompleted > 0 ? Math.round(exercise.totalRepsCompleted / exercise.sets) : 0
        setsComplete = exercise.totalRepsCompleted > 0 ? exercise.sets : 0
    })

</script>

<div class="bg-gray-200 lg:p-4 my-2 rounded relative m-4 lg:mx-1 p-2 md:p-2">
    <p class="lg:w-fit w-fit m-0 text-lg p-1 font-bold text-textblue self-center lg:ml-4">{exercise?.name}</p>
    <div class="lg:flex lg:flex-row">
        {#if !exercise.isMax}
            <div class="m-0 p-1 text-base lg:text-lg font-semibold text-textblue flex lg:mx-4">
                <input class={`bg-gray-200 w-8 ${exercise.isComplete ? 'text-green' : 'opacity-60'}`} bind:value={exercise.weightCompleted}>
                <p class="m-0 w-fit">&nbsp;/&nbsp;{exercise.weight} &nbsp;</p>
                <select on:change={(e) => handleChangeWeightUnits(e.target.value)} class="bg-gray-200 w-fit mx-0">
                    <option selected>kg</option>
                    <option>lb</option>
                </select>
            </div>
            <div class="m-0 p-1 text-base lg:text-lg font-semibold bg-gray-200 text-textblue flex lg:justify-center items-center  lg:mx-4">
                <div class="w-7 mr-3 lg:w-5 lg:ml-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditSetsComplete('minus')}>
                    <FaMinusCircle />
                </div>
                <p class="bg-gray-200 w-18">{setsComplete} &nbsp;/&nbsp; {exercise?.sets} &nbsp;Sets</p>
                <div class="w-7 ml-3 lg:w-5 lg:ml-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditSetsComplete('plus')}>
                    <FaPlusCircle />
                </div>
            </div>
            <div class="m-0 p-1 text-base lg:text-lg font-semibold bg-gray-200 text-textblue flex lg:justify-center items-center lg:mx-4">
                <div class="w-7 mr-3 lg:w-5 lg:mr-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditRepsComplete('minus')}>
                    <FaMinusCircle />
                </div>
                <p class="bg-gray-200 w-18">{repsPerSetComplete} &nbsp;/&nbsp; {exercise?.repsPerSet} &nbsp;Reps</p>
                <div class="w-7 ml-3 lg:w-5 lg:ml-2 text-gray-400 hover:text-textblue hover:cursor-pointer" on:click={() => handleEditRepsComplete('plus')}>
                    <FaPlusCircle />
                </div>
            </div>
        {:else}
            <div class="flex flex-col items-center">
                <div class="flex flex-row justify-center lg:justify-start text-lg font-semibold text-textblue p-2">
                    <p class="mx-2">{exercise?.name}</p>
                    <p class="mx-2">{exercise?.repsPerSet} RM</p>
                </div>
                <div class="flex flex-row justify-around text-lg font-semibold text-textblue">
                    <input type="number" class="flex-1 bg-gray-200 text-center w-14" bind:value={exercise.weightCompleted}>
                    <p class="w-6/12">&nbsp;kg</p>
                </div>
            </div>
        {/if}
    </div>
    {#if exercise?.notes}
        <div class="p-2">
            <hr>
            <p class="font-semibold">Coach Notes:</p>
            <p class="text-textblue"><i>{exercise?.notes}</i></p>
        </div>
    {/if}

    <div>
        <div class="flex justify-center md:justify-center lg:justify-start mt-1 lg:m-2 p-2">
        {#if !exercise?.isMax || exercise?.weightCompleted > 0}
                <button class="bg-yellow text-black p-2 mx-2 rounded hover:bg-yellow-shade"
                        disabled={$loadingAthleteProgram}
                        on:click={() => completeExercise(exercise)}>
                    {exercise.isComplete ? 'Mark Incomplete' : 'Mark Complete'}
                </button>
        {/if}
            {#if !exercise.isComplete}
                <button class="bg-gray-200 p-2 mx-2 rounded hover:bg-gray-400 text-red"
                        on:click={() => skipExercise(exercise)}>
                    Skip Exercise
                </button>
            {/if}
        </div>
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
        {#if exercise?.isComplete && exercise?.weightCompleted > 0 && exercise?.totalRepsCompleted > 0}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-green"></div>
        {:else if exercise?.isComplete}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-red-shade"></div>
        {:else}
            <div class="h-1 absolute bottom-0 w-full left-0 bg-orange"></div>
        {/if}
    </div>
</div>
<div>
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
            <textarea class="w-full break-words text-left h-16 text-black caret-black px-1 bg-textblue" bind:value={newCommentContent} type="text"></textarea>
        </div>
        <button class="bg-yellow text-black p-2 mx-2 rounded hover:bg-yellow-shade"
                disabled={$loadingAthleteProgram}
                on:click={() => addComment(exercise)}>{!$loadingAthleteProgram ? 'Comment' : 'Loading'}</button>
    {/if}
</div>

<style>

</style>
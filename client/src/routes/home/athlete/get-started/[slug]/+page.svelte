<script lang="ts">
    import dayjs from "dayjs";
    import {userDB} from "$lib/stores/authStore";
    import UserService from "$lib/service/userService";
    import {goto} from "$app/navigation";
    import {AthleteData, AthleteRecord} from "$lib/classes/user/athlete";

    let snatch: number = 0
    let cj: number = 0
    let backSquat: number = 0
    let frontSquat: number = 0
    let deadlift: number = 0
    let bench: number = 0
    let ohp: number = 0
    let row: number = 0

    const saveAndRedirect = async () => {
        const athlete = new AthleteData()
        athlete.name = $userDB.username
        athlete.userId = $userDB.id

        try {
            const updatedUser = await UserService.createAthleteData(athlete)
            if (updatedUser) {
                $userDB = updatedUser
            }
            const records = generateRecords($userDB.athleteData.id)
            if (records.length) {
                await UserService.createAthleteRecords($userDB.athleteData.id, records)
            }
            await goto('/home/athlete')
        } catch (e) {
            console.log(e)
        }
    }

    const generateRecords = (athleteId: string) => {
        const records: AthleteRecord[] = []
        if (snatch) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Snatch'
            record.numReps = 1
            record.weight = snatch

            records.push(record)
        }
        if (cj) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Clean and Jerk'
            record.numReps = 1
            record.weight = cj

            records.push(record)
        }
        if (backSquat) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Back squat'
            record.numReps = 1
            record.weight = backSquat

            records.push(record)
        }
        if (frontSquat) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Front squat'
            record.numReps = 1
            record.weight = frontSquat

            records.push(record)
        }
        if (deadlift) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Deadlift'
            record.numReps = 1
            record.weight = deadlift

            records.push(record)
        }
        if (bench) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Bench press'
            record.numReps = 1
            record.weight = bench

            records.push(record)
        }
        if (ohp) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Overhead Press'
            record.numReps = 1
            record.weight = ohp

            records.push(record)
        }
        if (row) {
            const record = new AthleteRecord()

            record.athleteId = athleteId
            record.createdAt = dayjs()
            record.dayId = null
            record.exerciseId = null
            record.exerciseName = 'Bent over row'
            record.numReps = 1
            record.weight = row

            records.push(record)
        }
        return records
    }

</script>

<div class="flex justify-center w-full mt-4">
    <div class="bg-gray-200 p-4 text-textblue shadow-2xl shadow-black w-10/12 mb-4">
        <h1 class="font-bold text-2xl mb-2 text-center text-textgray">Let's get started!</h1>
        <h2 class="lg:font-semibold font-medium text-md lg:text-lg text-center">Help us by entering some your personal records so we can track your progress</h2>
        <div class="flex flex-col items-center p-1 lg:p-4 mt-4 lg:font-medium lg:w-full overflow-y-auto">
            <small>Fill out any lifts you know, or leave them blank and click get started!</small>
            <div class="p-2">
                <p>Snatch (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={snatch}>
            </div>
            <div class="p-2">
                <p>Clean And Jerk (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={cj}>
            </div>
            <div class="p-2">
                <p>Back Squat (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={backSquat}>
            </div>
            <div class="p-2">
                <p>Front Squat (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={frontSquat}>
            </div>
            <div class="p-2">
                <p>Deadlift (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={deadlift}>
            </div>
            <div class="p-2">
                <p>Bench Press (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={bench}>
            </div>
            <div class="p-2">
                <p>Overhead Press (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={ohp}>
            </div>
            <div class="p-2">
                <p>Bent Over Row (1 rep)</p>
                <input type="number" class="bg-gray-300 p-1 rounded" bind:value={row}>
            </div>
            <button on:click={saveAndRedirect} class="text-link text-lg hover:text-link-shade hover:cursor-pointer">
                Save
            </button>
        </div>
    </div>
</div>

<style>

</style>
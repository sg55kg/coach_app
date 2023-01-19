<script lang="ts">

    import {onMount} from "svelte";
    import {AthleteData, AthleteRecord, athleteRecordFields} from "$lib/classes/user";
    import dayjs from "dayjs";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import UserService from "$lib/service/userService";
    import {goto} from "$app/navigation";

    const initializeRecords = () => {
        let recordsMap: Map<string, number> = new Map<string, number>()
        for (const key of athleteRecordFields) {
            recordsMap.set(key.split(' ').join('_'), 0)
        }
        let recordsData = { records: recordsMap, lastUpdated: dayjs(), createdAt: null }
        return recordsData as AthleteRecord
    }

    const addRecordToMap = () => {
        records.records.set(newRecord.key, parseInt(newRecord.value))
        addedRecords.push({ ...newRecord })
        addedRecords = addedRecords
        newRecord = { key: '', value: 0 }
    }

    const handleAutoComplete = (e: KeyboardEvent) => {
        if (e.key === 'Enter') {
            selectOption(exerciseOptions[0])
        } else if (e.key === '+') {
            // have ability to make a complex here like pull + snatch
        } else {
            exerciseOptions = athleteRecordFields.filter((x) => x.includes(newRecord.key.toLowerCase()))
        }
    }

    const selectOption = (nameOption: string) => {
        newRecord.key = nameOption
        newRecord = newRecord
        showOptions = false
        exerciseOptions = [...athleteRecordFields]
    }

    let records: AthleteRecord;
    let newRecord: { key: string, value: number } = { key: '', value: 0 }
    let addedRecords: { key: string, value: number }[] = []
    let exerciseOptions: string[] = []
    let showOptions: boolean = false


    const saveAndRedirect = async () => {
        let newAthleteData: AthleteData
        if (!$userDB?.athleteData) {
            newAthleteData = new AthleteData()
            newAthleteData.records = [{ lastUpdated: dayjs(), createdAt: null, ...Object.fromEntries(records.records) } as AthleteRecord]
            newAthleteData.name = $userDB!.username
            // @ts-ignore
            newAthleteData.userId = $userDB!.id
            try {
                const athlete: AthleteData = await UserService.createAthleteData(newAthleteData)
                console.log(athlete)
                userDB.update(prev => {
                    prev!.athleteData = athlete
                    return prev
                })
                await goto('/home/athlete/teams')
            } catch (e) {
                console.log(e)
            }
        } else {
            try {
                const dbRecords: AthleteRecord[] = await UserService.updateAthleteRecords(
                    { lastUpdated: dayjs(), createdAt: null, ...Object.fromEntries(records.records) } as AthleteRecord,
                    $userDB.athleteData.id
                )
                console.log(dbRecords)
                userDB.update(prev => {
                    prev!.athleteData!.records = dbRecords
                    return prev
                })
                await goto('/home/athlete')
            } catch (e) {
                console.log(e)
            }
        }
    }

    onMount(() => {
        records = initializeRecords()
        exerciseOptions = [...athleteRecordFields]
    })
</script>

<div class="flex justify-center w-full mt-4">
    <div class="bg-gray-200 p-4 text-textblue shadow-2xl shadow-black w-10/12">
        <h1 class="font-bold text-2xl mb-2 text-center text-textgray">Let's get started!</h1>
        <h2 class="lg:font-semibold font-medium text-md lg:text-lg text-center">Help us by entering some your personal records so we can track your progress</h2>
        <div class="flex flex-row justify-around p-1 lg:p-4 mt-4 lg:font-medium lg:px-72 lg:w-full">
            <div class="relative lg:w-fit">
                <input placeholder="Exercise name"
                       class="bg-gray-300 p-1 lg:p-2 w-32 lg:w-fit rounded"
                       bind:value={newRecord.key}
                       on:focus={() => showOptions = true}
                       on:blur={() => setTimeout(() => showOptions = false, 100)}
                       on:keydown={(e) => handleAutoComplete(e)}>
                {#if showOptions}
                    <div class="absolute right-0 left-0 z-40 max-h-44 bg-gray-300 overflow-scroll min-h-fit">
                        {#each exerciseOptions as option}
                            <div on:click={() => selectOption(option)} class="p-2 hover:bg-gray-200 cursor-pointer">{option}</div>
                        {/each}
                    </div>
                {/if}
            </div>
            <div class="flex items-center">
                <input type="number"
                       class="bg-gray-300 p-1 lg:p-2 w-8 lg:w-fit rounded"
                       placeholder="Exercise weight"
                       bind:value={newRecord.value}>
                <p class="mx-2">kg</p>
            </div>

            <button class="px-4 text-white rounded shadow-lg shadow-gray-100 bg-link hover:bg-link-shade" on:click={addRecordToMap}>Add</button>

        </div>
        <div>
            {#each addedRecords as record}
                <div class="flex justify-start lg:px-72 font-semibold pt-4 text-white">
                    <p class="px-2">{record.key}</p>
                    <p class="px-2">{record.value} kg</p>
                </div>
            {/each}
        </div>
        <div class="text-center mt-6">
            {#if addedRecords.length > 0}
                <p class="text-textgray">Finished?</p>
                <button on:click={saveAndRedirect} class="text-link hover:text-link-shade hover:cursor-pointer">
                    Save
                </button>
            {:else}
                <button on:click={saveAndRedirect} class="text-link hover:text-link-shade hover:cursor-pointer">
                    Skip this step for now
                </button>
            {/if}
        </div>
    </div>
</div>

<style>

</style>
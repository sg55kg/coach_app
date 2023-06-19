<script lang="ts">

    import {AthleteRecord} from "../../../../lib/classes/user/athlete/records";
    import UserService from "../../../../lib/service/UserService";
    import {userDB} from "../../../../lib/stores/authStore";
    import {onMount} from "svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";

    let recordsList: string[] = [];
    let selectedRecord: string = '';
    let loading: boolean = true;
    const cachedRecordHistories: Map<string, AthleteRecord> = new Map<string, AthleteRecord>();

    const toggleRecord = (record: string) => {
        if (selectedRecord === record) {
            selectedRecord = '';
        } else {
            selectedRecord = record;
        }
    }

    const handleTextChange = (name: string) => {
        debounce(searchRecordsByName(name), 1100);
    };

    const debounce = (func, delay) => {
        let timeoutId;

        return function (...args) {
            clearTimeout(timeoutId);

            timeoutId = setTimeout(() => {
                func.apply(this, args);
            }, delay);
        };
    };

    const searchRecordsByName = async (name: string) => {
        try {
            const res = await UserService.fetchAthleteRecords()
        } catch (e) {
            console.log(e);
        }
    };

    const fetchCommonRecords = async () => {
        loading = true;
        try {
            const res = await UserService.getCommonAthleteRecords($userDB!.athleteData.id);
            console.log(res);
            res.forEach((record: AthleteRecord) => {
                cachedRecordHistories.set(record.exerciseName, record);
                recordsList.push(record.exerciseName);
            });
        } catch (e) {
            console.log(e); // TODO: handle error
        } finally {
            loading = false;
        }
    }

    onMount(async () => {
       await fetchCommonRecords();
    });

</script>

<header class="flex justify-between">
    <h1 class="p-2 text-2xl">My Progress</h1>
    <button class="text-link p-2 text-sm">Edit Records</button>
</header>
{#if !loading}
    <div class="p-2 w-full">
        <input type="text" class="bg-gray-300 p-1 rounded w-full" placeholder="Search for records" on:change={(e) => handleTextChange(e.target.value)} />
    </div>
    <div class="grid grid-cols-12 gap-3 w-full p-2 mt-2">
        {#each recordsList as record}
            <div on:click={() => toggleRecord(record)}
                 class="rounded-xl border border-textblue text-textblue text-xs lg:text-normal text-center p-1 col-span-4 lg:col-span-1 hover:cursor-pointer"
                 class:selected-record={selectedRecord === record}
            >
                {record}
            </div>
        {/each}
    </div>
    <div>
        {#if !selectedRecord}
            <p class="text-center text-textblue">Select exercises above to see your records</p>
        {:else}
            <div class="p-2">
                <h2 class="text-lg text-textblue">{cachedRecordHistories.get(selectedRecord).exerciseName}</h2>
                <p>{cachedRecordHistories.get(selectedRecord).weight}kg</p>
                <p>Set on: {cachedRecordHistories.get(selectedRecord).createdAt.format('MMMM DD YYYY')}</p>
            </div>
        {/if}
    </div>
{:else}
    <LoadingSpinner spinnerColor="fill-yellow" />
{/if}


<style>
    .selected-record {
        border-color: #fde577 !important;
        color: #fde577 !important;
    }
</style>
<script lang="ts">
    import { fly } from 'svelte/transition';
    import IoMdClose from 'svelte-icons/io/IoMdClose.svelte';
    import {AthleteRecord} from "../../classes/user/athlete/records";
    import UserService from "../../service/UserService";
    import {userDB} from "../../stores/authStore";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";

    export let show: boolean = false;

    let success: string = '';
    let error: string = '';
    let loading: boolean = false;

    let record: AthleteRecord = new AthleteRecord();

    const handleAddRecord = async () => {
        loading = true;
        error = '';
        success = '';
        try {
            const res = await UserService.createAthleteRecord($userDB!.athleteData.id, record);
            if (res) {
                success = `Successfully updated your ${record.numReps}RM ${record.exerciseName}`;
            } else {
                error = `Can't update ${record.exerciseName} at ${record.weight}kg, it already exists in our records`;
            }
        } catch (e) {
            console.log(e);
            error = 'Sorry, an unknown error occurred on our end';
        } finally {
            loading = false;
        }
    }

    $: success
        ? setTimeout(() => {
            success = '';
            record = new AthleteRecord();
        }, 3000)
        : null;

</script>

<div class="fixed top-0 right-0 left-0 bottom-0 opacity-25 bg-gray-300 z-10" on:click={() => show = false}></div>
<dialog class="m-auto bg-gray-200 rounded shadow-lg shadow-gray-100 w-11/12 lg:w-6/12 p-2 lg:p-4 z-20 relative" open="{show}">
    <h1 class="text-2xl text-yellow-lt text-center">Add Personal Record</h1>
    <button
            class="absolute right-2 top-2 lg:right-4 lg:top-4 h-8 w-8 cursor-pointer"
            on:click="{() => (show = false)}"
    >
        <span class="text-white">
            <IoMdClose />
        </span>
    </button>
    <div class="flex flex-col py-4 text-textgray">
        <input type="text" class="bg-gray-300 p-1 rounded m-3" placeholder="Exercise Name" bind:value={record.exerciseName} />
        <div class="grid grid-cols-2 gap-4">
            <div class="flex flex-col m-3">
                <small>Weight (kg)</small>
                <input type="number" class="bg-gray-300 p-1 rounded" placeholder="Weight" bind:value={record.weight} />
            </div>
            <div class="flex flex-col m-3">
                <small>Reps</small>
                <input type="number" class="bg-gray-300 p-1 rounded" placeholder="# of Reps" bind:value={record.numReps} />
            </div>
        </div>
        <button class="m-4 mt-8 flex cursor-pointer justify-center rounded bg-purple-600 p-2
                       text-white shadow-md shadow-gray-100 hover:bg-purple-500
                       disabled:cursor-not-allowed disabled:bg-purple-900"
                disabled="{!record.weight || !record.numReps || !record.exerciseName || loading || success}"
                on:click={handleAddRecord}
        >
            {#if !loading && !success}
                <span>Add Record</span>
            {:else if loading}
                <LoadingSpinner
                    spinnerColor="fill-white"
                    spinnerBackground="text-purple-600"
                />
            {:else if success}
                <span in:fly="{{ x: 50, duration: 1000 }}">{success}</span>
            {/if}
        </button>
        {#if error}
            <p class="p-2 text-center text-red">
                {error}
            </p>
        {/if}
    </div>
</dialog>

<style></style>
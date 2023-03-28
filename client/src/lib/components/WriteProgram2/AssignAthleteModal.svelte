<script lang="ts">
    import {getContext} from "svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {AthleteData} from "$lib/classes/user/athlete";

    export let show: boolean = false

    const { getProgram } = getContext('program')
    const program = getProgram()

    let loading: boolean = false
    let success: boolean = false
    let error: boolean = false

    let selectedAthlete: AthleteData = new AthleteData()

</script>

<div class="absolute w-screen h-screen bg-gray-300 opacity-50 z-[100] lg:-mt-[7em]" on:click={() => show = false}></div>
<div class="absolute bottom-10 top-10 left-10 lg:left-72 lg:right-72 lg:bottom-20 right-10 lg:items-center
            bg-gray-200 z-[105] flex flex-col rounded shadow-md shadow-black">
    <header class="flex justify-center p-4">
        <h1 class="text-2xl font-bold text-yellow-lt lg:text-4xl text-center">Assign Athlete to {$program.name}</h1>
    </header>
    <div>

    </div>
    <div class="flex justify-center mt-8">
        <button class="bg-yellow rounded text-gray-300 p-2 px-4 m-auto text-lg font-bold
                           hover:bg-yellow-shade disabled:bg-yellow-lt flex"
        >
            {#if loading}
                <LoadingSpinner spinnerColor="fill-gray-300" spinnerBackground="text-yellow-lt" />
            {/if}
            Assign {selectedAthlete.name ? `to ${selectedAthlete.name}` : ''}
        </button>
    </div>
</div>

<style></style>
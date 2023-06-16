<script lang="ts">
    import {getContext, onMount} from "svelte";
    import {ProgramService} from "../../../service/ProgramService";
    import {userDB} from "../../../stores/authStore";
    import {DisplayProgram} from "../../../classes/program";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import IoMdDocument from 'svelte-icons/io/IoMdDocument.svelte'

    export let show: boolean = false;

    const { getProgramError } = getContext('program');
    let programError = getProgramError();

    let templates: DisplayProgram[] = [];
    let loading: boolean = false;

    onMount(async () => {
        loading = true;
        try {
            templates = await ProgramService.getCoachTemplates($userDB!.coachData.id);
        } catch (e) {
            console.log(e);
            $programError = 'Error retrieving your templates';
        } finally {
            loading = false
        }
    })
</script>

<div class="fixed top-0 left-0 right-0 bottom-0 z-30 bg-gray-300 opacity-25" on:click={() => show = false}></div>
<dialog class="fixed margin-auto bg-gray-200 rounded shadow-gray-100 shadow-md z-[110] p-4 flex flex-col items-center" open="{show}">
    <h1 class="text-2xl text-yellow-lt">Templates</h1>
    {#if !loading}
        <div class="grid grid-cols-3 lg:grid-cols-6 gap-3 text-textblue p-8">
            {#each templates as template}
                <a href="/home/coach/program/{template.id}">
                    <div class="rounded-lg bg-gray-100 p-2 text-center hover:cursor-pointer hover:bg-gray-400">
                        <div class="rounded flex justify-center bg-gray-200 py-4">
                            <div class="w-10 h-10"><IoMdDocument /></div>
                        </div>
                        <p>{template.name}</p>
                    </div>
                </a>
            {/each}
        </div>
    {:else}
        <div class="flex w-full h-full justify-center items-center">
            <LoadingSpinner />
        </div>
    {/if}
</dialog>


<style></style>
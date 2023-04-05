<script lang="ts">
    import FaEllipsisV from 'svelte-icons/fa/FaEllipsisV.svelte'
    import {isMobile} from "$lib/stores/authStore.js";
    import {DisplayProgram} from "../../classes/program";
    import {ProgramService} from "../../service/ProgramService";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {userDB} from "../../stores/authStore";
    import {goto} from "$app/navigation";

    let viewFilters: boolean = !$isMobile
    let searchName: string = ''
    let searchResults: DisplayProgram[] = []
    let loading: boolean = false
    let success: boolean = false

    function debounce (func, timeout = 500) {
        let timer
        return (...args) => {
            clearTimeout(timer)
            timer = setTimeout(() => { func.apply(this, args) }, timeout)
        }
    }

    const searchPrograms = async () => {
        if (!searchName) return
        success = false
        loading = true
        try {
            const programResults = await ProgramService.searchProgramsByName($userDB.coachData.id, searchName)
            searchResults = programResults
            success = true
        } catch (e) {
            console.log(e)
        } finally {
            loading = false
        }
    }

    const processSearch = debounce(() => searchPrograms())

</script>

{#if $isMobile}
    <div class="h-6 w-1/12 flex justify-end" on:click={() => viewFilters = !viewFilters}>
        <FaEllipsisV />
    </div>
{/if}
{#if !$isMobile}
    <div class="w-full">
        <input class="bg-gray-300 p-1 w-full" placeholder="Type to search programs" bind:value={searchName} on:keyup={() => processSearch()}>
    </div>
    {#if loading}
        <div class="absolute z-[110] top-0 bottom-0 right-0 left-0 bg-gray-300 opacity-25">
        </div>
        <div class="absolute bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center">
            <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
        </div>
    {/if}
    {#if success}
        <div class="absolute z-[110] top-[10em] bottom-0 right-0 left-0" on:click={() => {success = false; searchResults = []}}></div>
        <div class="absolute z-[115] top-[10em] right-10 bottom-0 left-10 bg-gray-100">
            {#each searchResults as result}
<!--                <a href={"/home/coach/program/" + result.id} >-->
                    <div class="m-2 rounded bg-gray-200 p-3 hover:bg-gray-400 hover:cursor-pointer" on:click={async() => await goto(`/home/coach/program/${result.id}`)}>
                        <h3 class="text-md font-semibold">{result.name}</h3>
                        <p>{result.athleteName ? 'Athlete: ' + result.athleteName : 'Template'}</p>
                        <p>Start Date: {result.startDate ? result.startDate.format('ddd MMMM DD') : 'None'}</p>
                        <p>End Date: {result.endDate ? result.endDate.format('ddd MMMM DD') : 'None'}</p>
                    </div>
<!--                </a>-->
            {/each}
        </div>
    {/if}
{/if}
<!--{#if viewFilters}-->
<!--    <div class="{$isMobile ? 'absolute h-[70vh] bg-red top-24 right-3 left-3 z-20' : 'w-full'} flex flex-col">-->
<!--        <div>-->
<!--            <input type="text" class="bg-gray-300 p-1" placeholder="Search Programs">-->
<!--            <h1 class="text-2xl">fksdjfkldsfjlksd</h1>-->
<!--        </div>-->
<!--    </div>-->
<!--    {#if $isMobile}-->
<!--        <div class="w-screen h-screen z-10" on:click={() => viewFilters = false}></div>-->
<!--    {/if}-->
<!--{/if}-->


<style></style>